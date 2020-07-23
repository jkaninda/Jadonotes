package com.jkantech.jadonotes.ui.utils
/**
 * Created by Jonas Kaninda on 10/07/2020.
 */
import android.content.Context
import android.widget.Toast





    fun toastMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

