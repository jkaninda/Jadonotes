package com.jkantech.jadonotes.ui.views

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jkantech.jadonotes.R
import com.jkantech.jadonotes.ui.models.Note
import com.jkantech.jadonotes.ui.utils.NoteColorPicker
import kotlinx.android.synthetic.main.add_notes.*
import kotlinx.android.synthetic.main.containt_note_detail.*
import kotlinx.android.synthetic.main.note_style.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class NoteDetailActivity : AppCompatActivity(),View.OnClickListener {

    companion object {
        val REQUEST_EDIT_NOTE = 1

        val EXTRA_NOTE = "note"
        val EXTRA_NOTE_INDEX = "noteIndex"

        val ACTION_SAVE = "com.jkantech.jadonotes.actions.ACTION_SAVE"
        val ACTION_DELETE = "com.jkantech.jadonotes.action.ACTION_DELETE"
        lateinit var menu: Menu


    }

    lateinit var note: Note
    lateinit var editnote: Note
    var noteIndex: Int = -1
    var editnoteIndex: Int = -1
    lateinit var titleView: TextView
    lateinit var textView: TextView
    lateinit var EdittitleView: TextView
    lateinit var EdittextView: TextView
    lateinit var Editdate: TextView
    lateinit var Createdate: TextView
    lateinit var notecolor: FrameLayout
    lateinit var notestyle: FrameLayout
    lateinit var Notedetailcolor:ScrollView
    lateinit var sendNote:String
    lateinit var imageView: ImageView
    lateinit var view: View




    private lateinit var CardNote: CardView
    private var idColorNote: Int? = null
    private val TAG = "msg"






    //card colors
    private lateinit var colorOne: ImageButton
    private lateinit var colorTwo: ImageButton
    private lateinit var colorThree: ImageButton
    private lateinit var colorFour: ImageButton
    private lateinit var colorFive: ImageButton
    private lateinit var  edit_date:String
    private lateinit var create_date:String
    private lateinit var getheure:String



    private var cardColor: String? = null
    private var colorId: Int? = null

    var appTheme=2
    lateinit var sharedPreferences: SharedPreferences
    private val themeKey = "currentTheme"
    private val textSize = "currentsize"




    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
            "ThemePref",
            Context.MODE_PRIVATE
        )
        applyStyle()
        setContentView(R.layout.activity_note_detail)
        toolbar.setTitle(getString(R.string.note_detail))


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        note = intent.getParcelableExtra<Note>(EXTRA_NOTE)
        noteIndex = intent.getIntExtra(EXTRA_NOTE_INDEX, 1)

        editnote = intent.getParcelableExtra<Note>(EXTRA_NOTE)
        editnoteIndex = intent.getIntExtra(EXTRA_NOTE_INDEX, -1)

         sendNote="${note.title} \n \n ${note.text} \n \n by"+" "+getString(R.string.app_name)




        titleView = findViewById<TextView>(R.id.title)
        textView = findViewById<TextView>(R.id.text)
        EdittitleView = findViewById<TextView>(R.id.edit_title)
        EdittextView = findViewById<TextView>(R.id.edit_text)
        Editdate = findViewById<TextView>(R.id.edit_date)
        Createdate = findViewById<TextView>(R.id.create_date)
        notecolor=findViewById(R.id.notecolor)
        notestyle=findViewById(R.id.notestyle)
        notecolor.visibility= GONE
        notestyle.visibility= GONE
        Notedetailcolor=findViewById(R.id.note_detail_color)
        // Notedetailcolor.setBackgroundColor(resources.getColor(R.color.aboutcolor))


        Notedetailcolor.setBackgroundColor(Color.parseColor(note.color))

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


        titleView.text = note.title
        textView.text = note.text
        EdittitleView.setText(titleView.text)
        EdittextView.setText(textView.text)
        Editdate.text=note.editdate
        Createdate.text=note.createdate






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
        applyTextSize()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_note_detail, menu)



        //  val editnoteMenu = menu?.findItem(R.id.action_edit)


        return true
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                saveNote()
               // toast(getString(R.string.note_edited))
                return true
            }

            R.id.action_edit -> {
                Editnote()
                return true

            }



            R.id.action_share-> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                        Intent.EXTRA_TEXT, sendNote
                )
                sendIntent.type = "text/plain"
               startActivity(Intent.createChooser(sendIntent, getString(R.string.send_to)))

               // startActivity(sendIntent)
                return true
            }
            R.id.action_share_as_picture->{
                sendImage()
                return true
            }

                R.id.action_delete -> {
                DeleteNoteDialog()
                return true
            }

            R.id.action_small_text->{
                titleView.textSize=22f
                EdittitleView.textSize=22f
                textView.textSize=18f
                    EdittextView.textSize=18f
                sharedPreferences.edit().putInt(textSize,1).apply()



                return true
            }
            R.id.action_medium_text->{
                titleView.textSize=27f
                EdittitleView.textSize=27f
                textView.textSize=22f
                EdittextView.textSize=22f
                sharedPreferences.edit().putInt(textSize,2).apply()

                return true
            }
            R.id.action_large_text->{
                titleView.textSize=30f
                EdittitleView.textSize=30f
                textView.textSize=26f
                EdittextView.textSize=26f
                sharedPreferences.edit().putInt(textSize,3).apply()
                return true
            }




        }
        return super.onOptionsItemSelected(item)

    }






    private fun saveNote() {
        if (edit_title.text.isEmpty()  && edit_text.text.isEmpty()) {
            deleteNote()
            Log.i(TAG, "Note supprimer $note")


        }else if (cardColor != null){
            note.title = edit_title.text.toString()
            note.text = edit_text.text.toString()
            note.editdate = getString(R.string.edit_date) + " " + create_date + " " + getString(R.string.edit_at) + " " + getheure
            //cardColor = "#" + (ContextCompat.getColor(this, colorId!!))
            cardColor = "#" + Integer.toHexString(ContextCompat.getColor(this, colorId!!))
            note.color = cardColor
        }else {
            //note.title = titleView.text.toString()
            //note.text = textView.text.toString()
            note.title = edit_title.text.toString()
            note.text = edit_text.text.toString()
            note.editdate = getString(R.string.edit_date) + " " + create_date + "  " + getString(R.string.edit_at) + " " + getheure

        }


        intent = Intent(ACTION_SAVE)
        intent.putExtra(EXTRA_NOTE, note as Parcelable)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    private fun Editnote() {
       // note.title = EdittextView.text.toString()
       // note.text = EdittitleView.toString()
        //EdittitleView=note.text.toString()

        EdittextView.visibility= VISIBLE
        EdittitleView.visibility= VISIBLE

        notestyle.visibility= VISIBLE

        //notecolor.visibility= VISIBLE
        titleView.visibility= GONE
        textView.visibility= GONE
        Editdate.visibility= GONE
        Createdate.visibility= GONE

        toolbar.setTitle(getString(R.string.editnote_title))



        //startActivityForResult(intent, EditNoteActivity.REQUEST_EDIT_NOTE)
    }
    /*
        intent = Intent(ACTION_SAVE)
        intent.putExtra(EXTRA_NOTE, note as Parcelable)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()

     */
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
        //cardColor = "#"+(ContextCompat.getColor(this, colorId!!))
        cardColor = "#" + Integer.toHexString(ContextCompat.getColor(this, colorId!!))
        Notedetailcolor.setBackgroundColor(Color.parseColor(cardColor))

    }




    private fun deleteNote() {
        intent = Intent(ACTION_DELETE)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    fun Context.toast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    fun Modifier(view: View) {
       Editnote()


    }

    private fun DeleteNoteDialog(){
        val dialog= Dialog(this)
            dialog.apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
       setContentView(R.layout.exit_dialog)
       setCancelable(true)
        val exit=dialog.findViewById<TextView>(R.id.exit_msg)
        val confirm_btn=findViewById<Button>(R.id.confirm_btn)
        val cancel=findViewById<Button>(R.id.cancel_btn)
        exit.text=getString(R.string.delete_message)
        confirm_btn.setOnClickListener {
            deleteNote()
            dismiss()
        }
        cancel.setOnClickListener {
            dismiss()
        }
            show()


    }
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun style(view: View) {

    }

    override fun onSupportNavigateUp(): Boolean {
        saveNote()
        return true
    }

    override fun onBackPressed() {
        saveNote()
        super.onBackPressed()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
                R.anim.activity_fade_in_animation,
                R.anim.activity_fade_out_animation
        )
    }
    private fun applyStyle() {

        when (sharedPreferences.getInt(themeKey, 0)) {

            0 -> theme.applyStyle(R.style.Theme_JadoNotes, true)
            1 -> theme.applyStyle(R.style.Theme1, true)
            2 -> theme.applyStyle(R.style.Theme2, true)
            3 -> theme.applyStyle(R.style.Theme3, true)

        }


    }
    private fun applyTextSize(){
        when(sharedPreferences.getInt(textSize,1)){

            1->{
                titleView.textSize=22f
                EdittitleView.textSize=22f
                textView.textSize=18f
                EdittextView.textSize=18f

            }
            2->{
                titleView.textSize=27f
                EdittitleView.textSize=27f
                textView.textSize=22f
                EdittextView.textSize=22f


            }
            3->{
                titleView.textSize=30f
                EdittitleView.textSize=30f
                textView.textSize=26f
                EdittextView.textSize=26f

            }


        }


    }


    private fun sendImage(){
        val intent=Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(""))
        startActivity(Intent.createChooser(intent, "Share Image"))



    }

}


