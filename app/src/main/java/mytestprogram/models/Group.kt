package mytestprogram.models

import java.util.Calendar
import kotlin.collections.ArrayList

class Group {

    val links = mutableMapOf<Int, String>()
    val arrayDeadLines = arrayListOf<Calendar>()
    val arrayCheckTimes = arrayListOf<Calendar>()

    companion object {

        fun createGroup(array: ArrayList<Container>, lambda: Container.() -> Boolean) =
            Group().apply {
                for (info in array)
                    if (lambda(info))
                        addNote(info)
            }
    }

    private val notes =  arrayListOf<Container>()

    fun getNotes() = notes

    fun addNote(info: Container) {
        info.apply {
            notes += this
            //links += id to action
            if (deadLine is Calendar) arrayDeadLines += deadLine
            if (times.isNotEmpty())
                arrayCheckTimes.addAll(times)
        }
    }
}