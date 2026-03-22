package com.jarvis.wablast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper

class NextReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (CurrentSession.isStopped) return

        val next = CSVProcessor.next(context)

        if (next != null) {
            CurrentSession.number = next.first
            
            Handler(Looper.getMainLooper()).postDelayed({
                if (!CurrentSession.isStopped) {
                    WhatsAppEngine.send(context, next.first, next.second)
                }
            }, DelayManager.nextDelay())
        } else {
            JarvisAccessibility.sendLock = false
        }
    }
}

