package com.jkantech.jadonotes.ui.views

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jkantech.jadonotes.MainActivity
import com.jkantech.jadonotes.R
import com.jkantech.jadonotes.ui.database.DBManagerCategory
import com.jkantech.jadonotes.ui.models.Note
import com.jkantech.jadonotes.ui.database.DBManagerNote
import com.jkantech.jadonotes.ui.utils.NoteColorPicker
import kotlinx.android.synthetic.main.containt_add_notes.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
/**
 * Created by Jonas Kaninda on 10/07/2020.
 */
class AddNotesActivity : AppCompatActivity(),View.OnClickListener,AdapterView.OnItemSelectedListener {

    companion object {
        val REQUEST_EDIT_NOTE = 1
        val REQUEST_ADD_NOTE = 1

        val EXTRA_NOTE = "note"
        val EXTRA_NOTE_INDEX = "noteIndex"

        val ACTION_SAVE = "com.jkantech.jadonotes.actions.ACTION_SAVE"
        val ACTION_DELETE = "com.jkantech.jadonotes.action.ACTION_DELETE"
        private val SPEECH_REC = 0
        const val TAG = "message"


    }


    lateinit var note: Note
    var noteIndex: Int = -1
    lateinit var time: TextView
    lateinit var titleView: TextView
    lateinit var textView: TextView
    private lateinit var CardNote: CardView
    lateinit var notecolor: FrameLayout
    lateinit var spinner: Spinner

    val PERMISSION_RECORD_AUDIO = 0


    //card colors
    private lateinit var colorOne: ImageButton
    private lateinit var colorTwo: ImageButton
    private lateinit var colorThree: ImageButton
    private lateinit var colorFour: ImageButton
    private lateinit var colorFive: ImageButton
    private lateinit var colorSix: ImageButton
    private lateinit var colorSeven: ImageButton
    private lateinit var colorEight: ImageButton
    private lateinit var edit_date: String
    private lateinit var create_date: String
    private lateinit var getheure: String
    private lateinit var colorButton: AppCompatImageView
    private lateinit var colorLayout: LinearLayout
    private lateinit var micButton: AppCompatImageView
    lateinit var fontButton: AppCompatImageView
    lateinit var saveButton: AppCompatImageView

    lateinit var myCalendar: Calendar
    var category: String? =null


    private var cardColor: String? = null
    private var colorId: Int? = null

    lateinit var sharedPreferences: SharedPreferences
    private val themeKey = "currentTheme"

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")

    override fun onCreate(savedInstanceState: Bundle?) {
        window.allowEnterTransitionOverlap = true

        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
                "ThemePref",
                Context.MODE_PRIVATE
        )

        applyStyle()

        setContentView(R.layout.activity_add_notes)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        title = (getString(R.string.add_note_title))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        note = intent.getParcelableExtra<Note>(EXTRA_NOTE)!!
        noteIndex = intent.getIntExtra(EXTRA_NOTE_INDEX, -1)
        time = findViewById(R.id.time)

        titleView = findViewById(R.id.title)
        textView = findViewById(R.id.text)
        CardNote = findViewById(R.id.card_color)

        titleView.text = note.title
        textView.text = note.text
        category=note.category

        myCalendar = Calendar.getInstance()
/*
        val intent: Intent = getIntent()
        val addnote: String = intent.getStringExtra("test")
        if (addnote!=null){
            textView.text=addnote
        }

 */


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
        val hour = getheure + " " + edit_date
        //val taille:Int=t
        time.text = hour

        applyTextSize()


        //Initialisation des couleurs
        colorOne = findViewById(R.id._1)
        colorTwo = findViewById(R.id._2)
        colorThree = findViewById(R.id._3)
        colorFour = findViewById(R.id._4)
        colorFive = findViewById(R.id._5)
        //Button
        colorButton = findViewById(R.id.colorButton)

        colorLayout = findViewById(R.id.colorLayout)
        micButton = findViewById(R.id.micButton)
        fontButton = findViewById(R.id.fontButton)
        saveButton = findViewById(R.id.saveButton)


        colorOne.setOnClickListener(this)
        colorTwo.setOnClickListener(this)
        colorThree.setOnClickListener(this)
        colorFour.setOnClickListener(this)
        colorFive.setOnClickListener(this)
        colorButton.setOnClickListener(this)
        micButton.setOnClickListener(this)
        fontButton.setOnClickListener(this)
        saveButton.setOnClickListener(this)


        spinner = findViewById(R.id.spinnerCategory)
         loadDataInSpinner()



        //application du couleur du CardView par defaud
        val colorNum: ImageButton = getColor()
        colorId = NoteColorPicker.NoteColor(colorNum.id)
        cardColor = "#" + Integer.toHexString(ContextCompat.getColor(this, colorId!!))
        CardNote.setCardBackgroundColor(Color.parseColor(cardColor))


        titleView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.i(TAG, "rien")


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (titleView.length() >= 49) {
                    toast(getString(R.string.max_title_characters))

                }
            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "rien")
            }


        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                saveNote()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }




    private fun saveNote() {
        note.title = titleView.text.toString()
        note.text = textView.text.toString()
        note.editdate = getString(R.string.create_date) + " " + create_date + " " + getString(R.string.edit_at) + " " + getheure
        note.createdate = getString(R.string.create_date) + " " + create_date + " " + getString(R.string.edit_at) + " " + getheure
        note.color = "#" + Integer.toHexString(ContextCompat.getColor(this, colorId!!))
        val dbManager = DBManagerNote(this)

        if (note.title!!.trim() == "" && note.text!!.trim() == "") {
            // toast("Aucune note à sauvegarder")
            finish()

        } else {
            intent = Intent(ACTION_SAVE)
            intent.putExtra(EXTRA_NOTE, note as Parcelable)
            intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
            //toast(getString(R.string.note_saved))
            dbManager.insert(note.title!!, note.text!!, note.category!!, note.editdate!!, note.createdate!!, note.color!!,note.text_size,0)


            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun saveNoteAuto() {
        note.title = titleView.text.toString()
        note.text = textView.text.toString()
        note.editdate = getString(R.string.create_date) + " " + create_date + " " + getString(R.string.edit_at) + " " + getheure
        note.createdate = getString(R.string.create_date) + " " + create_date + " " + getString(R.string.edit_at) + " " + getheure
        note.color = "#" + Integer.toHexString(ContextCompat.getColor(this, colorId!!))
        val dbManager = DBManagerNote(this)




        if (note.title!!.trim() == "" && note.text!!.trim() == "") {

            finish()

        } else {
            intent = Intent(ACTION_SAVE)
            intent.putExtra(EXTRA_NOTE, note as Parcelable)
            intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
            dbManager.insert(note.title!!, note.text!!, note.category!!, note.editdate!!, note.createdate!!, note.color!!,note.text_size,0)




            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }


    fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    override fun onSupportNavigateUp(): Boolean {
        saveNoteAuto()
        return true
    }

    override fun onBackPressed() {
        if (colorLayout.isVisible){
            colorLayout.visibility= GONE
        }else {
            saveNoteAuto()
            super.onBackPressed()
        }
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

                R.id.colorButton -> {
                    if (colorLayout.isVisible) {

                        colorLayout.visibility = GONE

                    } else {
                        colorLayout.visibility = VISIBLE


                    }

                }
                R.id.micButton -> {
                    if (colorLayout.isVisible) {

                        colorLayout.visibility = GONE
                    }
                    callPermissionRecord()


                }


                R.id.fontButton -> {
                    if (colorLayout.isVisible) {

                        colorLayout.visibility = GONE
                    }
                    showfontSizePopup()
                }
                R.id.saveButton -> {
                    saveNote()
                }


            }

        }
        cardColor = "#" + Integer.toHexString(ContextCompat.getColor(this, colorId!!))
        CardNote.setCardBackgroundColor(Color.parseColor(cardColor))

    }


    private fun applyTextSize() {
        when (note.text_size) {

            1 -> {
                titleView.textSize = 22f
                textView.textSize = 18f

            }
            2 -> {
                titleView.textSize = 27f
                textView.textSize = 22f


            }
            3 -> {
                titleView.textSize = 30f
                textView.textSize = 26f

            }




        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        if (requestCode == SPEECH_REC) {
            if (resultCode == Activity.RESULT_OK) {
                val result: ArrayList<String>? = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (result != null) {
                    textView.text = result.get(0)

                }


            }
        }


    }

    private fun callPermissionRecord() {

        if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.RECORD_AUDIO),
                    PERMISSION_RECORD_AUDIO)
        } else {
            askSpeechInput()
        }
    }

    private fun askSpeechInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            toast(getString(R.string.speech_not_available))
        } else {
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech))
            startActivityForResult(i, SPEECH_REC)


        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_RECORD_AUDIO -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    askSpeechInput()

                }
            }
        }
    }


    private fun applyStyle() {

        when (sharedPreferences.getInt(themeKey, 0)) {

            0 -> theme.applyStyle(R.style.Theme_JadoNotes, true)
            1 -> theme.applyStyle(R.style.Theme1, true)
            2 -> theme.applyStyle(R.style.Theme2, true)
            3 -> theme.applyStyle(R.style.Theme3, true)

        }


    }

    private fun showfontSizePopup() {
        val popupMenu = PopupMenu(this, fontButton)
        popupMenu.inflate(R.menu.fontsize_menu)
        popupMenu.menu.getItem(0).title = getString(R.string.small_text)
        popupMenu.menu.getItem(1).title = getString(R.string.medium)
        popupMenu.menu.getItem(2).title = getString(R.string.large)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_small_text -> {
                    titleView.textSize = 22f
                    textView.textSize = 18f
                    note.text_size=1


                    return@setOnMenuItemClickListener true
                }
                R.id.action_medium_text -> {
                    titleView.textSize = 27f
                    textView.textSize = 22f
                    note.text_size=2
                    return@setOnMenuItemClickListener true
                }
                R.id.action_large_text -> {
                    titleView.textSize = 30f
                    textView.textSize = 26f
                    note.text_size=3
                    return@setOnMenuItemClickListener true
                }

            }
            false
        }
        popupMenu.show()
    }

    private fun loadDataInSpinner() {

        val dbManager = DBManagerCategory(this)
        var labels = dbManager.getListOfCategory()
        if (labels.isEmpty()) {
            val arrayList: ArrayList<String> = ArrayList()
            arrayList.add(getString(R.string.no_category))
            labels = arrayList
        }
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels)


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val text: String = parent?.getItemAtPosition(position).toString()
        note.category=text


    }


}



