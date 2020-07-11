package com.jkantech.jadonotes.ui.views

//import kotlinx.android.synthetic.main.activity_kaninda.*

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.jkantech.jadonotes.MainActivity
import com.jkantech.jadonotes.R
import com.jkantech.jadonotes.ui.utils.MyPreferences
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.containt_settings.*
import java.util.*

class SettingsActivity : AppCompatActivity() {
    var appTheme=2
     lateinit var sharedPreferences: SharedPreferences
     private val themeKey = "currentTheme"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        appTheme= MyPreferences(this).darkMode
        sharedPreferences = getSharedPreferences(
                "ThemePref",
                Context.MODE_PRIVATE
        )
        applyStyle()

        setContentView(R.layout.activity_settings)
        title=getString(R.string.action_settings)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        checkTheme()


        card_theme.setOnClickListener{
            chooseThemeDialog()
        }
        card_style.setOnClickListener {
            //chooseStyleDialog()
            themeColorDialog()
        }



    }

    private fun chooseThemeDialog(){
        val builder=AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.apparence_mode))
        val res=resources
        val themes=res.getStringArray(R.array.theme_mode)

        builder.setSingleChoiceItems(themes,MyPreferences(this).darkMode){
                dialog, which ->
            when(which){
                0->{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    MyPreferences(this).darkMode=0
                    delegate.applyDayNight()
                    dialog.dismiss()
                }
                1->{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    MyPreferences(this).darkMode=1

                    delegate.applyDayNight()
                    dialog.dismiss()

                }
                2->{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    MyPreferences(this).darkMode=2

                    delegate.applyDayNight()
                    dialog.dismiss()

                }
            }


        }
            .setPositiveButton(getString(R.string.action_annuler),DialogInterface.OnClickListener { dialog, which ->
            })
        val dialog=builder.create()
        dialog.show()
    }
        @SuppressLint("ResourceAsColor")
        private fun checkTheme() {
            when (MyPreferences(this).darkMode) {
                0 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    theme_mode.text = getString(R.string.daymode)
                    delegate.applyDayNight()
                }
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    theme_mode.text = getString(R.string.night_mode)
                    delegate.applyDayNight()
                }
                2 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    theme_mode.text = getString(R.string.mode_auto)


                    delegate.applyDayNight()
                }

            }

        }
    /*

    private fun getPro(){
        toast("Passez au Premium pour utiliser ce beau style")
    }

     */

    private fun themeColorDialog() {
        val dialog = Dialog(this)
        dialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.theme_color_dialog)
            setCancelable(true)
            val theme1=dialog.findViewById<ImageButton>(R.id._1)
            val theme2=dialog.findViewById<ImageButton>(R.id._2)
            val theme3=dialog.findViewById<ImageButton>(R.id._3)
            val theme4=dialog.findViewById<ImageButton>(R.id._4)
            val stylevalue=0
            val checkedItem=sharedPreferences.getInt(themeKey,stylevalue)
            theme1.setOnClickListener{
                if (checkedItem==0){
                    dismiss()
                }else {


                    sharedPreferences.edit().putInt(themeKey, 0).apply()
                    val rIntent = Intent(this@SettingsActivity, MainActivity::class.java)
                    rIntent.putExtra(themeKey, 0)
                    setResult(Activity.RESULT_OK, rIntent)
                    startActivity(rIntent)
                    finish()

                }


            }
            theme2.setOnClickListener {
                if (checkedItem==1){
                    dismiss()
                }else {
                    sharedPreferences.edit().putInt(themeKey, 1).apply()
                    val rIntent = Intent(this@SettingsActivity, MainActivity::class.java)
                    rIntent.putExtra(themeKey, 1)
                    setResult(Activity.RESULT_OK, rIntent)
                    startActivity(rIntent)
                    finish()

                }



            }
            theme3.setOnClickListener {
                if (checkedItem==2){
                    dismiss()
                }else {


                    sharedPreferences.edit().putInt(themeKey, 2).apply()
                    val rIntent = Intent(this@SettingsActivity, MainActivity::class.java)
                    rIntent.putExtra(themeKey, 2)
                    setResult(Activity.RESULT_OK, rIntent)
                    startActivity(rIntent)
                    finish()

                }



            }
            theme4.setOnClickListener {
                if (checkedItem==3){
                    dismiss()
                }else {


                    sharedPreferences.edit().putInt(themeKey, 3).apply()
                    val rIntent = Intent(this@SettingsActivity, MainActivity::class.java)
                    rIntent.putExtra(themeKey, 3)
                    setResult(Activity.RESULT_OK, rIntent)
                    startActivity(rIntent)
                    finish()

                }



            }

            show()


        }
    }





    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun applyStyle() {

        when (sharedPreferences.getInt(themeKey, 0)) {

            0 -> theme.applyStyle(R.style.Theme_JadoNotes, true)
            1 -> theme.applyStyle(R.style.Theme1, true)
            2 -> theme.applyStyle(R.style.Theme2, true)
            3 -> theme.applyStyle(R.style.Theme3, true)

        }


    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            R.anim.activity_fade_in_animation,
            R.anim.activity_fade_out_animation
        )
    }


}


