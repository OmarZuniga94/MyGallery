package com.mtg.galleryapp

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.mtg.galleryapp.utils.Preferences
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso




@SuppressLint("StaticFieldLeak")
class Application : MultiDexApplication() {

    init {
        instance = this
    }

    companion object {
        val TAG = "Gallery App"
        private var instance: Application? = null
        private var prefs: Preferences? = null

        fun getContext(): Context {
            return instance!!.applicationContext
        }

        fun getPreferences(): Preferences {
            return prefs!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(getContext())
        prefs = Preferences(getContext())
        val builder = Picasso.Builder(this)
        builder.downloader(OkHttp3Downloader(this, Long.MAX_VALUE))
        val built = builder.build()
        built.setIndicatorsEnabled(true)
        built.isLoggingEnabled = true
        Picasso.setSingletonInstance(built)
    }
}