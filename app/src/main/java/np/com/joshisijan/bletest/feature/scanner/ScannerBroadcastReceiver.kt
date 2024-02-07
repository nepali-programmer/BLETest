package np.com.joshisijan.bletest.feature.scanner

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


@Suppress("DEPRECATION")
class ScannerBroadcastReceiver(private val scannerStateListener: ScannerStateListener?) :
    BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) {
            return
        }
        when (intent?.action) {
            BluetoothDevice.ACTION_FOUND -> {
                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                if(device == null) {
                    return
                }
                scannerStateListener?.scannerDeviceFound(device)
            }
        }
    }
}