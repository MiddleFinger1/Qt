package mytestprogram.models

import java.util.Calendar

data class InfoTask (
    override val id: Int = -1,
    override val action: String,
    override val description: String = "",
    override val nameDevice: String = "",
    override val isImportant: Boolean = false,
    override val isInTrash: Boolean = false,
    override val dateCreate: Calendar = Calendar.getInstance(),
    override val levelPrivacy: Int = PUBLIC,
    override val password: String = "",
             var links: ArrayList<Int> = arrayListOf(),
             var checkTimes: ArrayList<Calendar> = arrayListOf(),
             var deadLine: Calendar? = null
) : InfoPrototype()