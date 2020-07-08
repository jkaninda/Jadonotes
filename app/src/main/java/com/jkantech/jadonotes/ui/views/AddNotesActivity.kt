package com.jkantech.jadonotes.ui.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jkantech.jadonotes.R
import com.jkantech.jadonotes.ui.models.Note
import com.jkantech.jadonotes.ui.utils.NoteColorPicker
import kotlinx.android.synthetic.main.note_style.*
import java.text.SimpleDateFormat
import java.util.*

class AddNotesActivity : AppCompatActivity(),View.OnClickListener {

    companion object {
        val REQUEST_EDIT_NOTE = 1

        val EXTRA_NOTE = "note"
        val EXTRA_NOTE_INDEX = "noteIndex"

        val ACTION_SAVE = "com.jkantech.jadonotes.actions.ACTION_SAVE"
        val ACTION_DELETE = "com.jkantech.jadonotes.action.ACTION_DELETE"
    }


    lateinit var note: Note
    var noteIndex: Int = -1
    lateinit var titleView: TextView
    lateinit var textView: TextView
    private lateinit var CardNote: CardView
    lateinit var notecolor: FrameLayout



    //card colors
    private lateinit var colorOne: ImageButton
    private lateinit var colorTwo: ImageButton
    private lateinit var colorThree: ImageButton
    private lateinit var colorFour: ImageButton
    private lateinit var colorFive: ImageButton
    private lateinit var colorSix: ImageButton
    private lateinit var colorSeven: ImageButton
    private lateinit var colorEight: ImageButton
   private lateinit var  edit_date:String
    private lateinit var create_date:String
    private lateinit var getheure:String


            private var cardColor: String? = null
    private var colorId: Int? = null

    var appTheme=2
    lateinit var sharedPreferences: SharedPreferences
    private val themeKey = "currentTheme"
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
            "ThemePref",
            Context.MODE_PRIVATE
        )

        applyStyle()

        setContentView(R.layout.add_notes)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        title = (getString(R.string.add_note_title))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        note = intent.getParcelableExtra<Note>(EXTRA_NOTE)
        noteIndex = intent.getIntExtra(EXTRA_NOTE_INDEX, -1)

        titleView = findViewById<TextView>(R.id.title)
        textView = findViewById<TextView>(R.id.text)
        CardNote=findViewById(R.id.card_color)

        titleView.text = note.title
        textView.text = note.text
        notecolor=findViewById(R.id.notecolor)
        notecolor.visibility= GONE


        /*
*   Reccuperation de la date actuelle
 */
        val date = Calendar.getInstance().time
        val heures = Date().hours
        val minutes = Date().minutes
        @SuppressLint("SimpleDateFormat")
        val formatter = SimpleDateFormat(getString(R.string.date_format))
        edit_date = formatter.format(date).toString()
        create_date = formatter.format(date).toString()
        getheure = heures.toString() + ":" + minutes


        if (titleView.length()  >= 49) {
            toast("Vous avez atteind la taille limite , Max 50")

        }


        //Initialisation des couleurs
        colorOne = findViewById(R.id._1)
        colorTwo = findViewById(R.id._2)
        colorThree = findViewById(R.id._3)
        colorFour = findViewById(R.id._4)
        colorFive = findViewById(R.id._5)


        colorOne.setOnClickListener(this)
        colorTwo.setOnClickListener(this)
        colorThree.setOnClickListener(this)
        colorFour.setOnClickListener(this)
        colorFive.setOnClickListener(this)




        //application du couleur du CardView par defaud
        val colorNum: ImageButton = getColor()
        colorId = NoteColorPicker.NoteColor(colorNum.id)
        cardColor = "#" + Integer.toHexString(ContextCompat.getColor(this, colorId!!))
        CardNote.setCardBackgroundColor(Color.parseColor(cardColor))


        val fab = findViewById<FloatingActionButton>(R.id.style_note_fab)
        fab.setOnClickListener {
            if (notecolor.isVisible) {
                style_note_fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_color_lens))
                notecolor.visibility = GONE

            } else {
                notecolor.visibility = VISIBLE
                style_note_fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_cancel))


            }


        }





    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                saveNote()
                return true
            }


            else -> return super.onOptionsItemSelected(item)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBackPressed() {
        saveNoteAuto()
        // super.onBackPressed()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun saveNote() {
        note.title = titleView.text.toString()
        note.text = textView.text.toString()
        note.editdate = getString(R.string.create_date) + " " + create_date + " " + getString(R.string.edit_at) + " " + getheure
        note.createdate = getString(R.string.create_date) + " "  + create_date+" " + getString(R.string.edit_at) + " " + getheure
        note.color="#" + Integer.toHexString(ContextCompat.getColor(this, colorId!!))




        if (note.title!!.trim() == "" && note.text!!.trim() == "") {
            // toast("Aucune note à sauvegarder")
            finish()

        } else {
            intent = Intent(ACTION_SAVE)
            intent.putExtra(EXTRA_NOTE, note as Parcelable)
            intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
            toast(getString(R.string.note_saved))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveNoteAuto() {
        note.title = titleView.text.toString()
        note.text = textView.text.toString()
        note.editdate = getString(R.string.create_date) + " " + create_date + " " + getString(R.string.edit_at) + " " + getheure
        note.createdate = getString(R.string.create_date) + " "  + create_date+" " + getString(R.string.edit_at) + " " + getheure
        note.color="#" + Integer.toHexString(ContextCompat.getColor(this, colorId!!))



        if (note.title!!.trim() == "" && note.text!!.trim() == "") {
             //toast("Aucune note à sauvegarder")

            finish()

        } else {
            intent = Intent(ACTION_SAVE)
            intent.putExtra(EXTRA_NOTE, note as Parcelable)
            intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)

            //toast("Note sauvegardée automatiquemnt")

            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    fun deleteNote() {
        intent = Intent(ACTION_DELETE)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }





    override fun finish() {
        super.finish()
        overridePendingTransition(
                R.anim.activity_fade_in_animation,
                R.anim.activity_fade_out_animation
        )
    }



    private fun getColor(): ImageButton {

        val colorNum: ImageButton

        when ((1..5).random()) {
            1 -> {
                colorNum = colorOne
            }
            2 -> {
                colorNum = colorTwo
            }
            3 -> {
                colorNum = colorThree
            }
            4 -> {
                colorNum = colorFour
            }
            5 -> {
                colorNum = colorFive
            }

            else -> {
                colorNum = colorTwo
            }
        }
        return colorNum

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id._1 -> {
                    colorId = NoteColorPicker.NoteColor(v.id)
                }
                R.id._2 -> {
                    colorId = NoteColorPicker.NoteColor(v.id)
                }
                R.id._3 -> {
                    colorId = NoteColorPicker.NoteColor(v.id)
                }
                R.id._4 -> {
                    colorId = NoteColorPicker.NoteColor(v.id)
                }
                R.id._5 -> {
                    colorId = NoteColorPicker.NoteColor(v.id)

                }


            }

        }
        cardColor = "#" + Integer.toHexString(ContextCompat.getColor(this, colorId!!))
        CardNote.setCardBackgroundColor(Color.parseColor(cardColor))

    }

    fun style(view: View) {
        if (notecolor.isVisible){
            notecolor.visibility= GONE
        }else
        notecolor.visibility= VISIBLE


    }
    private fun applyStyle() {

        when (sharedPreferences.getInt(themeKey, 0)) {

            0 -> theme.applyStyle(R.style.Theme_JadoNotes, true)
            1 -> theme.applyStyle(R.style.Theme1, true)
            2 -> theme.applyStyle(R.style.Theme2, true)
            3 -> theme.applyStyle(R.style.Theme3, true)

        }


    }

}
