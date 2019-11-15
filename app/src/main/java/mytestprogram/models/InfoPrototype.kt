package mytestprogram.models

import java.util.Calendar

abstract class InfoPrototype {

    abstract val id: Int
    abstract val action: String
    abstract val description: String
    abstract val nameDevice: String
    abstract val isImportant: Boolean
    abstract val isInTrash: Boolean
    abstract val dateCreate: Calendar
    abstract val levelPrivacy: Int
    abstract val password: String

    companion object {
        const val PUBLIC = 0
        const val READONLY = 1
        const val PRIVATE = 2
    }
}