package np.com.joshisijan.bletest.feature.device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import np.com.joshisijan.bletest.R

class DeviceActivity : AppCompatActivity() {
    private val tag = "ScannerLog"

    private var viewModel: DeviceActivityViewModel? = null

    private var device: BluetoothDevice? = null

    private var deviceTitle: TextView? = null
    private var connectButton: Button? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[DeviceActivityViewModel::class.java]

        @Suppress("DEPRECATION")
        device = intent.getParcelableExtra<BluetoothDevice>("device")
        Log.i(tag, device?.name ?: "Unnamed")
        setContentView(R.layout.activity_device)

        deviceTitle = findViewById(R.id.activityDeviceDeviceName)
        connectButton = findViewById(R.id.activityDeviceConnectButton)
        connectButton?.setOnClickListener {
            viewModel?.connectGATT(this, device)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        deviceTitle?.text = device?.name ?: ""
    }
}