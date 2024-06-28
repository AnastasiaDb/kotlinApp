package com.example.appmood.database

import android.content.Context
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHelper(context: Context) :
    SQLiteOpenHelper(context, MyDataBase.DB_NAME, null, MyDataBase.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(MyDataBase.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(MyDataBase.DROP_TABLE)
        onCreate(db)
    }

}