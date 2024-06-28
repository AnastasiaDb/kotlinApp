package com.example.appmood.parser

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.Random

object ParserFile {

    fun getRandomLineFromRaw(context: Context, resourceId: Int): String? {
        val lines = readRawFile(context, resourceId)
        return if (lines.isNotEmpty()) {
            val random = Random()
            val randomIndex = random.nextInt(lines.size)
            lines[randomIndex]
        } else {
            null
        }
    }

    private fun readRawFile(context: Context, resourceId: Int): List<String> {
        val lines: MutableList<String> = ArrayList()
        val inputStream = context.resources.openRawResource(resourceId)

        try {
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    line?.let { lines.add(it) }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return lines
    }
}