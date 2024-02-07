package np.com.joshisijan.bletest.feature.device

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel

class DeviceActivityViewModel : ViewModel() {
    private val tag = "ScannerLog"
    fun connectGATT(context: Context, device: BluetoothDevice?) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        device?.connectGatt(context, true, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                Log.i(tag, "Connection State Change: $status")
                super.onConnectionStateChange(gatt, status, newState)
            }

            override fun onCharacteristicWrite(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                Log.i(tag, "Characteristic Write")
                super.onCharacteristicWrite(gatt, characteristic, status)
            }

            override fun onDescriptorWrite(
                gatt: BluetoothGatt?,
                descriptor: BluetoothGattDescriptor?,
                status: Int
            ) {
                Log.i(tag, "Descriptor Write")
                super.onDescriptorWrite(gatt, descriptor, status)
            }
        })
    }
}