package com.example.sync_front.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.sync_front.ui.login.LoginActivity
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val backExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
        val mainExecutor: Executor = ContextCompat.getMainExecutor(this)
        backExecutor.schedule({
            mainExecutor.execute{
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
        }, 0, TimeUnit.SECONDS)
    }
}