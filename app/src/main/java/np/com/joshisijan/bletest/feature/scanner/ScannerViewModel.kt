package np.com.joshisijan.bletest.feature.scanner

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.Permission

class ScannerViewModel() : ViewModel() {
    var scannerStateListener: ScannerStateListener? = null
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var btReceiver: ScannerBroadcastReceiver? = null

    private val bluetoothScanRequestCode = 1
    private val backgroundLocationRequestCode = 2



    fun init(context: Context, activity: Activity) {
        btReceiver = ScannerBroadcastReceiver(scannerStateListener)
        scannerStateListener?.scannerInitial()
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null) {
            scannerStateListener?.scannerError("No bluetooth adapter found")
        } else {
            /// request for initial permission
            ActivityCompat.requestPermissions(
                activity,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    arrayOf(
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT,
                    )
                } else {
                    arrayOf(
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                    )
                },
                bluetoothScanRequestCode
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    backgroundLocationRequestCode
                )
            }

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                scannerStateListener?.scannerError("No permission granted for bluetooth")
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                scannerStateListener?.scannerError("No permission granted for background location")
            }
        }
    }

    fun initBluetoothListener(context: Context) {
        val intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(context, btReceiver, intentFilter, ContextCompat.RECEIVER_EXPORTED)
    }

    fun stopScan(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        bluetoothAdapter?.cancelDiscovery()
        scannerStateListener?.scannerInitial()
    }

    fun startScan(context: Context) {
        try {
            scannerStateListener?.scannerInitial(reset = true)
            /// scanning
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                return
            }

            scannerStateListener?.scannerScanning()
            CoroutineScope(Dispatchers.Main).launch {
                delay(30000)
                bluetoothAdapter?.cancelDiscovery()
                scannerStateListener?.scannerInitial()
            }

            bluetoothAdapter?.startDiscovery()
        } catch (e: Exception) {
            scannerStateListener?.scannerError(e.message ?: "An Error Occurred")
        }
    }
}