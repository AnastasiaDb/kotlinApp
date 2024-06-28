package com.example.appmood.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.appmood.database.MyDataBase.TABLE_NAME
import com.example.appmood.database.MyDataBase._DATE
import com.example.appmood.database.MyDataBase._EMOTIONS
import com.example.appmood.database.MyDataBase._INFORMATION
import com.example.appmood.database.MyDataBase._IS_GIVEN_ADVICE
import com.example.appmood.database.MyDataBase._MOOD
import com.example.appmood.date.DayMood
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyDBManager {
    private val context: Context
    private val myDBHelper:MyDBHelper
    private lateinit var db:SQLiteDatabase

    constructor(context: Context){
        this.context=context
        myDBHelper = MyDBHelper(context)
    }

    fun openDB(){
        db = myDBHelper.writableDatabase
    }

    fun insertToDB_Mood(mood: Int, date: String) {
        val cv = ContentValues()
        cv.put(_MOOD, mood)
        cv.put(MyDataBase._DATE, date)
        db.insert(TABLE_NAME, null, cv)
    }

    fun insertToDBEmotionsAndInform(emotions: String, information: String, date: String) {
        val cv = ContentValues()
        cv.put(MyDataBase._DATE, date)
        cv.put(MyDataBase._MOOD, -1)
        cv.put(_EMOTIONS, emotions)
        cv.put(_INFORMATION, information)
        db.insert(TABLE_NAME, null, cv)
    }

    fun insertToDB_Advice() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDateString = dateFormat.format(Date())
        val cv = ContentValues()
        cv.put(_MOOD, -1)
        cv.put(_IS_GIVEN_ADVICE, 1)
        cv.put(MyDataBase._DATE, todayDateString)
        db.insert(TABLE_NAME, null, cv)
    }

    fun updateToDB_Mood(mood: Int, date: String) {
        val cv = ContentValues()
        cv.put(_MOOD, mood)
        val whereClause = MyDataBase._DATE + "=?"
        val whereArgs = arrayOf(date)
        db.update(TABLE_NAME, cv, whereClause, whereArgs)
    }

    fun updateToDB_EmotionsAndInform(emotions: String, information: String, date: String) {
        val cv = ContentValues()
        cv.put(_EMOTIONS, emotions)
        cv.put(_INFORMATION, information)
        val whereClause = MyDataBase._DATE + "=?"
        val whereArgs = arrayOf(date)
        db.update(TABLE_NAME, cv, whereClause, whereArgs)
    }

    fun updateToDB_Advice() {
        val cv = ContentValues()
        cv.put(_IS_GIVEN_ADVICE, 1) // Устанавливаем флаг совета для сегодняшней даты
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDateString = dateFormat.format(Date())

        // Обновляем запись в базе данных, устанавливая флаг совета для сегодняшней даты
        val whereClause: String = _DATE + "=?"
        val whereArgs = arrayOf(todayDateString)
        db.update(TABLE_NAME, cv, whereClause, whereArgs)
    }

    fun getTodayEmotions(): String? {
        val date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val strDate = formatter.format(date)

        // Параметризированный SQL-запрос для выборки записей за сегодняшний день
        val selectionArgs = arrayOf(strDate)
        val cursor =
            db.rawQuery("SELECT $_EMOTIONS FROM $TABLE_NAME WHERE $_DATE = ?", selectionArgs)
        var emotions: String? = ""

        // Проверяем, есть ли строки в курсоре
        if (cursor.moveToFirst()) {
            // Получаем эмоции из курсора
            emotions = cursor.getString(cursor.getColumnIndexOrThrow(_EMOTIONS))
        }
        cursor.close()
        return emotions
    }

    fun getTodayInformation(): String? {
        val date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val strDate = formatter.format(date)

        // Параметризированный SQL-запрос для выборки записей за сегодняшний день
        val selectionArgs = arrayOf(strDate)
        val cursor =
            db.rawQuery("SELECT $_INFORMATION FROM $TABLE_NAME WHERE $_DATE = ?", selectionArgs)
        var information: String? = ""

        // Проверяем, есть ли строки в курсоре
        if (cursor.moveToFirst()) {
            // Получаем эмоции из курсора
            information = cursor.getString(cursor.getColumnIndexOrThrow(_INFORMATION))
        }
        cursor.close()
        return information
    }

    fun getTodayMood(): Int {
        val date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val strDate = formatter.format(date)

        // Параметризированный SQL-запрос для выборки записей за сегодняшний день
        val selectionArgs = arrayOf(strDate)
        val cursor = db.rawQuery("SELECT $_MOOD FROM $TABLE_NAME WHERE $_DATE = ?", selectionArgs)
        var mood = -1

        // Проверяем, есть ли строки в курсоре
        if (cursor.moveToFirst()) {
            // Получаем эмоции из курсора
            mood = cursor.getInt(cursor.getColumnIndexOrThrow(_MOOD))
        }
        cursor.close()
        return mood
    }

    fun getFromDB(): List<DayMood> {
        val tempList: MutableList<DayMood> = ArrayList()
        val cursor = db.query(
            TABLE_NAME, null, null, null,
            null, null, null
        )
        while (cursor.moveToNext()) {
            val mood = cursor.getInt(cursor.getColumnIndexOrThrow(_MOOD))
            val information = cursor.getString(cursor.getColumnIndexOrThrow(_INFORMATION))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(_DATE))
            val emotions = cursor.getString(cursor.getColumnIndexOrThrow(_EMOTIONS))
            val advice = cursor.getInt(cursor.getColumnIndexOrThrow(_IS_GIVEN_ADVICE))
            val emotionsNotNull = emotions ?: ""
            val informationNotNull = information ?: ""
            tempList.add(DayMood(date, mood, emotionsNotNull, informationNotNull))
        }
        cursor.close()
        return tempList
    }

    fun getFromDB_7(): List<DayMood> {
        val tempList: MutableList<DayMood> = ArrayList()
        val sqlQuery = "SELECT * FROM $TABLE_NAME" +
                " WHERE _date BETWEEN date('now', '-6 days') AND date('now')" +
                " ORDER BY _date"
        val cursor = db.rawQuery(sqlQuery, null)
        while (cursor.moveToNext()) {
            val mood = cursor.getInt(cursor.getColumnIndexOrThrow(_MOOD))
            val information = cursor.getString(cursor.getColumnIndexOrThrow(_INFORMATION))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(_DATE))
            val emotions = cursor.getString(cursor.getColumnIndexOrThrow(_EMOTIONS))
            val emotionsNotNull = emotions ?: ""
            val informationNotNull = information ?: ""
            tempList.add(DayMood(date, mood, emotionsNotNull, informationNotNull))
        }
        cursor.close()
        return tempList
    }

    fun getFromDB_30(): List<DayMood> {
        val tempList: MutableList<DayMood> = ArrayList()

        val sqlQuery = "SELECT * FROM $TABLE_NAME" +
                " WHERE _date BETWEEN date('now', '-29 days') AND date('now')" +
                " ORDER BY _date"
        val cursor = db.rawQuery(sqlQuery, null)
        while (cursor.moveToNext()) {
            val mood = cursor.getInt(cursor.getColumnIndexOrThrow(_MOOD))
            val information = cursor.getString(cursor.getColumnIndexOrThrow(_INFORMATION))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(_DATE))
            val emotions = cursor.getString(cursor.getColumnIndexOrThrow(_EMOTIONS))
            val emotionsNotNull = emotions ?: ""
            val informationNotNull = information ?: ""
            tempList.add(DayMood(date, mood, emotionsNotNull, informationNotNull))
        }
        cursor.close()
        return tempList
    }

    fun hasTodayRecordAndMood(): Boolean {

        // Получаем сегодняшнюю дату в формате, который используется в вашей базе данных
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDateString = dateFormat.format(Date())

        // Параметризированный SQL-запрос с условием на дату и непустое значение в поле _MOOD
        val selectionArgs = arrayOf(todayDateString)
        val cursor = db.rawQuery(
            ((((("SELECT * FROM $TABLE_NAME").toString() + " WHERE " + _DATE).toString() + " = ? AND " + _MOOD).toString() + " IS NOT NULL AND "
                    + _MOOD).toString() + "!= ''" + " AND " + _MOOD).toString() + "!=-1",
            selectionArgs
        )
        val hasNonEmptyMood = cursor.count > 0
        cursor.close()
        return hasNonEmptyMood
    }

    fun hasTodayRecordAndEmotion(): Boolean {

        // Получаем сегодняшнюю дату в формате, который используется в вашей базе данных
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDateString = dateFormat.format(Date())

        // Параметризированный SQL-запрос с условием на дату и непустое значение в поле _MOOD
        val selectionArgs = arrayOf(todayDateString)
        val cursor = db.rawQuery(
            ((((("SELECT * FROM $TABLE_NAME").toString() + " WHERE " + _DATE).toString() + " = ? AND " + _EMOTIONS).toString() + " IS NOT NULL AND "
                    + _EMOTIONS).toString() + " != ''"), selectionArgs
        )
        val hasNonEmptyEmo = cursor.count > 0
        cursor.close()
        return hasNonEmptyEmo
    }

    fun hasTodayRecordAndInformation(): Boolean {

        // Получаем сегодняшнюю дату в формате, который используется в вашей базе данных
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDateString = dateFormat.format(Date())

        // Параметризированный SQL-запрос с условием на дату и непустое значение в поле _MOOD
        val selectionArgs = arrayOf(todayDateString)
        val cursor = db.rawQuery(
            ((((("SELECT * FROM $TABLE_NAME").toString() + " WHERE " + _DATE) + " = ? AND " + _INFORMATION) + " IS NOT NULL AND "
                    + _INFORMATION).toString() + " != ''"), selectionArgs
        )
        val hasNonEmptyEmo = cursor.count > 0
        cursor.close()
        return hasNonEmptyEmo
    }

    fun hasTodayRecord(): Boolean {

        // Получаем сегодняшнюю дату в формате, который используется в вашей базе данных
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDateString = dateFormat.format(Date())

        // Параметризированный SQL-запрос с условием на дату и непустое значение в поле _MOOD
        val selectionArgs = arrayOf(todayDateString)
        val cursor = db.rawQuery(
            (("SELECT * FROM $TABLE_NAME").toString() + " WHERE " + _DATE) + " = ? ",
            selectionArgs
        )
        val hasNonEmptyEmo = cursor.count > 0
        cursor.close()
        return hasNonEmptyEmo
    }


    fun closeDB() {
        myDBHelper.close()
    }


    fun hasAdviceRecordToday(): Boolean {
        // Получаем сегодняшнюю дату в формате, который используется в вашей базе данных
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDateString = dateFormat.format(Date())
        val selectionArgs = arrayOf(todayDateString)
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $_DATE = ? AND $_IS_GIVEN_ADVICE = 1",
            selectionArgs
        )
        val isAdviceGiven = cursor.count > 0
        cursor.close()
        return isAdviceGiven
    }


}