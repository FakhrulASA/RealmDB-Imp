package com.example.realmdb

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        RealmConfiguration.Builder().allowWritesOnUiThread(true)
    }
}