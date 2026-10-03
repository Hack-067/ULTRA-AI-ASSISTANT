package com.ultra

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class UltraAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event == null) return

        val text = event.text.joinToString(" ")

        println("ULTRA SCREEN DATA:")
        println(text)
    }

    override fun onInterrupt() {

    }
}