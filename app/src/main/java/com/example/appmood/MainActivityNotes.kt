package com.example.appmood

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appmood.database.MyDBManager
import com.example.appmood.date.DayMood
import com.example.appmood.recyclerSetting.MyAdapter

class MainActivityNotes : AppCompatActivity() {
    private var myDBManager = MyDBManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_notes)

        myDBManager.openDB()

        val items: List<DayMood> = myDBManager.getFromDB()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdapter(this, items)
    }

    fun newMain(view: View?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}