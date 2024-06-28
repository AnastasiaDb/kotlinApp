package com.example.appmood

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.appmood.database.MyDBManager
import com.example.appmood.date.DayMood
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class MainActivityGraph : AppCompatActivity() {
    private val myDBManager = MyDBManager(this)
    private lateinit var values: List<DayMood>
    private lateinit var chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_graph)

        chart = findViewById(R.id.chart)
        myDBManager.openDB()
        setupEmptyChart()
    }

    override fun onResume() {
        super.onResume()
        findViewById<View>(R.id.seven_days).setOnClickListener {
            values = myDBManager.getFromDB_7()
            fillChartWithData(7)
        }
        findViewById<View>(R.id.thirty_days).setOnClickListener {
            values = myDBManager.getFromDB_30()!!
            fillChartWithData(30)
        }
    }

    private fun fillChartWithData(maxX: Int) {
        val entries = ArrayList<Entry>()

        val datesToCheck = generateDatesToCheck(maxX)
        var index = 0
        var nullIndex = 0
        for (date in datesToCheck) {
            // Проверяем, есть ли текущая дата в списке данных
            if (index < values.size && values[index].getCalendar().equals(date)) {
                val mood = values[index]
                if (mood.getMood() !== -1 && mood.getCalendar() != null) {
                    entries.add(Entry((nullIndex + 1).toFloat(), mood.getMood().toFloat()))
                }
                index++
            }
            nullIndex++
        }


        // Получаем набор данных графика и обновляем его
        var data = chart.data
        val dataset = data.getDataSetByIndex(0) as LineDataSet
        dataset.values = entries


        // цвет
        dataset.color = Color.rgb(199, 21, 133)
        dataset.circleRadius = 5f
        //dataset.setCircleColor(Color.BLACK);
        // График будет плавным
        dataset.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

        // График будет заполненным
        dataset.setDrawFilled(true)

        // График будет анимироваться 0.5 секунды
        chart.animateY(500)

        // Настройка оси x
        val xAxis = chart.xAxis
        xAxis.axisMinimum = 1f
        xAxis.axisMaximum = maxX.toFloat()
        xAxis.setDrawGridLines(false)

        // Создадим переменную данных для графика
        data = LineData(dataset)
        // Передадим данные для графика в сам график
        chart.data = data
        chart.invalidate()
    }

    private fun setupEmptyChart() {
        val entries = ArrayList<Entry>()

        val dataset = LineDataSet(entries, "График настроения")

        dataset.color = Color.rgb(199, 21, 133)

        // График будет заполненным
        dataset.setDrawFilled(true)

        val yAxis = chart.axisLeft
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 10f
        yAxis.setDrawGridLines(false)

        val data = LineData(dataset)
        chart.data = data

        chart.invalidate()
    }

    private fun generateDatesToCheck(maxX: Int): ArrayList<String> {
        val dates = ArrayList<String>()
        // Получаем текущую дату
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        // Добавляем даты за последние maxX дней в список
        for (i in maxX - 1 downTo 0) {
            val calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            dates.add(dateFormat.format(calendar.time))
        }
        return dates
    }

     fun newMain(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}