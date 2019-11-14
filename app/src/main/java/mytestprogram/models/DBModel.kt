package mytestprogram.models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.util.*
import kotlin.collections.ArrayList

class DBModel(private val context: Context)
    : SQLiteOpenHelper(context, NAME_TABLE, null, VERSION
){

    val mainGroup = MainGroup()

    companion object {
        const val NAME_TABLE = "data.db"
        const val TABLE_NOTES = "notes"
        const val VERSION = 1
        const val ID = "id"
        const val TYPE = "type"
        const val ACTION = "action"
        const val DESCRIPTION = "description"
        const val LINKS = "links"
        const val DATE_CREATE = "dateCreate"
        const val DATE_DEADLINE = "dateDeadLine"
        const val PATHS = "paths"
        const val LEVEL_PRIVACY = "levelPrivacy"
        const val PASSWORD = "password"
        const val NAME_OF_DEVICE = "nameOfDevice"
        const val CHECK_TIME = "checkTimes"
        const val IS_IMPORTANT = "isImportant"
        const val IS_IN_TRASH = "isInTrash"
    }

    override fun onCreate(database: SQLiteDatabase?) {
        try {
            database!!.execSQL("""
                CREATE TABLE IF NOT EXISTS $TABLE_NOTES
                (
                    $ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $TYPE INTEGER,
                    $LEVEL_PRIVACY INTEGER,
                    $IS_IMPORTANT INTEGER,
                    $ACTION TEXT,
                    $DESCRIPTION TEXT,
                    $NAME_OF_DEVICE VARCHAR(50),
                    $PASSWORD VARCHAR(50),
                    $DATE_CREATE VARCHAR(50),
                    $DATE_DEADLINE VARCHAR(50),
                    $CHECK_TIME VARCHAR(50),
                    $LINKS VARCHAR(50),
                    $PATHS VARCHAR(50),
                    $IS_IN_TRASH INTEGER
                );
            """.trimIndent())
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

    fun selectNotes() {
        readableDatabase.rawQuery("SELECT * FROM $TABLE_NOTES", null).apply {
            while(moveToNext())
                mainGroup.addNote(InfoPrototype(
                    id = getInt(getColumnIndex(ACTION)),
                    type = intToType(getInt(getColumnIndex(TYPE))),
                    action = getString(getColumnIndex(ACTION)),
                    description = getString(getColumnIndex(DESCRIPTION)),
                    links = stringToLinks(getString(getColumnIndex(LINKS))),
                    dateCreate = stringToCalendar(getString(getColumnIndex(DATE_CREATE))),
                    dateDeadLine = stringToCalendar(getString(getColumnIndex(DATE_DEADLINE))),
                    paths = stringToPaths(getString(getColumnIndex(PATHS))),
                    isImportant = getInt(getColumnIndex(IS_IMPORTANT)) == 1,
                    password = getString(getColumnIndex(PASSWORD)),
                    nameOfDevice = getString(getColumnIndex(NAME_OF_DEVICE)),
                    levelPrivacy = intToPrivacy(getInt(getColumnIndex(LEVEL_PRIVACY))),
                    checkTimes = stringToCheckTime(getString(getColumnIndex(CHECK_TIME))),
                    isInTrash = getInt(getColumnIndex(IS_IN_TRASH)) == 1
                    ))
            close()
        }
    }

    fun createNote(info: InfoPrototype){
        readableDatabase.beginTransaction()
        try {
            val sql: String
            info.apply {
                sql = "INSERT INTO " +
                        "$TABLE_NOTES ($TYPE, $LEVEL_PRIVACY, $IS_IMPORTANT, $ACTION, $DESCRIPTION, $NAME_OF_DEVICE, " +
                        " $PASSWORD, $DATE_CREATE, $DATE_DEADLINE, $CHECK_TIME, $LINKS, $PATHS, $IS_IN_TRASH)" +
                        " VALUES (${typeToInt(type)}, " +
                        "${privacyToInt(levelPrivacy)}, " +
                        "${if (isImportant) 1 else 0}, " +
                        "'$action', '$description', '$nameOfDevice', '$password', " +
                        "'${calendarToString(dateCreate)}', " +
                        "'${if (dateDeadLine is Calendar) calendarToString(dateDeadLine) else ""}', " +
                        "'${if (checkTimes is ArrayList<Calendar>) checkTimeToString(checkTimes) else ""}', " +
                        "'${if (links is ArrayList<Int>) linksToString(links) else ""}', " +
                        "'${if (paths is ArrayList<String>) pathsToString(paths) else ""}', " +
                        "${if (isInTrash) 1 else 0}" +
                        ");"
            }
            mainGroup.addNote(info)
            Toast.makeText(context, sql, Toast.LENGTH_SHORT).show()
            readableDatabase.execSQL(sql)
            readableDatabase.setTransactionSuccessful()
        }
        catch (ex: Exception){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
            readableDatabase.endTransaction()
        }
    }

    fun updateNote(id: Int, info: InfoPrototype){
        val sql: String
        readableDatabase.beginTransaction()
        try {
            info.apply {
                sql = "UPDATE $TABLE_NOTES SET " +
                        "$LEVEL_PRIVACY = ${levelPrivacy}, " +
                        "$IS_IMPORTANT = ${isImportant}, " +
                        "$ACTION = '${action}', " +
                        "$DESCRIPTION = '${description}', " +
                        "$NAME_OF_DEVICE = '${nameOfDevice}, '" +
                        "$PASSWORD = '${password}, '" +
                        "$DATE_CREATE = '${calendarToString(dateCreate)}, '" +
                        "$DATE_DEADLINE = '${if (dateDeadLine is Calendar)
                            calendarToString(dateDeadLine) else ""}', " +
                        "$CHECK_TIME = '${if (checkTimes is ArrayList<Calendar>)
                            checkTimeToString(checkTimes) else ""}', " +
                        "$LINKS = '${if (links is ArrayList<Int>)
                            linksToString(links) else ""}', " +
                        "$PATHS = '${if (paths is ArrayList<String>)
                            pathsToString(paths) else ""}'," +
                        "$IS_IN_TRASH = ${if (isInTrash) 1 else 0}" +
                        "WHERE $ID = $id;"
            }
            Toast.makeText(context, sql, Toast.LENGTH_LONG).show()
            readableDatabase.execSQL(sql)
            readableDatabase.setTransactionSuccessful()
        }
        catch (ex: Exception){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
            readableDatabase.endTransaction()
        }
    }
}