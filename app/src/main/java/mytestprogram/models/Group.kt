package mytestprogram.models

import java.util.Calendar
import kotlin.collections.ArrayList

class Group {

    val links = mutableMapOf<Int, String>()
    val arrayDeadLines = arrayListOf<Calendar>()
    val arrayCheckTimes = arrayListOf<Calendar>()

    companion object {

        fun createGroup(array: ArrayList<InfoPrototype>, lambda: InfoPrototype.() -> Boolean) =
            Group().apply {
                for (info in array)
                    if (lambda(info))
                        addNote(info)
            }
    }

    private val notes =  arrayListOf<InfoPrototype>()

    fun getNotes() = notes

    fun addNote(info: InfoPrototype) {
        info.apply {
            notes += this
            links += id to action
            if (this is InfoTask ) {
                if (deadLine is Calendar) arrayDeadLines += deadLine!!
                if (checkTimes.isNotEmpty())
                    arrayCheckTimes.addAll(checkTimes)

            }
        }
    }

}