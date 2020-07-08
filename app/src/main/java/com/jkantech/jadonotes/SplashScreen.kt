package com.jkantech.jadonotes

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
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
}