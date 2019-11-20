package mytestprogram.models

import java.util.Calendar

data class Item(
    val id: Int = -1,
    val parentId: Int = -1,
    val action: String,
    val description: String = "",
    val dateCreate: Calendar = Calendar.getInstance()
)