package mytestprogram.models

import java.util.Calendar

data class InfoList (
    override val id: Int = -1,
    override val action: String,
    override val description: String = "",
    override val nameDevice: String = "",
    override val isImportant: Boolean = false,
    override val isInTrash: Boolean = false,
    override val dateCreate: Calendar = Calendar.getInstance(),
    override val levelPrivacy: Int = PUBLIC,
    override val password: String = "",
             var groupListItems: ArrayList<InfoListItem> = arrayListOf()
) : InfoPrototype()