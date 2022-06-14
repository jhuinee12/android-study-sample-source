package com.zahi.steptestapp

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreModule(private val context: Context) {
    private val Context.datastore by preferencesDataStore(name = "stepCounter")

    private val key = intPreferencesKey("step")

    val step = context.datastore.data
        .catch { ex ->
            if (ex is IOException) {
                emit(emptyPreferences())
            } else {
                throw ex
            }
        }
        .map { pref ->
            pref[key] ?: 0
        }

    suspend fun setInt (step: Int) {
        context.datastore.edit { pref ->
            pref[key] = step
        }
    }
}