package com.example.datastoresampleapp

import android.content.Context
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

class UserManager(context: Context) {
    private val dataStore = context.dataStoreFile(fileName = "user_prefs")

    companion object {
        val USER_NAME_KEY = stringPreferencesKey("USER_NAME")
        val USER_AGE_KEY = intPreferencesKey("USER_AGE")
        val USER_GENDER_KEY = booleanPreferencesKey("USER_GENDER")
    }

    suspend fun storeUser(age:Int, name:String, isMale:Boolean) {
    }
}