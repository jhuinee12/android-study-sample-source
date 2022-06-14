package com.zahi.steptestapp

import android.app.Application
import android.content.Context
import android.content.Intent

class App : Application() {
    private lateinit var dataStore: DataStoreModule

    companion object {
        private lateinit var dataStoreApplication: App
        fun getInstance(): App = dataStoreApplication
    }

    override fun onCreate() {
        super.onCreate()
        dataStoreApplication = this
        dataStore = DataStoreModule(this)
    }

    fun getDataStore(): DataStoreModule = dataStore
}