package com.jarvis.wablast

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object ResultLogger {
    fun log(context: Context, number: String, status: String) {
        if (status == "SENT") CurrentSession.sentCount++ else CurrentSession.failedCount++
        
        val file = File(context.getExternalFilesDir(null), "jarvis_log.txt")
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        
        try {
            file.appendText("$time | $number | $status\n")
        } catch (e: Exception) { e.printStackTrace() }
    }
}
