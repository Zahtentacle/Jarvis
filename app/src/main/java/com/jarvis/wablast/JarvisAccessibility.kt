package com.jarvis.wablast

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class JarvisAccessibility : AccessibilityService() {

    companion object {
        var sendLock = false
        var isDetecting = false
    }

    private val handler = Handler(Looper.getMainLooper())
    private var timeoutRunnable: Runnable? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (CurrentSession.isStopped || event?.packageName != "com.whatsapp" || sendLock) return

        val root = rootInActiveWindow ?: return

        if (!isDetecting) {
            isDetecting = true
            val activeNumber = CurrentSession.number

            timeoutRunnable = Runnable {
                if (!sendLock && !CurrentSession.isStopped && CurrentSession.number == activeNumber) {
                    ResultLogger.log(applicationContext, activeNumber, "FAILED/TIMEOUT")
                    performGlobalAction(GLOBAL_ACTION_BACK)
                    sendBroadcast(Intent("JARVIS_NEXT").setPackage(packageName))
                }
                isDetecting = false
            }
            handler.postDelayed(timeoutRunnable!!, 8000)
        }

        clickSend(root)
    }

    private fun clickSend(root: AccessibilityNodeInfo): Boolean {
        val ids = arrayOf("com.whatsapp:id/send", "com.whatsapp:id/send_button")
        for (id in ids) {
            val nodes = root.findAccessibilityNodeInfosByViewId(id)
            if (nodes.isNotEmpty()) return performClick(nodes[0])
        }
        return false
    }

    private fun performClick(node: AccessibilityNodeInfo?): Boolean {
        var current = node
        while (current != null) {
            if (current.isClickable) {
                sendLock = true
                
                // BATALKAN TIMEOUT JIKA KLIK SUKSES
                timeoutRunnable?.let { handler.removeCallbacks(it) }
                isDetecting = false

                current.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                ResultLogger.log(applicationContext, CurrentSession.number, "SENT")

                Handler(Looper.getMainLooper()).postDelayed({
                    if (!CurrentSession.isStopped) {
                        performGlobalAction(GLOBAL_ACTION_BACK)
                        sendBroadcast(Intent("JARVIS_NEXT").setPackage(packageName))
                    }
                    sendLock = false
                }, 500)
                
                return true
            }
            current = current.parent
        }
        return false
    }

    override fun onInterrupt() {}
}
