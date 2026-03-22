package com.jarvis.wablast.automation

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class WhatsAppEngine : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val rootNode = rootInActiveWindow ?: return
        val nodes = rootNode.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send")
        if (nodes.isNotEmpty()) {
            nodes[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
            Thread.sleep(500)
            performGlobalAction(GLOBAL_ACTION_BACK)
        }
    }
    override fun onInterrupt() {}
}
