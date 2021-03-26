package com.mtg.galleryapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class Preferences(val context: Context) {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun saveData(key: Int, data: String) {
        preferences.edit().apply {
            putString(context.getString(key), data)
        }.apply()
    }

    fun saveDataBool(key: Int, data: Boolean) {
        preferences.edit().apply {
            putBoolean(context.getString(key), data)
        }.apply()
    }

    fun containsData(key: Int): Boolean {
        return preferences.getBoolean(context.getString(key), false)
    }

    fun loadDataBoolean(key: Int, defValue: Boolean): Boolean {
        return preferences.getBoolean(context.getString(key), defValue)
    }

    fun loadData(key: Int, def: String): String {
        return preferences.getString(context.getString(key), def) ?: def
    }

    fun getDataString(key: Int): String? {
        return preferences.getString(context.getString(key), "")
    }

    fun clearPreferences() {
        preferences.edit().apply {
            clear()
        }.apply()
        return
    }

    fun clearPreference(key: Int) {
        preferences.edit().apply {
            remove(context.getString(key))
        }.apply()
        return
    }

    fun saveDataInt(key: Int, data: Int) {
        preferences.edit().apply {
            putInt(context.getString(key), data)
        }.apply()
    }

    fun loadDataInt(key: Int): Int {
        return preferences.getInt(context.getString(key), -1)
    }
}