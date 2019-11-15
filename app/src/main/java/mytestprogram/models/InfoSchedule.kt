package mytestprogram.models

import java.util.Calendar

data class InfoSchedule (
    override val id: Int = -1,
    override val action: String,
    override val description: String = "",
    override val nameDevice: String = "",
    override val isImportant: Boolean = false,
    override val isInTrash: Boolean = false,
    override val dateCreate: Calendar = Calendar.getInstance(),
    override val levelPrivacy: Int = PUBLIC,
    override val password: String = "",
             var groupScheduleItems: ArrayList<InfoScheduleItem> = arrayListOf()
) : InfoPrototype()