package mytestprogram.models

const val NAME_TABLE = "data.db"
const val TABLE_CONTAINERS = "containers"
const val TABLE_ITEMS = "items"
const val VERSION = 2
const val ID = "id"
const val PARENT_ID = "parentId"
const val ACTION = "action"
const val DESCRIPTION = "description"
const val LINKS = "links"
const val DATE_CREATE = "dateCreate"
const val DATE_DEADLINE = "dateDeadLine"
const val PATHS = "paths"
const val LEVEL_PRIVACY = "levelPrivacy"
const val PASSWORD = "password"
const val NAME_OF_DEVICE = "nameOfDevice"
const val TIMES = "times"
const val IS_IMPORTANT = "isImportant"
const val IS_IN_TRASH = "isInTrash"
const val ITEMS = "items"

const val CREATE_TABLE_CONTAINERS = """
    CREATE TABLE IF NOT EXISTS $TABLE_CONTAINERS (
        $ID INTEGER PRIMARY KEY AUTOINCREMENT,
        $ACTION TEXT,
        $DESCRIPTION TEXT,
        $NAME_OF_DEVICE VARCHAR(50),
        $IS_IMPORTANT INTEGER,
        $IS_IN_TRASH INTEGER,
        $DATE_CREATE VARCHAR(50),
        $LEVEL_PRIVACY INTEGER,
        $PASSWORD VARCHAR(50),
        $DATE_DEADLINE VARCHAR(50),
        $LINKS TEXT,
        $PATHS TEXT,
        $ITEMS TEXT,
        $TIMES TEXT
    );
"""
const val CREATE_TABLE_ITEMS = """
    CREATE TABLE IF NOT EXISTS $TABLE_ITEMS (
        $ID INTEGER PRIMARY KEY AUTOINCREMENT,
        $PARENT_ID INTEGER,
        $ACTION TEXT,
        $DESCRIPTION TEXT,
        $DATE_CREATE VARCHAR(50)
    );
"""