package com.idz.find_my_dog.Base

import android.app.Application
import android.content.Context

class ApplicationGlobals: Application()  {
    object Globals {
        var appContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        Globals.appContext = applicationContext
    }
}
