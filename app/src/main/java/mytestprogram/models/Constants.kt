package mytestprogram.models

const val RECORD_TYPE = 0
const val VERSION = 1
const val TASK_TYPE = 1
const val LIST_TYPE = 2
const val SCHEDULE_TYPE = 3
const val NAME_TABLE = "data.db"
const val TABLE_NOTES = "notes"
const val TABLE_RECORDS = "records"
const val TABLE_TASKS = "tasks"
const val TABLE_LISTS = "lists"
const val TABLE_SCHEDULES = "schedules"
const val ID = "id"
const val PARENT_ID = "parentId"
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

const val CREATE_TABLES = """
    CREATE TABLE IF NOT EXISTS $TABLE_NOTES (
                    $ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $TYPE INTEGER,
                    $ACTION TEXT,
                    $DESCRIPTION TEXT,
                    $NAME_OF_DEVICE VARCHAR(50),
                    $IS_IMPORTANT INTEGER,
                    $IS_IN_TRASH INTEGER,
                    $DATE_CREATE VARCHAR(50),
                    $LEVEL_PRIVACY INTEGER,
                    $PASSWORD VARCHAR(50)
    );
    CREATE TABLE IF NOT EXISTS $TABLE_RECORDS (
                    $ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $PARENT_ID INTEGER,
                    $PATHS TEXT
    );
    CREATE TABLE IF NOT EXISTS $TABLE_TASKS (
                    $ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $PARENT_ID INTEGER,
                    $LINKS TEXT,
                    $CHECK_TIME TEXT,
                    $DATE_DEADLINE VARCHAR(50)
    );
    CREATE TABLE IF NOT EXISTS $TABLE_LISTS (
                    $ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $PARENT_ID INTEGER,
                    $ACTION TEXT,
                    $DESCRIPTION TEXT
    );
    CREATE TABLE IF NOT EXISTS $TABLE_SCHEDULES (
                    $ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $PARENT_ID INTEGER,
                    $ACTION TEXT,
                    $DESCRIPTION TEXT,
                    $DATE_CREATE VARCHAR(50)
    );
"""