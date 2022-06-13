package com.zahi.steptestapp

import android.app.Application
import android.content.Context
import android.content.Intent

class App : Application() {

    init{
        instance = this

        val intent = Intent(this.applicationContext, StepForegroundService::class.java)
        startService(intent)
    }

    companion object {
        lateinit var instance: App
        fun ApplicationContext() : Context {
            return instance.applicationContext
        }
    }
}