package np.com.joshisijan.bletest.feature.scanner

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import np.com.joshisijan.bletest.R


class ScannerActivity : AppCompatActivity(), ScannerStateListener {
    private val tag = "ScannerStatus"

    private var viewModel: ScannerViewModel? = null

    private var scanStatus: TextView? = null
    private var scanButton: Button? = null
    private var listRecyclerView: RecyclerView? = null
    private var scannerListAdapter: ScannerListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[ScannerViewModel::class.java]
        viewModel?.scannerStateListener = this

        scanStatus = findViewById(R.id.scanStatus)
        scanButton = findViewById(R.id.scanButton)

        listRecyclerView = findViewById(R.id.listRecyclerView)
        val layoutManager = LinearLayoutManager(this)
        listRecyclerView?.layoutManager = layoutManager
        scannerListAdapter = ScannerListAdapter(mutableListOf())
        listRecyclerView?.adapter = scannerListAdapter

        scanButton?.setOnClickListener {
            if (scanButton?.text == getString(R.string.start_scan)) {
                viewModel?.startScan(this)
            } else {
                viewModel?.stopScan(this)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel?.init(this, this)
        viewModel?.initBluetoothListener(this)
    }

    override fun scannerInitial(reset: Boolean) {
        Log.i(tag, "Scanner Initial")
        scanButton?.text = getString(R.string.start_scan)
        scanStatus?.text = getString(R.string.click_to_scan)

        if (reset) {
            scannerListAdapter?.resetList()
        }
    }

    override fun scannerScanning() {
        Log.i(tag, "Scanner Scanning")
        scanButton?.text = getString(R.string.stop_scan)
        scanStatus?.text = getString(R.string.scanning)
    }

    @SuppressLint("MissingPermission")
    override fun scannerDeviceFound(device: BluetoothDevice) {
        Log.i(tag, "Scanner Device Found")
        if (device.name != null) {
            scannerListAdapter?.addItem(device)
        }
    }

    override fun scannerError(message: String) {
        Log.i(tag, "Scanner Error")
        scanStatus?.text = message
    }
}