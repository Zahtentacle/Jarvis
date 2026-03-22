package com.jarvis.wablast

import android.content.Context
import android.content.Intent
import android.net.Uri

object WhatsAppEngine {
    fun send(context: Context, number: String, message: String) {
        val url = "https://wa.me/$number?text=${Uri.encode(message)}"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            setPackage("com.whatsapp")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) { e.printStackTrace() }
    }
}

