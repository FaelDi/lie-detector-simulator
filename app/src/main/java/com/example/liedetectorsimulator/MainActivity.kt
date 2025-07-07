package com.example.liedetectorsimulator

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.example.liedetectorsimulator.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var scannerTone: ToneGenerator
    private lateinit var resultTone: ToneGenerator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scannerTone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        resultTone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

        binding.startButton.setOnClickListener {
            startScan()
        }
    }

    private fun startScan() {
        binding.resultText.text = getString(R.string.scanning)
        binding.resultText.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.progressBar.visibility = android.view.View.VISIBLE
        scannerTone.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 3000)

        Handler(Looper.getMainLooper()).postDelayed({
            val isTruth = Random.nextBoolean()
            val textRes = if (isTruth) R.string.truth else R.string.lie
            val colorRes = if (isTruth) R.color.truth_green else R.color.lie_red
            binding.resultText.text = getString(textRes)
            binding.resultText.setTextColor(ContextCompat.getColor(this, colorRes))
            binding.progressBar.visibility = android.view.View.GONE
            resultTone.startTone(ToneGenerator.TONE_PROP_ACK, 200)
            vibrate()
        }, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        scannerTone.release()
        resultTone.release()
    }

    private fun vibrate() {
        val vibrator = getSystemService<Vibrator>()
        vibrator?.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}
