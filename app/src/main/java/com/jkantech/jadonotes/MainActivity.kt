package com.jkantech.jadonotes

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.jkantech.jadonotes.ui.views.AddNotesActivity
import com.jkantech.jadonotes.ui.models.Note
import com.jkantech.jadonotes.ui.adapters.NoteAdapter
import com.jkantech.jadonotes.ui.utils.MyPreferences
import com.jkantech.jadonotes.ui.utils.loadNotes
import com.jkantech.jadonotes.ui.utils.persistNote
import com.jkantech.jadonotes.ui.views.AboutActivity
import com.jkantech.jadonotes.ui.views.NoteDetailActivity
import com.jkantech.jadonotes.ui.views.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList




class MainActivity : AppCompatActivity(),View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var adapter: NoteAdapter
    lateinit var notes: ArrayList<Note>
    private var gridLayoutManager: GridLayoutManager?=null
    // lateinit var contextMenu:Context
    var appTheme = 2
    lateinit var sharedPreferences: SharedPreferences
    private val themeKey = "currentTheme"
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
                "ThemePref",
                Context.MODE_PRIVATE
        )
        applyTheme()
        applyStyle()



        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        title=""

        notes = loadNotes(this)

        adapter = NoteAdapter(notes, this)




        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout

        val appversion = drawer.findViewById<TextView>(R.id.versionapp_nav)
        val versionCode: Int = BuildConfig.VERSION_CODE
        val versionName: String = BuildConfig.VERSION_NAME





        toggle = ActionBarDrawerToggle(
                this, drawer,toolbar, 0, 0
        )



        drawer.addDrawerListener(toggle)
        toggle.syncState()


        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        /*
*   Reccuperation de la date actuelle
*/
        val date = Calendar.getInstance().time
        val heures = Date().hours
        val minutes = Date().minutes
        @SuppressLint("SimpleDateFormat")
        val formatter = SimpleDateFormat(getString(R.string.date_format))
        val edit_date = formatter.format(date).toString()
        val create_date = formatter.format(date).toString()
        val getheure = heures.toString() + ":" + minutes
        val Createddate = getString(R.string.create_date) + " " + create_date + " " + getString(R.string.edit_at) + " " + getheure
        val Editdate = getString(R.string.create_date) + " "  + create_date+" " + getString(R.string.edit_at) + " " + getheure



        //val edit_date ="Modifié le "+ formatter.format(date).toString()+" "+"à"+" "+heures+":"+minutes
       // val create_date ="Crée le "+ formatter.format(date).toString()+" " +"à" + " "+heures+":"+minutes




        //suppression de la note si la liste de de note n'est pas vide


        if (notes.isNotEmpty()) {
            loadNotes(this)
            notes.remove(
                    Note(
                            getString(R.string.notemodeletitre),
                            getString(R.string.notemodeleText),
                            Editdate,Createddate
                    )
            )


        } else {
            //Ajout de note si la liste de note est vide

            notes.add(
                    Note(
                            getString(R.string.notemodeletitre),
                            getString(R.string.notemodeleText),
                            Editdate,Createddate
                    )
            )


        }




        coordinatorLayout = findViewById<CoordinatorLayout>(R.id.coordinator_layout)
        // val addnote findViewById(R.id.create_note_fab).setOnClickListener(this)

        val fab = findViewById<FloatingActionButton>(R.id.create_note_fab)
        fab.setOnClickListener {
            AddNote()
        }


        val recyclerView = findViewById<RecyclerView>(R.id.notes_recycler_view)
        gridLayoutManager= GridLayoutManager(applicationContext,2,LinearLayoutManager.VERTICAL,false)
        recyclerView?.layoutManager=gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        // recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        NbNotes()

        //Afficher Masquer le FloactingAction
        recyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fab.visibility == VISIBLE) {
                    fab.hide()
                    //title=getString(R.string.app_name)

                } else if (dy < 0 && fab.visibility !== VISIBLE) {
                    title=" "
                    fab.show()


                }
            }

        })
        /*
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

             //  adapter.getFilter().filter(charSequence.toString())

                adapter.filter.filter(charSequence)

            }

            override fun afterTextChanged(editable: Editable) {}
        })


         */

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }
        when (requestCode) {
            NoteDetailActivity.REQUEST_EDIT_NOTE -> processEditNoteResult(data)
        }
        if (requestCode == REQUEST_STYLE) {
            if (resultCode == Activity.RESULT_OK) {
                applyStyle()
                finish()

            }
        }
    }
    override fun onClick(view: View) {
        if (view.tag != null) {
            // startAddNotesActivity(view.tag as Int)
            startEditNotesActivity(view.tag as Int)
        }else{
            when (view.id) {
                R.id.create_note_fab -> AddNote()
            }

        }



    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        //val search = menu.findItem(R.id.action_search)

        //val search=menu.findItem(R.id.action_search)
        //  val searchView = MenuItemCompat.getActionView(search) as SearchView
        //   searchView.queryHint="Recherche..."
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings->{
                val intent = (Intent(this, SettingsActivity::class.java))
                startActivityForResult(intent, REQUEST_STYLE)
                overridePendingTransition(
                        R.anim.activity_fade_in_animation,
                        R.anim.activity_fade_out_animation
                )
                return true
            }
            R.id.action_about->{
                startActivity(Intent(this,AboutActivity::class.java))
                overridePendingTransition(
                        R.anim.activity_fade_in_animation,
                        R.anim.activity_fade_out_animation
                )
            }
            R.id.action_share->{
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                        Intent.EXTRA_TEXT,""
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)



            }
            R.id.action_edit->{




            }




        }
        return super.onOptionsItemSelected(item)
    }







    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer.closeDrawer(GravityCompat.START)

        when (item.itemId) {
            R.id.nav_add_note -> {
                AddNote()
                return true
            }
            R.id.nav_settings->{
                val intent = (Intent(this, SettingsActivity::class.java))
                startActivityForResult(intent, REQUEST_STYLE)
                overridePendingTransition(
                        R.anim.activity_fade_in_animation,
                        R.anim.activity_fade_out_animation
                )
                        return true
            }
            R.id.nav_backup->{
                toast(getString(R.string.backup_note_msg))
                return true
            }
            R.id.nav_about->{
                startActivity(Intent(this,AboutActivity::class.java))
                overridePendingTransition(
                    R.anim.activity_fade_in_animation,
                    R.anim.activity_fade_out_animation
                )
                return true
            }
            R.id.nav_share->{


                return true



            }


        }
        return true
    }


    private fun close() {
        drawer.closeDrawer(GravityCompat.START)


    }

    fun processEditNoteResult(data: Intent) {
        val noteIndex = data.getIntExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, -1)

        when(data.action) {
            NoteDetailActivity.ACTION_SAVE -> {
                val note = data.getParcelableExtra<Note>(NoteDetailActivity.EXTRA_NOTE)
                saveNote(note, noteIndex)
                NbNotes()
            }
            NoteDetailActivity.ACTION_DELETE -> {
                deleteNote(noteIndex)
                //Appel de la fonction de Comptage de notes
                NbNotes()
            }
        }
    }

    fun saveNote(note: Note, noteIndex: Int) {
        persistNote(this, note)
        if (noteIndex < 0) {
            notes.add(0, note)
        } else {
            notes[noteIndex] = note
        }
        adapter.notifyDataSetChanged()
    }

    fun deleteNote(noteIndex: Int) {

        if (noteIndex <0) {
            return
        }
        val note = notes.removeAt(noteIndex)
        com.jkantech.jadonotes.ui.utils.deleteNote(this, note)
        adapter.notifyDataSetChanged()

        Snackbar.make(coordinatorLayout, "${note.title}" +" "+getString(R.string.note_deleted), Snackbar.LENGTH_SHORT).show()

    }



    /*
    *
    * Ajout de la note
     */

    fun AddNote() {
        startAddNotesActivity(-1)
    }

    fun startAddNotesActivity(noteIndex: Int) {
        val note = if (noteIndex < 0) Note() else notes[noteIndex]

        val intent = Intent(this, AddNotesActivity::class.java)
        intent.putExtra(AddNotesActivity.EXTRA_NOTE, note as Parcelable)
        intent.putExtra(AddNotesActivity.EXTRA_NOTE_INDEX, noteIndex)
        startActivityForResult(intent, AddNotesActivity.REQUEST_EDIT_NOTE)
        overridePendingTransition(
                R.anim.activity_fade_in_animation,
                R.anim.activity_fade_out_animation
        )
    }
    /*
    *
    *  Edition de la Note
    *
     */
    fun EditNote() {
        startEditNotesActivity(-1)
    }

    fun startEditNotesActivity(noteIndex: Int) {
        val note = if (noteIndex < 0) Note() else notes[noteIndex]

        val intent = Intent(this, NoteDetailActivity::class.java)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE, note as Parcelable)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, noteIndex)
        startActivityForResult(intent, NoteDetailActivity.REQUEST_EDIT_NOTE)
        overridePendingTransition(
                R.anim.activity_fade_in_animation,
                R.anim.activity_fade_out_animation
        )
    }
    private fun recherche(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // if (adapter != null) adapter!!.filter.filter(newText)
                return true
            }
        })
    }
    private fun toast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
    @SuppressLint("SetTextI18n")
    private fun NbNotes(){
        val nb=resources.getQuantityString(R.plurals.nb_notes,notes.size)
        nb_notes.text= notes.size.toString()+" "+nb

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
    private fun applyStyle() {

        when (sharedPreferences.getInt(themeKey, 0)) {

            0 -> theme.applyStyle(R.style.Theme_JadoNotes, true)
            1 -> theme.applyStyle(R.style.Theme1, true)
            2 -> theme.applyStyle(R.style.Theme2, true)
            3 -> theme.applyStyle(R.style.Theme3, true)

        }


    }

    override fun onBackPressed() {
        finish()

        super.onBackPressed()
    }
    companion object {

       private val REQUEST_STYLE = 0

    }



}
