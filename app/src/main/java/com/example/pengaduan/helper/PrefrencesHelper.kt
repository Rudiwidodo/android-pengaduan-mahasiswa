package com.example.pengaduan.helper

import android.content.Context
import android.content.SharedPreferences

class PrefrencesHelper(context: Context) {
    private val PREF_NAME = "sharedprefrencespengdaduan"
    private val SharedPref: SharedPreferences
    val editor: SharedPreferences.Editor

    init {
        SharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = SharedPref.edit()
    }

    fun put(key: String, value: Boolean){
        editor.putBoolean(key, value).apply()
    }

    fun getBoolean(key: String) : Boolean {
        return SharedPref.getBoolean(key,false)
    }
    fun put(key: String, value: String){
        editor.putString(key, value).apply()
    }

    fun getString(key: String) : String? {
        return SharedPref.getString(key,null)
    }
    fun put(key: String, value: Int){
        editor.putInt(key, value).apply()
    }

    fun getInt(key: String) : Int {
        return SharedPref.getInt(key,0)
    }
}