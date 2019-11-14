package mytestprogram.models

import java.util.*
import kotlin.collections.ArrayList

fun calendarToString(calendar: Calendar) =
    "${calendar[Calendar.SECOND]}.${calendar[Calendar.MINUTE]}.${calendar[Calendar.HOUR]}." +
            "${calendar[Calendar.DAY_OF_MONTH]}.${calendar[Calendar.MONTH]}.${calendar[Calendar.YEAR]}"

fun stringToCalendar(string: String) =
    GregorianCalendar().apply {
        val measures = string.split(".")
        if (measures.size == 6) {
            try {
                this[Calendar.SECOND] = measures[0].toInt()
                this[Calendar.MINUTE] = measures[1].toInt()
                this[Calendar.HOUR] = measures[2].toInt()
                this[Calendar.DAY_OF_MONTH] = measures[3].toInt()
                this[Calendar.MONTH] = measures[4].toInt()
                this[Calendar.YEAR] = measures[5].toInt()
            }
            catch (ex: Exception){}
        }
    }

fun linksToString(links: ArrayList<Int>) = ""

fun stringToLinks(string: String) = null

fun pathsToString(array: ArrayList<String>) = ""

fun stringToPaths(string: String) = null

fun stringToCheckTime(string: String) = null

fun checkTimeToString(array: ArrayList<Calendar>) = ""

fun intToType(type: Int) =
    when(type) {
        0 -> InfoType.RECORD
        1 -> InfoType.TASK
        2 -> InfoType.LIST
        3 -> InfoType.SCHEDULE
        else -> InfoType.NONE
    }

fun privacyToInt(privacy: InfoPrototype.LevelPrivacy) =
    when (privacy) {
        InfoPrototype.LevelPrivacy.PUBLIC -> 0
        InfoPrototype.LevelPrivacy.READONLY -> 1
        InfoPrototype.LevelPrivacy.PRIVATE -> 2
    }

fun intToPrivacy(int: Int) =
    when (int){
        2 -> InfoPrototype.LevelPrivacy.PRIVATE
        1 -> InfoPrototype.LevelPrivacy.READONLY
        else -> InfoPrototype.LevelPrivacy.PUBLIC
    }

fun typeToInt(type: InfoType) =
    when(type) {
        InfoType.RECORD -> 0
        InfoType.TASK -> 1
        InfoType.LIST -> 2
        InfoType.SCHEDULE -> 3
        InfoType.NONE -> -1
    }