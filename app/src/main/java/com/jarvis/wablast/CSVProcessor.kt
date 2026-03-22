package com.jarvis.wablast

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.random.Random

object CSVProcessor {
    private val queue = mutableListOf<Pair<String, String>>()
    var currentIndex = 0

    fun load(context: Context) {
        queue.clear()
        val prefs = context.getSharedPreferences("jarvis_prefs", Context.MODE_PRIVATE)
        currentIndex = prefs.getInt("last_index", 0)

        val reader = BufferedReader(InputStreamReader(context.assets.open("data.csv")))
        reader.forEachLine { line ->
            val parts = line.split(",", limit = 2)
            if (parts.size >= 2) queue.add(parts[0].trim() to parts[1].trim())
        }
        reader.close()
    }

    fun next(context: Context): Pair<String, String>? {
        if (currentIndex >= queue.size) return null
        
        val data = queue[currentIndex++]
        context.getSharedPreferences("jarvis_prefs", Context.MODE_PRIVATE)
            .edit().putInt("last_index", currentIndex).apply()
            
        return data.first to parseSpintax(data.second)
    }

    fun reset(context: Context) {
        currentIndex = 0
        context.getSharedPreferences("jarvis_prefs", Context.MODE_PRIVATE)
            .edit().putInt("last_index", 0).apply()
    }

    private fun parseSpintax(text: String): String {
        val regex = Regex("\\{([^}]*)\\}")
        return regex.replace(text) { match ->
            match.groupValues[1].split("|").random()
        }
    }
}
