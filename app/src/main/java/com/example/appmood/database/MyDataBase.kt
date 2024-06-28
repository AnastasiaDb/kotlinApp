package com.example.appmood.database

object MyDataBase {

    val TABLE_NAME = "Mood"
    val _ID = "_id"
    val _MOOD = "_mood"
    val _DATE = "_date"

    val _EMOTIONS = "_emotions"

    val _INFORMATION = "_information"

    val _IS_GIVEN_ADVICE = "_is_given_advice"

    val DB_NAME = "mood_db.db"
    val DB_VERSION = 1

    val CREATE_TABLE: String = ("CREATE TABLE IF NOT EXISTS $TABLE_NAME ($_ID INTEGER PRIMARY KEY, $_MOOD INTEGER," +
            "$_DATE VARCHAR(100), $_EMOTIONS VARCHAR(100), $_INFORMATION VARCHAR(300), $_IS_GIVEN_ADVICE INTEGER DEFAULT 0)")

    val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"

}