package com.example.realmdb

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class DataModel : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var name: String? = null
    var age = 0
    var gender: String? = null
}