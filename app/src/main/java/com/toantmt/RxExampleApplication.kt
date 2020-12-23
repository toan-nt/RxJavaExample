package com.toantmt

import android.app.Application
import com.toantmt.db.AppDatabase

class RxExampleApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AppDatabase.initDatabase(this)
        AppDatabase.get().clearAllTables()
    }
}