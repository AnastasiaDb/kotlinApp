package com.example.appmood

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.appmood.database.MyDBManager
import com.example.appmood.date.DayMood
import com.example.appmood.parser.ParserFile
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {
    private var myDBManager: MyDBManager = MyDBManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // myDBManager = MyDBManager(this)
        myDBManager.openDB()

//        val values: List<DayMood>? = myDBManager.getFromDB()
//        for (i in values!!.indices) {
//            Log.d(
//                "HI!!!!!", "mood: " + values[i].getMood() + ", day: " + values[i].getCalendar() +
//                        ",emotion " + values[i].getEmotions() + ",information " + values[i].getNotes() + ",advice " + values[i].getAdvice()
//            )
//        }
    }


    override fun onResume() {
        super.onResume()
        findViewById<View>(R.id.change).visibility = View.INVISIBLE

        if (myDBManager.hasTodayRecordAndMood())
            hideEverything()
        if (myDBManager.hasAdviceRecordToday())
            findViewById<View>(R.id.advice).visibility = View.INVISIBLE


        findViewById<View>(R.id.advice).setOnClickListener {
            showAlertDialog()
            if (myDBManager.hasTodayRecord())
                myDBManager.updateToDB_Advice()
            else myDBManager.insertToDB_Advice()
            findViewById<View>(R.id.advice).visibility = View.INVISIBLE
        }

        findViewById<View>(R.id.change).setOnClickListener { showEverything() }
        findViewById<View>(R.id.accept).setOnClickListener {
            val value = (findViewById<View>(R.id.mood_scale) as SeekBar).progress
            val date = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val strDate = formatter.format(date)
            if (myDBManager.hasTodayRecord()) myDBManager.updateToDB_Mood(
                value,
                strDate
            ) else myDBManager.insertToDB_Mood(value, strDate)
            hideEverything()
        }
    }

    private fun hideEverything() {
        findViewById<View>(R.id.change).visibility = View.VISIBLE
        findViewById<View>(R.id.accept).visibility = View.INVISIBLE
        findViewById<View>(R.id.mood_scale).visibility = View.INVISIBLE
        findViewById<View>(R.id.imageView).visibility = View.INVISIBLE
        findViewById<View>(R.id.imageView2).visibility = View.INVISIBLE
        (findViewById<View>(R.id.textView) as TextView).setText(R.string.alreadyChoose)
    }

    private fun showEverything() {
        val mood = myDBManager.getTodayMood()
        findViewById<View>(R.id.change).visibility = View.INVISIBLE
        findViewById<View>(R.id.accept).visibility = View.VISIBLE
        findViewById<View>(R.id.mood_scale).visibility = View.VISIBLE
        (findViewById<View>(R.id.mood_scale) as SeekBar).progress = mood
        findViewById<View>(R.id.imageView).visibility = View.VISIBLE
        findViewById<View>(R.id.imageView2).visibility = View.VISIBLE
        (findViewById<View>(R.id.textView) as TextView).setText(R.string.mood)
    }

    private fun showAlertDialog() {
        val randomLine: String? = ParserFile.getRandomLineFromRaw(this, R.raw.advice)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Совет дня").setMessage(randomLine)

        builder.setPositiveButton(
            "OK"
        ) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        // Создание и отображение диалогового окна
        val dialog = builder.create()
        dialog.show()
    }

    fun newMain(view: View) {
        val intent = Intent(this, MainActivityGraph::class.java)
        startActivity(intent)
    }

    fun newMain_Inform(view: View) {
        val intent = Intent(this, MainActivityInformation::class.java)
        startActivity(intent)
    }

    fun newMain_AllNotes(view: View) {
        val intent = Intent(this, MainActivityNotes::class.java)
        startActivity(intent)
    }
}