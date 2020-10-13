package com.steve.flowlayoutradiogroup

import android.app.Application
import android.content.Context

class App : Application() {

    companion object {
        private lateinit var mAppContext: Context
        fun getAppContext(): Context = mAppContext
    }

    override fun onCreate() {
        super.onCreate()
        mAppContext = applicationContext
    }
}