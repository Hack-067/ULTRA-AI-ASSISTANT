package com.ultra

import android.content.Context
import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import java.util.Locale

class VoiceManager(
    context: Context,
    private val onText: (String) -> Unit
) {
    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private lateinit var speaker: TextToSpeech

    init {
        speaker = TextToSpeech(context) { result ->
            if (result == TextToSpeech.SUCCESS) {
                speaker.language = Locale.US
            }
        }

        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: android.os.Bundle?) {
                val text = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()

                if (!text.isNullOrBlank()) {
                    onText(text)
                }
            }

            override fun onError(error: Int) = Unit
            override fun onReadyForSpeech(params: android.os.Bundle?) = Unit
            override fun onBeginningOfSpeech() = Unit
            override fun onRmsChanged(value: Float) = Unit
            override fun onBufferReceived(buffer: ByteArray?) = Unit
            override fun onEndOfSpeech() = Unit
            override fun onPartialResults(results: android.os.Bundle?) = Unit
            override fun onEvent(type: Int, params: android.os.Bundle?) = Unit
        })
    }

    fun listen() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
        }

        recognizer.startListening(intent)
    }

    fun speak(text: String) {
        speaker.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ultra-response")
    }

    fun release() {
        recognizer.destroy()
        speaker.shutdown()
    }
}