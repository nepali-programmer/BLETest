package np.com.joshisijan.bletest.feature.scanner

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import np.com.joshisijan.bletest.R
import np.com.joshisijan.bletest.feature.device.DeviceActivity

class ScannerListAdapter(private val context: Context, private val list: MutableList<BluetoothDevice>): RecyclerView.Adapter<ScannerListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScannerListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.scan_list_item, parent, false)
        return ScannerListViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ScannerListViewHolder, position: Int) {
        val device: BluetoothDevice = list[position]
        holder.deviceName.text = device.name ?: "Unknown Device Name"
        holder.macAddress.text = device.address ?: "Unknown Device Address"
        holder.viewDetailButton?.setOnClickListener {
            val intent = Intent(context, DeviceActivity::class.java)
            intent.putExtra("device", device)
            context.startActivity(intent)
        }
    }

    fun resetList() {
        list.clear()
        notifyDataSetChanged()
    }

    fun addItem(device: BluetoothDevice) {
        list.add(device)
        notifyItemInserted(list.size - 1)
    }
}