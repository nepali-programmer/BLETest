package np.com.joshisijan.bletest.feature.broadcast

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID


class BroadcastActivityViewModel: ViewModel() {
    var bluetoothAdapter: BluetoothAdapter? = null
    var broadcastStateListener: BroadcastStateListener? = null
    private var btBroadcastThread: BtBroadcastThread? = null

    fun startBroadcast(context: Context) {
        broadcastStateListener?.initial()
        if(bluetoothAdapter == null) {
            return
        }
        btBroadcastThread = BtBroadcastThread(bluetoothAdapter)
        btBroadcastThread?.start()
        btBroadcastThread?.run()
        broadcastStateListener?.broadcasting()
    }

    fun sendRandomMessage() {
        btBroadcastThread?.sendRandomMessage()
    }

    fun stopBroadcast(context: Context) {
        if(bluetoothAdapter == null) {
            return
        }
        btBroadcastThread?.close()
        btBroadcastThread?.stop()
        broadcastStateListener?.initial()
    }
}

class BtBroadcastThread(private val bluetoothAdapter: BluetoothAdapter?): Thread() {
    private var bluetoothServerSocket: BluetoothServerSocket? = null
    private var socket: BluetoothSocket? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private val broadcastName = "bluetooth_broadcast"
    fun run(context:  Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val broadcastUUID: UUID = UUID.fromString("5a46b6b3-1e2e-4d3d-84c6-0ca173b9ba69")
        bluetoothServerSocket = bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord(broadcastName, broadcastUUID)
        socket = bluetoothServerSocket?.accept()
        inputStream = socket?.inputStream
        outputStream = socket?.outputStream
    }

    fun close() {
        socket?.close()
        bluetoothServerSocket?.close()
    }

    fun sendRandomMessage() {
        val message = UUID.randomUUID().toString().toByteArray()
        outputStream?.write(message)
        outputStream?.flush()
    }
}