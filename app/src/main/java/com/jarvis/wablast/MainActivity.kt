package com.jarvis.wablast

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var txtStatus: TextView
    private lateinit var txtCounter: TextView
    private val handler = Handler(Looper.getMainLooper())

    private val uiUpdater = object : Runnable {
        override fun run() {
            txtCounter.text = "Sent: ${CurrentSession.sentCount} | Failed: ${CurrentSession.failedCount}"
            handler.postDelayed(this, 500)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 50, 50, 50)
        }

        txtStatus = TextView(this).apply { text = "Status: IDLE"; textSize = 18f }
        txtCounter = TextView(this).apply { text = "Sent: 0 | Failed: 0"; textSize = 16f }
        val btnStart = Button(this).apply { text = "START / RESUME" }
        val btnStop = Button(this).apply { text = "STOP" }
        val btnReset = Button(this).apply { text = "RESET INDEX" }

        root.addView(txtStatus)
        root.addView(txtCounter)
        root.addView(btnStart)
        root.addView(btnStop)
        root.addView(btnReset)

        setContentView(root)

        btnStart.setOnClickListener {
            CurrentSession.isStopped = false
            JarvisAccessibility.sendLock = false
            JarvisAccessibility.isDetecting = false
            txtStatus.text = "Status: RUNNING"

            CSVProcessor.load(this)
            sendBroadcast(Intent("JARVIS_NEXT").setPackage(packageName))
        }

        btnStop.setOnClickListener {
            CurrentSession.isStopped = true
            txtStatus.text = "Status: STOPPED"
        }

        btnReset.setOnClickListener {
            CSVProcessor.reset(this)
            CurrentSession.sentCount = 0
            CurrentSession.failedCount = 0
            txtStatus.text = "Status: INDEX RESET"
        }
    }

    override fun onResume() {
        super.onResume()
        handler.post(uiUpdater)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(uiUpdater)
    }
}
