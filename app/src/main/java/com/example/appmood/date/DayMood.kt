package com.example.appmood.date

class DayMood {
    private val advice: Int
    private val calendar: String?
    private var mood: Int
    private var emotions: String?
    private var notes: String?

    constructor(calendar: String, mood: Int) {
        this.calendar = calendar
        this.mood = mood
        emotions = null
        notes = null
        advice = 0
    }

    constructor(calendar: String, mood: Int, emotions: String, notes: String, advice: Int) {
        this.calendar = calendar
        this.mood = mood
        this.emotions = emotions
        this.notes = notes
        this.advice = advice
    }

    constructor(calendar: String, mood: Int, emotions: String, notes: String) {
        this.calendar = calendar
        this.mood = mood
        this.emotions = emotions
        this.notes = notes
        advice = 0
    }

    constructor(calendar: String, emotions: String) {
        this.calendar = calendar
        mood = 0
        this.emotions = emotions
        notes = null
        advice = 0

    }

    fun getMood(): Int = mood

    fun getCalendar(): String? = calendar

    fun getEmotions(): String? = emotions

    fun getNotes(): String? = notes

    fun getAdvice(): Int = advice
}