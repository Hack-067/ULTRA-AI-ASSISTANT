package com.ultra

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class UltraVoice(context: Context) {

    private val tts = TextToSpeech(
        context
    ) {

    }

    init {
        tts.language = Locale.US
    }

    fun speak(text: String) {

        tts.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
    }
}