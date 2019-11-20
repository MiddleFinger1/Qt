package mytestprogram.models

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import kotlin.collections.ArrayList

class DBModel(private val context: Context): SQLiteOpenHelper(context, NAME_TABLE, null, VERSION){

    lateinit var mainGroup: Group
    private var maxId = 0

    override fun onCreate(database: SQLiteDatabase?) {
        try {
            database!!.execSQL(CREATE_TABLE_CONTAINERS)
            database.execSQL(CREATE_TABLE_ITEMS)
            Toast.makeText(context, "${database.isOpen}", Toast.LENGTH_LONG).show()
            maxId = findMaxId()
        }
        catch (ex: Exception) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        database!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTAINERS")
        database.execSQL("DROP TABLE IF EXISTS $TABLE_ITEMS")
        // настроить работу обновления базы с учетом сохранения существующих данных
        onCreate(database)
    }

    private fun findMaxId(): Int {
        val resultSet = readableDatabase.rawQuery("SELECT * FROM $TABLE_CONTAINERS ORDER BY $ID DESC LIMIT 1;", null)
        while (resultSet.moveToNext()) {
            val id = resultSet.getInt(resultSet.getColumnIndex(ID))
            resultSet.close()
            return id
        }
        return -1
    }

    fun insertContainer(container: Container){
        try {
            maxId++
            container.apply {
                for (item in items)
                    insertItem(item)
                ContentValues().apply {
                    put(ACTION, action)
                    put(DESCRIPTION, description)
                    put(NAME_OF_DEVICE, nameDevice)
                    put(IS_IMPORTANT, isImportant)
                    put(IS_IN_TRASH, isInTrash)
                    put(DATE_CREATE, calendarToString(dateCreate))
                    put(LEVEL_PRIVACY, privacy)
                    put(PASSWORD, password)
                    writableDatabase.insert(TABLE_CONTAINERS, null, this)
                }
            }
        }
        catch(ex: Exception){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun insertItem(item: Item){
        try{
            val sql: String
            item.apply {
                sql = "INSERT INTO $TABLE_ITEMS ($PARENT_ID, $ACTION, $DESCRIPTION, $DATE_CREATE)" +
                        "VALUES ($parentId, '$action', '$description', '${calendarToString(dateCreate)}');"
            }
            writableDatabase.execSQL(sql)
        }
        catch (ex: Exception){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun createGroup(){
        mainGroup = Group.createGroup(select()) {
            true
        }
    }

    fun updateContainer(id: Int, container: Container){
        var sql = ""
        for (item in container.items)
            sql += "UPDATE $TABLE_ITEMS SET $PARENT_ID = $id, $ACTION = '${item.action}', $DESCRIPTION = " +
                    "'${item.description}', $DATE_CREATE = '${calendarToString(item.dateCreate)}' WHERE $ID = ${item.id};"
        container.apply {
            sql += """UPDATE $TABLE_CONTAINERS SET 
            $ACTION = '$action', 
            $DESCRIPTION = '$description',
            $NAME_OF_DEVICE = '$nameDevice',
            $IS_IMPORTANT = $isImportant,
            $IS_IN_TRASH = $isInTrash,
            $LEVEL_PRIVACY = $privacy,
            $PASSWORD = '$password',
            $DATE_DEADLINE = '${calendarToString(deadLine)}',
            $LINKS = '${linksToString(links)}',
            $PATHS = '${pathsToString(paths)}',
            $TIMES = '${checkTimeToString(times)}'
             WHERE $ID = $id;
        """.trimMargin()
        }
        Toast.makeText(context, sql, Toast.LENGTH_LONG).show()
        writableDatabase.execSQL(sql)
    }

    fun updateItem(id: Int, item: Item){
        val sql: String
        item.apply {
            sql = """UPDATE $TABLE_CONTAINERS SET 
            $ACTION = '$action', 
            $DESCRIPTION = '$description'
            $DATE_CREATE = '${calendarToString(dateCreate)}'
             WHERE $ID = $id;
        """.trimMargin()
        }
        Toast.makeText(context, sql, Toast.LENGTH_LONG).show()
        writableDatabase.execSQL(sql)
    }

    fun deleteNote(id: Int, childId: Int = -1){
        val sql =
            if (childId >= 0)
                "DELETE FROM $TABLE_ITEMS WHERE $PARENT_ID = $id AND $ID = $childId;"
            else
                "DELETE FROM $TABLE_CONTAINERS WHERE $ID = $id;\nDELETE FROM $TABLE_ITEMS WHERE $PARENT_ID = $id;"
        Toast.makeText(context, sql, Toast.LENGTH_LONG).show()
        writableDatabase.execSQL(sql)
    }

    private fun select(): ArrayList<Container> {
        val array = arrayListOf<Container>()
        readableDatabase.rawQuery("SELECT * FROM $TABLE_CONTAINERS;", null).apply {
            while (moveToNext())
                array += Container(
                    getInt(getColumnIndex(ID)),
                    getString(getColumnIndex(ACTION)),
                    getString(getColumnIndex(DESCRIPTION)),
                    getString(getColumnIndex(NAME_OF_DEVICE)),
                    getInt(getColumnIndex(IS_IMPORTANT)) == 1,
                    getInt(getColumnIndex(IS_IN_TRASH)) == 1,
                    stringToCalendar(getString(getColumnIndex(DATE_CREATE))),
                    getInt(getColumnIndex(LEVEL_PRIVACY)),
                    getString(getColumnIndex(PASSWORD))
                )
            close()
        }
        readableDatabase.rawQuery("SELECT * FROM $TABLE_ITEMS;", null).apply {
            while (moveToNext())
                array[getInt(getColumnIndex(PARENT_ID)) - 1].items += Item(
                    getInt(getColumnIndex(ID)),
                    getInt(getColumnIndex(PARENT_ID)),
                    getString(getColumnIndex(ACTION)),
                    getString(getColumnIndex(DESCRIPTION)),
                    stringToCalendar(getString(getColumnIndex(DATE_CREATE)))
                )
            close()
        }
        return array
    }
}