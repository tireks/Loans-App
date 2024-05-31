package com.tirexmurina.leonov

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tirexmurina.leonov.databinding.ActivityMainBinding
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainHandler = Handler(Looper.getMainLooper())
    private var timer: Timer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.timerButton.setOnClickListener {
            startTimer()
        }
    }

    private fun startTimer() {
        timer?.cancel()
        timer = Timer()

        var secondsLeft = 10

        val timerTask = object : TimerTask() {
            override fun run() {
                if (secondsLeft > 0) {
                    mainHandler.post {
                        binding.timerText.text = "Timer: $secondsLeft"
                    }
                    secondsLeft--
                } else {
                    timer?.cancel()
                    mainHandler.post {
                        binding.timerText.text = getString(R.string.timerText_label)
                    }
                }
            }
        }

        timer?.schedule(timerTask, 0, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }


}