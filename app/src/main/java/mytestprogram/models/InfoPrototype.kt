package mytestprogram.models

import java.util.Calendar

/*
    InfoPrototype представляет собой сущность какой-то информации.
    свойства:
        components - представляет некий аналог кол-ва элементов свойств, доступных для пользователя
        id - индекс информации(необходим как для нахождения, так и для работы со ссылками)
        type - характеризует данную информации на сущность в InfoType
        action - текст, для выполнения действий
        description - описание действия подробней
        link - ссылка на другую информацию
        dateCreate - дата создания записи
        dateDeadLine - дата уничтожения записи // актуально для типа Task

    функции:
        convertToInsertQuery - позволяет создавать относительно запрос в БД на добавление записи в конкретную таблицу

    заметки на будующее:
        1) можно прикреплять несколько ссылок для одной записи
        2) ограничение выполнения задания относительно геоположения человека
 */

class InfoPrototype (
    val type: InfoType = InfoType.NONE,
    val action: String = "",
    val id: Int = -1,
    val description: String = "",
    val dateCreate: Calendar = Calendar.getInstance(),
    val isImportant: Boolean = false,
    val dateDeadLine: Calendar? = null,
    val links: ArrayList<Int>? = null,
    val paths: ArrayList<String>? = null,
    val checkTimes: ArrayList<Calendar>? = null,
    val levelPrivacy: LevelPrivacy = LevelPrivacy.PUBLIC,
    val password: String = "",
    val nameOfDevice: String = "",
    val isInTrash: Boolean = false
) {
    enum class LevelPrivacy(val symbol: String) {
        PRIVATE("private"),
        READONLY("readOnly"),
        PUBLIC("public")
    }
}