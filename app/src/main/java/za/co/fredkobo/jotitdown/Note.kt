package za.co.fredkobo.jotitdown

import java.util.*

data class Note(val uid:String, val title:String, val body:String, val date: Long) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "title" to title,
            "body" to body,
            "date" to date
        )
    }
}