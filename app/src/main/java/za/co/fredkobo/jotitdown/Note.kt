package za.co.fredkobo.jotitdown

import java.util.*

data class Note(
    var id: String = "",
    val title: String = "",
    val body: String = "",
    val date: Long = -1L
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "title" to title,
            "body" to body,
            "date" to date
        )
    }
}