package com.jarvis.wablast.automation

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.os.Bundle

class WhatsAppEngine : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val rootNode = rootInActiveWindow ?: return

        [span_2](start_span)// Mapping Visual: Mencari ID objek tombol kirim WhatsApp secara presisi[span_2](end_span)
        val sendButtons = rootNode.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send")
        
        if (sendButtons.isNotEmpty()) {
            val button = sendButtons[0]
            if (button.isEnabled) {
                // Eksekusi klik otomatis
                button.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                
                // Jeda sinkronisasi sebelum kembali ke antrean berikutnya
                Thread.sleep(800)
                performGlobalAction(GLOBAL_ACTION_BACK)
            }
        }
    }

    override fun onInterrupt() {
        // Logika recovery jika servis terhenti secara paksa
    }
}
