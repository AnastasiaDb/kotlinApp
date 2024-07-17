package com.example.appmood.recyclerSetting

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appmood.R

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val date: TextView = itemView.findViewById(R.id.date)
    val note: TextView = itemView.findViewById(R.id.note)
    val mood: TextView = itemView.findViewById(R.id.mood)
    var emotion: Button = itemView.findViewById(R.id.emotion)

}