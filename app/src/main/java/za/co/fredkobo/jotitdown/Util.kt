package za.co.fredkobo.jotitdown

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

private val simpleDateFormat = SimpleDateFormat("E, dd MMM yyyy HH:mm", Locale.ENGLISH)

fun formatDate(time: Long): String? {
    return if (DateUtils.isToday(time)) {
        "Today"
    } else if (DateUtils.isToday(time + 86400000)) {
        "Yesterday"
    } else {
        simpleDateFormat.format(Date(time))
    }
}