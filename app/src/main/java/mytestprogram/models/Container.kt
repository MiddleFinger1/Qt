package mytestprogram.models

import java.util.Calendar

data class Container(
    val id: Int = -1,
    val action: String,
    val description: String = "",
    val nameDevice: String = "",
    val isImportant: Boolean = false,
    val isInTrash: Boolean = false,
    val dateCreate: Calendar = Calendar.getInstance(),
    val privacy: Int = PUBLIC,
    val password: String = "",
    val deadLine: Calendar? = null,
    val links: ArrayList<Int> = arrayListOf(),
    val paths: ArrayList<String> = arrayListOf(),
    val items: ArrayList<Item> = arrayListOf(),
    val times: ArrayList<Calendar> = arrayListOf()
) {
    companion object {
        const val PUBLIC = 0
        const val READONLY = 1
        const val PRIVATE = 2
    }
}