package za.co.fredkobo.jotitdown

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

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

fun isValidEmailAddress(email: String?): Boolean {
    val ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
    val p = Pattern.compile(ePattern)
    val m = p.matcher(email)
    return m.matches()
}