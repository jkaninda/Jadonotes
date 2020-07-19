package com.jkantech.jadonotes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate
import com.jkantech.jadonotes.ui.utils.MyPreferences
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
         applyTheme()

        val versionCode:Int=BuildConfig.VERSION_CODE
        val versionName:String=BuildConfig.VERSION_NAME
        app_vname.text="Version: "+versionName+" "+"($versionCode)"
        startSplash()
        AnimImage()

    }
    private fun startSplash(){
        val temps:Long=1000
        Handler().postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish()

        },temps)

    }
     fun AnimImage(){
        app_icon.animate().apply {
            duration=1000
            rotationXBy(360f)
                .start()

        }

    }
    @SuppressLint("ResourceAsColor")
    private fun applyTheme() {
        when (MyPreferences(this).darkMode) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                delegate.applyDayNight()
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)


                delegate.applyDayNight()
            }
            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)


                delegate.applyDayNight()
            }


        }
    }

    }