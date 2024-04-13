package com.bendeng.swingproject

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App : Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
    }

    companion object{
        lateinit var instance : App
    }
}