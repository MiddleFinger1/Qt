package mytestprogram.models

open class MainGroup {

    var maxIndex = 0
    private set

    private val notes = arrayListOf<InfoPrototype>()
    private val groups = arrayListOf<MainGroup>()

    // properties notes
    val addresses = arrayListOf<Int>()
    val titles = mutableMapOf<String, Unit>()

    /*
    функции:
        createGroup - создание группы
        addNote - добавляет запись
        removeNote - удаляет запись
        updateNote - обновляет запись
     */

    fun createGroup(function: InfoPrototype.() -> Boolean) = MainGroup().apply {
        for (note in this@MainGroup.notes)
            if (note.function())
                addNote(note)
    }

    infix fun addNote(info: InfoPrototype){
        notes += info
        if (info.id >= maxIndex) maxIndex ++

        addresses += info.id
        if (titles[info.action] == null && info.type == InfoType.LIST)
            titles[info.action] = Unit

    }

    fun getNotes() = notes

    infix fun addNotes(group: MainGroup){
        for (note in group.notes){
            if (findNote(note.id) != null)
                addNote(note)
        }
    }

    fun findNote(id: Int): InfoPrototype? {
        for (note in notes)
            if (note.id == id) return note
        return null
    }

    fun hasNote(info: InfoPrototype) = info in notes

    fun removeNote(id: Int){
        val note = notes.find { it.id  == id}
        notes.remove(note)
    }

    fun removeNote(info: InfoPrototype){

    }

    fun updateNote(replaced: InfoPrototype, updated: InfoPrototype){
        notes.remove(replaced)
        addNote(updated)
    }


}