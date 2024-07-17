package com.example.appmood.date

class DayMood (private val advice: Int,
               private val calendar: String?,
               private var mood: Int,
               private var emotions: String?,
               private var notes: String?){
    //constructor(calendar: String, mood: Int):this (0,calendar,mood,null,null)

    constructor(calendar: String, mood: Int, emotions: String, notes: String):this(0,calendar,mood,emotions,notes)


    fun getMood(): Int = mood

    fun getCalendar(): String? = calendar

    fun getEmotions(): String? = emotions

    fun getNotes(): String? = notes

    fun getAdvice(): Int = advice
}