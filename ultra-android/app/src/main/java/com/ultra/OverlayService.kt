package com.ultra

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.WindowManager
import android.widget.Button

class OverlayService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {

        super.onCreate()

        val button = Button(this)

        button.text = "ULTRA"

        val wm =
            getSystemService(
                WINDOW_SERVICE
            ) as WindowManager

        val params =
            WindowManager.LayoutParams(
                200,
                200,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                0,
                PixelFormat.TRANSLUCENT
            )

        wm.addView(
            button,
            params
        )
    }
}