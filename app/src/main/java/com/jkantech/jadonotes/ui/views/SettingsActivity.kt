package com.jkantech.jadonotes.ui.views

//import kotlinx.android.synthetic.main.activity_kaninda.*

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
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
    private var isNightModeOn: Boolean = false


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
            chooseStyleDialog()
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

     @SuppressLint("ResourceType")
     private fun chooseStyleDialog(){
         val builder=AlertDialog.Builder(this)
         builder.setTitle(getString(R.string.choose_style))
         val res:Resources= resources
         val styles=res.getStringArray(R.array.theme_styles)
         val stylevalue=0
         val checkedItem=sharedPreferences.getInt(themeKey,stylevalue)
         builder.setSingleChoiceItems(styles,checkedItem){
             dialog, which ->
             when(which){
                 0-> {
                     sharedPreferences.edit().putInt(themeKey, 0).apply()

                     if (checkedItem==0){

                         dialog.dismiss()
                     }else {

                         val rIntent = Intent()
                         rIntent.putExtra(themeKey, 0)
                         setResult(Activity.RESULT_OK, rIntent)
                         //startActivity(rIntent)
                         val intent = Intent(this, MainActivity::class.java)
                         startActivity(intent)
                         finish()
                     }

                 }
                 1-> {
                     sharedPreferences.edit().putInt(themeKey, 1).apply()

                     if (checkedItem==1){

                         dialog.dismiss()
                     }else {

                         val rIntent = Intent()
                         rIntent.putExtra(themeKey, 1)
                         setResult(Activity.RESULT_OK, rIntent)
                         val intent = Intent(this, MainActivity::class.java)
                         startActivity(intent)
                         finish()
                     }
                 }
                 2-> {
                     sharedPreferences.edit().putInt(themeKey, 2).apply()

                     if (checkedItem==2){

                         dialog.dismiss()
                     }else {

                         val rIntent = Intent()
                         rIntent.putExtra(themeKey, 2)
                         setResult(Activity.RESULT_OK, rIntent)
                         val intent = Intent(this, MainActivity::class.java)
                         startActivity(intent)
                         finish()
                     }
                 }
                 3-> {
                     sharedPreferences.edit().putInt(themeKey, 3).apply()

                     if (checkedItem==3){

                         dialog.dismiss()
                     }else {

                         val rIntent = Intent()
                         rIntent.putExtra(themeKey, 3)
                         setResult(Activity.RESULT_OK, rIntent)
                         val intent = Intent(this, MainActivity::class.java)
                         startActivity(intent)
                         finish()
                     }
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
    private fun toast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
    private fun getPro(){
        toast("Passez au Premium pour utiliser ce beau style")
    }

     */


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


