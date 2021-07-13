package com.example.dogs.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class SharedPreferencesHelper {
    companion object {
        private const val PREFS_TIME_KEY = "time"
        private var prefs: SharedPreferences? = null
        @Volatile
        private var instance: SharedPreferencesHelper? = null
        private val LOCK = Any()
        operator fun invoke(context: Context): SharedPreferencesHelper =
            instance ?: synchronized(LOCK) {
                instance ?: sharedPreferencesBuilder(context).also {
                    instance = it
                }
            }

        private fun sharedPreferencesBuilder(context: Context): SharedPreferencesHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferencesHelper()
        }
    }

    fun saveUpdateTime(time: Long) {
        prefs?.edit(commit = true) {
            putLong(PREFS_TIME_KEY, time)
        }
    }

    fun getUpdateTime() : Long = prefs?.getLong(PREFS_TIME_KEY, 0) ?: 0
}