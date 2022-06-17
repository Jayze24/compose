package compose.utils

import android.os.SystemClock

/**
 * made by Yu JaeHa
 * ----------
 * HOW TO USE
 * ----------
 *  example)
 *   val singleClickListener by remember {mutableStateOf(OnSingleClickListener(interval = "clickInterval", onSingleClick = "onClick"))}
 *   Button(..,onClick = { singleClickListener() })
 *  parameter)
 *   interval : clickable interval.
 *   onSingleClick : code to run.
 **/

class OnSingleClickListener(
    private val interval : Int = 600,
    private val onSingleClick: () -> Unit) : ()->Unit {

    private var lastClickTime: Long = 0

    override fun invoke() {
        val elapsedRealtime = SystemClock.elapsedRealtime()
        if ((elapsedRealtime - lastClickTime) < interval) {
            return
        }
        lastClickTime = elapsedRealtime
        onSingleClick()
    }
}