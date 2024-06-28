package com.example.appmood.recyclerSetting

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.appmood.R
import com.example.appmood.date.DayMood

class MyAdapter : RecyclerView.Adapter<MyViewHolder> {
    private var context: Context
    private var items: List<DayMood>


    constructor(context: Context, items: List<DayMood>) {

        this.context = context
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_for_notes, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = items[position].getEmotions()
        holder.emotion.setOnClickListener { //String note = items.get(position).getEmotions();
            val noteText = parseEmotion(note)
            showDialog(noteText)
        }
        holder.date.text = items[position].getCalendar()
        holder.note.text = items[position].getNotes()
        holder.mood.text = java.lang.String.valueOf(items[position].getMood())
        var mood = items[position].getMood()
        when (mood) {
            -1 -> {
                holder.mood.text = "-"
                holder.mood.setBackgroundColor(ContextCompat.getColor(context, R.color.gray))
            }
            0 -> holder.mood.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.mood_0
                )
            )
            1 -> holder.mood.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.mood_1
                )
            )
            2 -> holder.mood.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.mood_2
                )
            )
            3 -> holder.mood.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.mood_3
                )
            )
            4 -> holder.mood.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.mood_4
                )
            )
            5 -> holder.mood.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.mood_5
                )
            )
            6 -> holder.mood.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.mood_6
                )
            )
            7 -> holder.mood.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.mood_7
                )
            )
            8 -> holder.mood.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.mood_8
                )
            )
            9 -> holder.mood.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.mood_9
                )
            )
            10 -> holder.mood.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.mood_10
                )
            )
        }
    }

    // Метод для отображения всплывающего окна с текстом заметки
    private fun showDialog(noteText: String?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Эмоции")
            .setPositiveButton(
                "OK"
            ) { dialogInterface, _ -> dialogInterface.dismiss() }
        if (noteText == null || noteText == "")
            builder.setMessage("Вы не выбрали эмоции")
        else
            builder.setMessage(noteText)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun parseEmotion(noteText: String?): String? {
        return if (noteText != null) {
            val res = StringBuilder()
            var i = 1
            val emotion_arr = noteText.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (x in emotion_arr) {
                if (x == "reverie") {
                    res.append(i).append(".Задумчивость\n")
                    i++
                }
                if (x == "happy") {
                    res.append(i).append(".Веселье\n")
                    i++
                }
                if (x == "sad") {
                    res.append(i).append(".Грусть\n")
                    i++
                }
                if (x == "shy") {
                    res.append(i).append(".Смущение\n")
                    i++
                }
                if (x == "confidence") {
                    res.append(i).append(".Уверенность\n")
                    i++
                }
                if (x == "angry") {
                    res.append(i).append(".Злость\n")
                    i++
                }
            }
            res.toString()
        } else null
    }

}