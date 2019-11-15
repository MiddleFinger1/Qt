package models

import java.util.Calendar
import java.util.GregorianCalendar
import kotlin.collections.ArrayList

fun calendarToString(calendar: Calendar?) =
    if (calendar != null) "${calendar[Calendar.SECOND]}.${calendar[Calendar.MINUTE]}.${calendar[Calendar.HOUR]}." +
            "${calendar[Calendar.DAY_OF_MONTH]}.${calendar[Calendar.MONTH]}.${calendar[Calendar.YEAR]}"
    else ""

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

fun linksToString(links: ArrayList<Int>): String {
    var string = ""
    for (link in links) string += "$link|"
    return string
}

fun stringToLinks(string: String) =
    arrayListOf<Int>().apply {
        for (item in string.split("|"))
            if (item != "") add(item.toInt())
    }

fun pathsToString(array: ArrayList<String>): String {
    var string = ""
    for (item in array) string += "$item|"
    return string
}

fun stringToPaths(string: String) =
    arrayListOf<String>().apply {
        addAll(string.split("|").filter { it != "" })
    }

fun stringToCheckTime(string: String) =
    arrayListOf<Calendar>().apply {
        addAll(string.split("|").map { stringToCalendar(it) })
    }

fun checkTimeToString(array: ArrayList<Calendar>): String {
    var string = ""
    for (item in array) string += calendarToString(item) + "|"
    return string
}