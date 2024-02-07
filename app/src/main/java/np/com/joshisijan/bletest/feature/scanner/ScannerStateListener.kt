package np.com.joshisijan.bletest.feature.scanner

import android.bluetooth.BluetoothDevice

interface ScannerStateListener {
    fun scannerInitial(reset: Boolean = false)
    fun scannerScanning()
    fun scannerDeviceFound(device: BluetoothDevice)
    fun scannerError(message: String)
}