package com.jkantech.jadonotes.ui.utils

import android.content.Context
import android.preference.PreferenceManager

class MyPreferences(context: Context?) {
    companion object {

        private const val DARK_STATUS = "com.jkantech.jadonotes"



    }
    private val preferences= PreferenceManager.getDefaultSharedPreferences(context)
    var darkMode=preferences.getInt(DARK_STATUS,2)
        set(value) = preferences.edit().putInt(DARK_STATUS,value).apply()



}