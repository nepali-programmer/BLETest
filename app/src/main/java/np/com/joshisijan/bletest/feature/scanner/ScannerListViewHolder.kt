package np.com.joshisijan.bletest.feature.scanner

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import np.com.joshisijan.bletest.R

class ScannerListViewHolder(item: View): RecyclerView.ViewHolder(item) {
    val deviceName = item.findViewById<TextView>(R.id.scanItemTitle)
    val macAddress = item.findViewById<TextView>(R.id.scanItemMacAddress)
    val viewDetailButton = item.findViewById<TextView>(R.id.viewDetailButton)
}