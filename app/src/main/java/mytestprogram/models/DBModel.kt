package mytestprogram.models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import mytestprogram.NavigationActivity
import kotlin.collections.ArrayList

class DBModel(private val context: Context): SQLiteOpenHelper(context, NAME_TABLE, null, VERSION){

    lateinit var mainGroup: Group
    private var maxId = 0
    private var maxListId = 0
    private var maxScheduleId = 0

    override fun onCreate(database: SQLiteDatabase?) {
        try {
            database!!.execSQL(CREATE_TABLES)
            maxId = findMaxId(TABLE_NOTES)
            maxListId = findMaxId(TABLE_LISTS)
            maxScheduleId = findMaxId(TABLE_SCHEDULES)
        }
        catch (ex: Exception) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        database!!.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        // настроить работу обновления базы с учетом сохранения существующих данных
        onCreate(database)
    }

    private fun findMaxId(table: String): Int {
        val resultSet = readableDatabase.rawQuery("SELECT $ID FROM $table ORDER BY $ID DESC LIMIT 1;", null)
        while (resultSet.moveToNext()) {
            val id = resultSet.getInt(resultSet.getColumnIndex(ID))
            resultSet.close()
            return id
        }
        return -1
    }

    fun createGroup(){
        mainGroup = Group.createGroup(select()) {
            true
        }
    }

    fun deleteNote(id: Int, childId: Int = -1){
        var sql = ""
        readableDatabase.rawQuery("SELECT * FROM $TABLE_NOTES WHERE $ID = $id;", null).apply {
            while (moveToNext()){
                val table = when (getInt(getColumnIndex(TYPE))) {
                    0 -> TABLE_RECORDS
                    1 -> TABLE_TASKS
                    2 -> TABLE_LISTS
                    3 -> TABLE_SCHEDULES
                    else -> ""
                }
                sql = if (childId >= 0) "DELETE FROM $table WHERE $PARENT_ID = $id AND $ID = $childId;"
                else "DELETE FROM $TABLE_NOTES WHERE $ID = $id;\nDELETE FROM $table WHERE $PARENT_ID = $id;"
            }
            close()
        }
        writableDatabase.execSQL(sql)
    }

    // pre-download properties in infoPrototype
    private fun selectDownloads(info: InfoPrototype){
        val query = when {
            (info is InfoRecord) -> "SELECT * FROM $TABLE_RECORDS WHERE $PARENT_ID = ${info.id};"
            (info is InfoTask) -> "SELECT * FROM $TABLE_TASKS WHERE $PARENT_ID = ${info.id};"
            (info is InfoList) -> "SELECT * FROM $TABLE_LISTS WHERE $PARENT_ID = ${info.id};"
            (info is InfoSchedule) -> "SELECT * FROM $TABLE_SCHEDULES WHERE $PARENT_ID = ${info.id};"
            else -> ""
        }
        readableDatabase.rawQuery(query, null).apply {
            while (moveToNext())
                when {
                    (info is InfoRecord) -> info.paths = stringToPaths(getString(getColumnIndex(PATHS)))
                    (info is InfoTask) -> {
                        info.links = stringToLinks(getString(getColumnIndex(LINKS)))
                        info.checkTimes = stringToCheckTime(getString(getColumnIndex(CHECK_TIME)))
                        info.deadLine = stringToCalendar(getString(getColumnIndex(DATE_DEADLINE)))
                    }
                    (info is InfoList) -> info.groupListItems.add(
                        InfoListItem(getInt(getColumnIndex(ID)), getInt(getColumnIndex(PARENT_ID)),
                            getString(getColumnIndex(ACTION)), getString(getColumnIndex(DESCRIPTION)))
                    )
                    (info is InfoSchedule) -> info.groupScheduleItems.add(
                        InfoScheduleItem(getInt(getColumnIndex(ID)), getInt(getColumnIndex(PARENT_ID)),
                            getString(getColumnIndex(ACTION)), getString(getColumnIndex(DESCRIPTION)),
                            stringToCalendar(getString(getColumnIndex(DATE_CREATE)))
                        ))
                }
            close()
        }
    }

    private fun select(): ArrayList<InfoPrototype> {
        val array = arrayListOf<InfoPrototype>()
        readableDatabase.rawQuery("SELECT * FROM $TABLE_NOTES", null).apply {
            while(moveToNext()) {
                val id = getInt(getColumnIndex(ID))
                val type = getInt(getColumnIndex(TYPE))
                val action = getString(getColumnIndex(ACTION))
                val description = getString(getColumnIndex(DESCRIPTION))
                val nameOfDevice = getString(getColumnIndex(NAME_OF_DEVICE))
                val isImportant = getInt(getColumnIndex(IS_IMPORTANT)) == 1
                val isInTrash = getInt(getColumnIndex(IS_IN_TRASH)) == 1
                val calendar = stringToCalendar(getString(getColumnIndex(DATE_CREATE)))
                val levelPrivacy = getInt(getColumnIndex(LEVEL_PRIVACY))
                val password = getString(getColumnIndex(PASSWORD))
                val info: InfoPrototype = when (type){
                    0 -> InfoRecord(id, action, description, nameOfDevice,
                        isImportant, isInTrash, calendar, levelPrivacy, password)
                    1 -> InfoTask(id, action, description, nameOfDevice,
                            isImportant, isInTrash, calendar, levelPrivacy, password)
                    2 -> InfoList(id, action, description, nameOfDevice,
                            isImportant, isInTrash, calendar, levelPrivacy, password)
                    3 -> InfoSchedule(id, action, description, nameOfDevice,
                            isImportant, isInTrash, calendar, levelPrivacy, password)
                    else -> throw Exception() //
                }
                //selectDownloads(info)
                array += info
            }
            close()
        }
        return array
    }

    fun insertNote(info: InfoPrototype){
        try {
            var sql = ""
            val type = when {
                (info is InfoRecord) -> {
                    sql = "INSERT INTO $TABLE_RECORDS ($PARENT_ID, $PATHS) VALUES ($maxId, '${pathsToString(info.paths)}');\n"
                    0
                }
                (info is InfoTask) -> {
                    sql = "INSERT INTO $TABLE_TASKS ($PARENT_ID, $LINKS, $CHECK_TIME, $DATE_DEADLINE) " +
                            "VALUES ($maxId, '${linksToString(info.links)}', " +
                            "'${checkTimeToString(info.checkTimes)}', '${calendarToString(info.deadLine)}');\n"
                    1
                }
                (info is InfoList) -> {
                    for (item in info.groupListItems)
                        sql += "INSERT INTO $TABLE_LISTS ($PARENT_ID, $ACTION, $DESCRIPTION) " +
                                "VALUES ($maxId, '${item.action}', '${item.description}');\n"
                    2
                }
                (info is InfoSchedule) -> {
                    for (item in info.groupScheduleItems)
                        sql += "INSERT INTO $TABLE_SCHEDULES ($PARENT_ID, $ACTION, $DESCRIPTION, $DATE_CREATE)" +
                                "VALUES ($maxId, '${item.action}', '${item.description}', '${calendarToString(item.dateCreate)}');\n"
                    3
                }
                (info is InfoListItem) -> {
                    writableDatabase.execSQL(
                        "INSERT INTO $TABLE_LISTS ($PARENT_ID, $ACTION, $DESCRIPTION)" +
                                " VALUES (${info.parentId}, '${info.action}', '${info.description}');\n"
                    )
                    return
                }
                (info is InfoScheduleItem) -> {
                    writableDatabase.execSQL(
                        "INSERT INTO $TABLE_SCHEDULES ($PARENT_ID, $ACTION, $DESCRIPTION, $DATE_CREATE) " +
                                "VALUES (${info.parentId}, '${info.action}', '${info.description}', " +
                                "'${calendarToString(info.dateCreate)}');"
                    )
                    return
                }
                else -> -1
            }
            maxId++
            info.apply {
                sql += """INSERT INTO $TABLE_NOTES
                    ($TYPE, $ACTION, $DESCRIPTION, $NAME_OF_DEVICE, $IS_IMPORTANT,
                    $IS_IN_TRASH, $DATE_CREATE, $LEVEL_PRIVACY, $PASSWORD)
                    VALUES ($type, '$action', '$description', '$nameDevice', ${if (isImportant) 1 else 0},
                    ${if (isInTrash) 1 else 0}, '${calendarToString(dateCreate)}', $levelPrivacy, '$password');"""
                Toast.makeText(context, sql, Toast.LENGTH_LONG).show()
                writableDatabase.execSQL(sql)
            }
        }
        catch(ex: Exception){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun updateNote(id: Int, info: InfoPrototype){
        var sql = ""
        when {
            (info is InfoRecord) ->
                sql += "UPDATE $TABLE_RECORDS SET $PARENT_ID = $id, $PATHS = '${pathsToString(info.paths)}' WHERE $PARENT_ID = $id;"
            (info is InfoTask) ->
                sql += "UPDATE $TABLE_TASKS SET $PARENT_ID = $id, $LINKS = '${linksToString(info.links)}', " +
                        "$CHECK_TIME = '${checkTimeToString(info.checkTimes)}', " +
                        "$DATE_DEADLINE = '${calendarToString(info.deadLine)}' WHERE $PARENT_ID = $id;"
            (info is InfoList) ->
                for (item in info.groupListItems)
                    sql += "UPDATE $TABLE_LISTS SET $PARENT_ID = $id, $ACTION = '${item.action}'," +
                            " $DESCRIPTION = '${item.description}' WHERE $ID = ${item.id};"
            (info is InfoSchedule) ->
                for (item in info.groupScheduleItems)
                    sql += "UPDATE $TABLE_LISTS SET $PARENT_ID = $id, $ACTION = '${item.action}', $DESCRIPTION = " +
                            "'${item.description}', $DATE_CREATE = '${calendarToString(item.dateCreate)}' WHERE $ID = ${item.id};"
        }
        info.apply {
            sql += """UPDATE $TABLE_NOTES SET 
            $ACTION = '$action', 
            $DESCRIPTION = '$description',
            $NAME_OF_DEVICE = '$nameDevice',
            $IS_IMPORTANT = $isImportant,
            $IS_IN_TRASH = $isInTrash,
            $LEVEL_PRIVACY = $levelPrivacy,
            $PASSWORD = '$password'
             WHERE $ID = $id;
        """.trimMargin()
        }
        writableDatabase.execSQL(sql)
    }
}