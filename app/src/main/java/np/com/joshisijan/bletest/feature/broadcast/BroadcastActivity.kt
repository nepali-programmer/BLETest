package np.com.joshisijan.bletest.feature.broadcast

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import np.com.joshisijan.bletest.R

class BroadcastActivity : AppCompatActivity(), BroadcastStateListener {
    private val tag = "BleBroadcast"

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var broadcastButton: MaterialButton? = null
    private var stopButton: MaterialButton? = null
    private var randomButton: MaterialButton? = null
    private var broadcastStatus: MaterialTextView? = null

    private var viewModel: BroadcastActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_broadcast)

        viewModel = ViewModelProvider(this)[BroadcastActivityViewModel::class.java]

        bluetoothAdapter = (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

        viewModel?.bluetoothAdapter = bluetoothAdapter

        viewModel?.broadcastStateListener = this
        viewModel?.broadcastStateListener?.initial()

        broadcastButton = findViewById(R.id.activityBroadcastBroadcast)
        broadcastStatus = findViewById(R.id.broadcastStatus)
        broadcastButton?.setOnClickListener{
            viewModel?.startBroadcast(this)
        }
        stopButton = findViewById(R.id.activityBroadcastStop)
        stopButton?.setOnClickListener{
            viewModel?.stopBroadcast(this)
        }
        randomButton = findViewById(R.id.activityBroadcastRandomMessage)
        randomButton?.setOnClickListener{
            viewModel?.sendRandomMessage()
        }
    }

    override fun initial() {
        Log.i(tag, "Broadcast Initial")
        broadcastStatus?.text = getString(R.string.no_broadcast)
    }

    override fun broadcasting() {
        Log.i(tag, "Broadcast Broadcasting")
        broadcastStatus?.text = getString(R.string.broadcasting)
    }

    override fun error(message: String) {
        Log.i(tag, "Broadcast Error")
        broadcastStatus?.text = message
    }
}