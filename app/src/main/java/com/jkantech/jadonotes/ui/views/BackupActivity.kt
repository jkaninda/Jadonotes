package com.jkantech.jadonotes.ui.views

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.jkantech.jadonotes.R

class BackupActivity : AppCompatActivity(),View.OnClickListener {

    var appTheme=2
    lateinit var sharedPreferences: SharedPreferences
    lateinit var backup_text:Button
    lateinit var backup_notes:Button

    private val themeKey = "currentTheme"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
            "ThemePref",
            Context.MODE_PRIVATE
        )

        applyStyle()

        setContentView(R.layout.activity_backup)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        title = (getString(R.string.backup))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        backup_text=findViewById(R.id.backup_text)
        backup_notes=findViewById(R.id.backup_notes)
        backup_text.setOnClickListener(this)
        backup_notes.setOnClickListener(this)
    }


    private fun applyStyle() {

        when (sharedPreferences.getInt(themeKey, 0)) {

            0 -> theme.applyStyle(R.style.Theme_JadoNotes, true)
            1 -> theme.applyStyle(R.style.Theme1, true)
            2 -> theme.applyStyle(R.style.Theme2, true)
            3 -> theme.applyStyle(R.style.Theme3, true)

        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.backup_text->{
                    toast(getString(R.string.backup_note_msg))


                }
                R.id.backup_notes->{
                    toast(getString(R.string.backup_note_msg))



                }
            }
        }
    }
}