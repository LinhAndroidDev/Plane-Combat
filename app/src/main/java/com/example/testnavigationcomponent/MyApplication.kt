package com.example.testnavigationcomponent

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        setUpScreenSize()
    }

    private fun setUpScreenSize() {
        screenWidth = resources.displayMetrics.widthPixels
        screenHeight = resources.displayMetrics.heightPixels
    }
}