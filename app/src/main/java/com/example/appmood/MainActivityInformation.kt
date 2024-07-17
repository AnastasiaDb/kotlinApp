package com.example.appmood

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.appmood.database.MyDBManager
import java.text.SimpleDateFormat
import java.util.Date

class MainActivityInformation : AppCompatActivity() {

    private val myDBManager: MyDBManager = MyDBManager(this)
    var set = arrayOf(false, false, false, false, false, false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_information)
        myDBManager.openDB()
    }

    override fun onResume() {
        super.onResume()
        //   val set = arrayOf(false, false, false, false, false, false)
        val happyButton = findViewById<Button>(R.id.happy)
        val sadButton = findViewById<Button>(R.id.sad)
        val shyButton = findViewById<Button>(R.id.shy)
        val reverieButton = findViewById<Button>(R.id.reverie)
        val angryButton = findViewById<Button>(R.id.angry)
        val confidenceButton = findViewById<Button>(R.id.confidence)
        val editText = findViewById<EditText>(R.id.editTextEmotion)
        if (myDBManager.hasTodayRecordAndInformation()) {
            val text = myDBManager.getTodayInformation()
            editText.setText(text)
        }
        if (myDBManager.hasTodayRecordAndEmotion()) {
            val emotionsToday = myDBManager.getTodayEmotions()
            val emotion_arr = emotionsToday?.split("\\s+".toRegex())?.dropLastWhile { it.isEmpty() }
                ?.toTypedArray()
            if (emotion_arr != null) {
                for (x in emotion_arr) {
                    when (x) {
                        "reverie" -> {
                            reverieButton.setBackgroundResource(R.drawable.feelings_button_background_choosed)
                            set[0] = true
                        }

                        "happy" -> {
                            happyButton.setBackgroundResource(R.drawable.feelings_button_background_choosed)
                            set[1] = true
                        }

                        "sad" -> {
                            sadButton.setBackgroundResource(R.drawable.feelings_button_background_choosed)
                            set[2] = true
                        }

                        "shy" -> {
                            shyButton.setBackgroundResource(R.drawable.feelings_button_background_choosed)
                            set[3] = true
                        }

                        "confidence" -> {
                            confidenceButton.setBackgroundResource(R.drawable.feelings_button_background_choosed)
                            set[4] = true
                        }

                        "angry" -> {
                            angryButton.setBackgroundResource(R.drawable.feelings_button_background_choosed)
                            set[5] = true
                        }
                    }
                }
            }
        }
        val okButton = findViewById<Button>(R.id.ok)


        setColorButton(reverieButton, 0)
        setColorButton(happyButton, 1)
        setColorButton(sadButton, 2)
        setColorButton(shyButton, 3)
        setColorButton(confidenceButton, 4)
        setColorButton(angryButton, 5)


        val emotions = StringBuilder()
        okButton.setOnClickListener { view ->
            if (set[0]) emotions.append(" reverie")
            if (set[1]) emotions.append(" happy")
            if (set[2]) emotions.append(" sad")
            if (set[3]) emotions.append(" shy")
            if (set[4]) emotions.append(" confidence")
            if (set[5]) emotions.append(" angry")
            val date = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val strDate = formatter.format(date)
            val res = emotions.toString()
            val information = editText.text.toString()
            if (myDBManager.hasTodayRecord()) myDBManager.updateToDB_EmotionsAndInform(
                res,
                information,
                strDate
            ) else myDBManager.insertToDBEmotionsAndInform(res, information, strDate)
            mainActivity(view)
        }
    }

    fun mainActivity(view: View?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun setColorButton(button: Button, index: Int) {

        button.setOnClickListener {
            if (!set[index]) {
                // Устанавливаем цвет фона кнопки в зависимости от ее состояния
                set[index] = true
                button.setBackgroundResource(R.drawable.feelings_button_background_choosed)
            } else {
                set[index] = false
                button.setBackgroundResource(R.drawable.feelings_button_background)
            }
        }
    }
}