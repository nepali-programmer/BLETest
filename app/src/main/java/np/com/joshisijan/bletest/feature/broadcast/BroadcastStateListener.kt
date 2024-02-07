package np.com.joshisijan.bletest.feature.broadcast

interface BroadcastStateListener {
    fun initial()
    fun broadcasting()
    fun error(message: String)
}