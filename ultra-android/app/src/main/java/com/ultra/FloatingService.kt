package com.ultra

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView

class FloatingService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var bubble: TextView
    private lateinit var voiceManager: VoiceManager
    private lateinit var api: UltraApi

    override fun onCreate() {
        super.onCreate()

        startForeground(1, createNotification())
        api = UltraApi("http://10.0.2.2:8000/chat")

        voiceManager = VoiceManager(this) { text ->
            Thread {
                val response = api.chat(text)
                android.os.Handler(mainLooper).post {
                    voiceManager.speak(response)
                }
            }.start()
        }

        if (!android.provider.Settings.canDrawOverlays(this)) {
            stopSelf()
            return
        }

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        bubble = TextView(this).apply {
            text = "U"
            textSize = 22f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(20, 100, 220))
            setOnClickListener {
                voiceManager.listen()
            }
        }

        val params = WindowManager.LayoutParams(
            150,
            150,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = 32
            y = 180
        }

        windowManager.addView(bubble, params)
    }

    private fun createNotification(): Notification {
        val channel = NotificationChannel(
            "ultra_service",
            "ULTRA Assistant",
            NotificationManager.IMPORTANCE_LOW
        )

        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)

        return Notification.Builder(this, "ultra_service")
            .setContentTitle("ULTRA is active")
            .setContentText("Tap the bubble to speak")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .build()
    }

    override fun onDestroy() {
        voiceManager.release()
        windowManager.removeView(bubble)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}