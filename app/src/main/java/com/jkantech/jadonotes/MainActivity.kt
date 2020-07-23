package com.jkantech.jadonotes

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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
import com.jkantech.jadonotes.ui.adapters.NoteAdapter
import com.jkantech.jadonotes.ui.database.DBManagerCategory
import com.jkantech.jadonotes.ui.database.DBManagerNote
import com.jkantech.jadonotes.ui.models.Note
import com.jkantech.jadonotes.ui.utils.MyPreferences
import com.jkantech.jadonotes.ui.utils.toastMessage
import com.jkantech.jadonotes.ui.views.*
import com.kobakei.ratethisapp.RateThisApp
import kotlinx.android.synthetic.main.app_bar_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
/**
 * Created by Jonas Kaninda on 10/07/2020.
 */

class MainActivity : AppCompatActivity(),View.OnClickListener,View.OnLongClickListener, NavigationView.OnNavigationItemSelectedListener {

    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var adapter: NoteAdapter
    var notes: ArrayList<Note> = ArrayList()
    private var gridLayoutManager: GridLayoutManager? = null
    private lateinit var dbManager: DBManagerNote
    var  dbCategory = DBManagerCategory(this)
    lateinit var navigationView:NavigationView
    lateinit var note:Note


    var appTheme = 2
    lateinit var sharedPreferences: SharedPreferences
    private val themeKey = "currentTheme"
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    var doubleTap = false
    private val TAG = "msg"
    private val SPANCOUNT_KEY = "current"
    private var spanCount = 2
    var id: Int?=null
    lateinit var no_notes:LinearLayout

    private var recyclerView: RecyclerView? = null


    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(

                "ThemePref",
                Context.MODE_PRIVATE
        )

        applyStyle()

        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        title = ""

        // applyTheme()
        firstOpen()


        dbManager = DBManagerNote(this)
        this.notes = dbManager.getNotesList()
        test(true)
        adapter = NoteAdapter(this.notes, this, this)








        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val versionCode: Int = BuildConfig.VERSION_CODE
        val versionName: String = BuildConfig.VERSION_NAME
        no_notes=findViewById(R.id.no_notes)


        toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, 0, 0
        )



        drawer.addDrawerListener(toggle)
        toggle.syncState()


        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.menu.findItem(R.id.versionapp_nav).title =
            "Version: " + versionName













        coordinatorLayout = findViewById(R.id.coordinator_layout)

        val fab = findViewById<FloatingActionButton>(R.id.create_note_fab)
        fab.setOnClickListener {
            AddNote()
        }
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            getSpanCount()

        } else {
            spanCount = 3

        }
        rateApp()



        recyclerView = findViewById(R.id.notes_recycler_view)
        gridLayoutManager = GridLayoutManager(applicationContext, spanCount, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)

        // recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = adapter




        NbNotes()


        //Afficher Masquer le FloactingAction
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fab.visibility == VISIBLE) {
                    fab.hide()
                    //title=getString(R.string.app_name)

                } else if (dy < 0 && fab.visibility !== VISIBLE) {
                    title = " "
                    fab.show()


                }
            }

        })


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
        when(requestCode) {
            DeletedNoteActivity.REQUEST_RESTORE_NOTE-> {

                isAdded()
                NbNotes()

            }


        }

    }

    override fun onClick(view: View) {

        if (view.tag != null) {
            startEditNotesActivity(view.tag as Int)
        } else {
            when (view.id) {
                R.id.create_note_fab -> AddNote()
            }


        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchmenu = menu!!.findItem(R.id.action_search)
        if (searchmenu != null) {
            val searchView = searchmenu.actionView as SearchView
            searchView.queryHint=getString(R.string.searchhint)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                  //  adapter.filter.filter(newText)

                    return true
                }


            })


        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = (Intent(this, SettingsActivity::class.java))
                startActivityForResult(intent, REQUEST_STYLE)
                overridePendingTransition(
                        R.anim.activity_fade_in_animation,
                        R.anim.activity_fade_out_animation
                )
                return true
            }
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                overridePendingTransition(
                        R.anim.activity_fade_in_animation,
                        R.anim.activity_fade_out_animation
                )
                return true
            }
            R.id.action_share -> {
                shareApp()
                return true

            }
            R.id.action_contact -> {
                val intent = Intent(
                        Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "contact.jkantech@gmail.com", null)
                )
                val subject: String? = null
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                val message: String? = null
                intent.putExtra(Intent.EXTRA_TEXT, message)
                startActivity(Intent.createChooser(intent, getString(R.string.contact_me)))


                return true
            }
            R.id.action_view_as_list -> {
                return if (spanCount == 1) {
                    true
                } else {
                    sharedPreferences.edit().putInt(SPANCOUNT_KEY, 1).apply()
                    recreate()



                    true

                }
            }
            R.id.action_view_as_grid_2 -> {
                return if (spanCount == 2) {
                    true
                } else {
                    sharedPreferences.edit().putInt(SPANCOUNT_KEY, 2).apply()
                    recreate()


                    true

                }
            }
            R.id.action_view_as_grid_3 -> {
                return if (spanCount == 3) {
                    true
                } else {
                    sharedPreferences.edit().putInt(SPANCOUNT_KEY, 3).apply()
                    recreate()


                    true

                }
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
            R.id.nav_category -> {

                startActivity(Intent(this, CategoryActivity::class.java))
            }
            R.id.nav_deletednote->{
                val intent = (Intent(this, DeletedNoteActivity::class.java))
                startActivityForResult(intent, DeletedNoteActivity.REQUEST_RESTORE_NOTE)
                overridePendingTransition(
                    R.anim.activity_fade_in_animation,
                    R.anim.activity_fade_out_animation
                )
                return true

            }

            R.id.nav_settings -> {
                val intent = (Intent(this, SettingsActivity::class.java))
                startActivityForResult(intent, REQUEST_STYLE)
                overridePendingTransition(
                        R.anim.activity_fade_in_animation,
                        R.anim.activity_fade_out_animation
                )
                return true
            }
            R.id.nav_backup -> {
                //toast(getString(R.string.backup_note_msg))
                startActivity(Intent(this, BackupActivity::class.java))
                overridePendingTransition(
                        R.anim.activity_fade_in_animation,
                        R.anim.activity_fade_out_animation
                )
                return true
            }
            R.id.nav_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                overridePendingTransition(
                        R.anim.activity_fade_in_animation,
                        R.anim.activity_fade_out_animation
                )
                return true
            }
            R.id.nav_share -> {
                shareApp()
                return true


            }
            R.id.nav_rateapp -> {

                try {
                    startActivity(
                            Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=$packageName")
                            )
                    )
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                            Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                            )
                    )


                }

            }


        }
        return true
    }

    fun processEditNoteResult(data: Intent) {
        val noteIndex = data.getIntExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, -1)

        when (data.action) {
            NoteDetailActivity.ACTION_SAVE -> {
                val note = data.getParcelableExtra<Note>(NoteDetailActivity.EXTRA_NOTE)
                saveNote(note!!, noteIndex)
                NbNotes()
            }
            NoteDetailActivity.ACTION_DELETE -> {
                deleteNote(noteIndex)
                //Appel de la fonction de Comptage de notes
                NbNotes()
            }
        }

        adapter.notifyDataSetChanged()

    }


    fun saveNote(note: Note, noteIndex: Int) {

        if (noteIndex < 0) {
            this.notes.add(0, note)
            isAdded()


        } else {
            this.notes[noteIndex] = note


        }
        adapter.notifyDataSetChanged()

    }

    fun deleteNote(noteIndex: Int) {

        if (noteIndex < 0) {
            return
        }
        val note = this.notes.removeAt(noteIndex)

        adapter.notifyDataSetChanged()
        Snackbar.make(coordinatorLayout, "${note.title}" + " " + getString(R.string.note_deleted), Snackbar.LENGTH_SHORT).show()


    }


    /*
    *
    * Ajout de la note
     */

    fun AddNote() {
        startAddNotesActivity(-1)
    }

    fun startAddNotesActivity(noteIndex: Int) {
        val note = if (noteIndex < 0) Note() else this.notes[noteIndex]

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
        val note = if (noteIndex < 0) Note() else this.notes[noteIndex]

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

    /*
    private fun toast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

     */
    @SuppressLint("SetTextI18n")
    private fun NbNotes() {
        val getCategory=dbCategory.getListOfCategory()
        val getdeletedNote=dbManager.getDeletedNotesList()


        val nb = resources.getQuantityString(R.plurals.nb_notes, this.notes.size)
        nb_notes.text = this.notes.size.toString() + " " + nb
        navigationView.menu.findItem(R.id.nav_all_note).title =getString(R.string.all_notes)+"             " + notes.size
        navigationView.menu.findItem(R.id.nav_category).title =getString(R.string.all_category)+"            " + getCategory.size
        navigationView.menu.findItem(R.id.nav_deletednote).title =getString(R.string.trash)+"            " + getdeletedNote.size





        if (this.notes.isEmpty()) {
            no_notes.visibility= VISIBLE
        }else{
            no_notes.visibility= GONE
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

    private fun applyStyle() {

        when (sharedPreferences.getInt(themeKey, 0)) {

            0 -> theme.applyStyle(R.style.Theme_JadoNotes, true)
            1 -> theme.applyStyle(R.style.Theme1, true)
            2 -> theme.applyStyle(R.style.Theme2, true)
            3 -> theme.applyStyle(R.style.Theme3, true)

        }
    }


    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            if (doubleTap) {
                super.onBackPressed()
                return
            }

            this.doubleTap = true
            toastMessage(this, getString(R.string.press_again_to_exit))
            Handler().postDelayed({ doubleTap = false }, 2000)
        }
    }

    private fun shareApp() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_msg))
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, getString(R.string.send_to)))
    }

    private fun getSpanCount() {
        when (sharedPreferences.getInt(SPANCOUNT_KEY, 2)) {
            1 -> {
                spanCount = 1


            }
            2 -> {
                spanCount = 2

            }

            3 -> {
                spanCount = 3


            }


        }

    }

    private fun rateApp() {
        // Custom condition: 3 days and 2 launches
        val config = RateThisApp.Config(3, 4)
        RateThisApp.init(config)

        // Monitor launch times and interval from installation
        RateThisApp.onCreate(this)
        // If the condition is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(this)
        RateThisApp.setCallback(object : RateThisApp.Callback {
            override fun onNoClicked() {

                return
            }

            override fun onYesClicked() {
                startActivity(
                        Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                        )
                )
            }

            override fun onCancelClicked() {
                return
            }


        })
    }




    private fun isAdded() {
        adapter.clearAdapter()

        this.notes = dbManager.getNotesList()
        adapter = NoteAdapter(this.notes, this, this)
        recyclerView!!.adapter = adapter
        adapter.notifyDataSetChanged()


    }

    private fun test(teste: Boolean = true) {
        if (teste == true) {
            this.notes.sortBy {
                it.color
            }
        } else {
            this.notes.sortBy {
                it.id

            }

        }

    }


    fun deleteNotePopMenu(noteIndex: Int) {

        if (noteIndex < 0) {
            return
        }
        //val note = this.notes.removeAt(noteIndex)
        val note = this.notes.removeAt(noteIndex)
        dbManager.update(note.id!!,note.title!!,note.text!!,note.category!!,note.editdate!!,note.createdate!!,note.color!!,
            note.text_size,1)
        Snackbar.make(coordinatorLayout, "${note.title}" + " " + getString(R.string.note_deleted), Snackbar.LENGTH_SHORT).show()


        adapter.notifyDataSetChanged()


    }
    private fun DeleteNoteDialog(v: View?){
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
                deleteNotePopMenu(v!!.tag as Int)
                NbNotes()
                isAdded()



                dismiss()
            }
            cancel.setOnClickListener {
                dismiss()
            }
            show()


        }
    }

    override fun onLongClick(v: View?): Boolean {
        if (v!!.tag != null) {
            val popupMenu = PopupMenu(this,v)
            popupMenu.inflate(R.menu.note_detail_popmenu)
            popupMenu.menu.getItem(0).title = getString(R.string.note_detail)
            popupMenu.menu.getItem(1).title = getString(R.string.edit_note)
            popupMenu.menu.getItem(2).title = getString(R.string.delete_note)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.pop_detail -> {
                        startEditNotesActivity(v.tag as Int)



                        return@setOnMenuItemClickListener true
                    }
                    R.id.pop_edit -> {
                        startEditNotesActivity(v.tag as Int)

                        return@setOnMenuItemClickListener true
                    }

                    R.id.pop_delete -> {
                       DeleteNoteDialog(v)
                        return@setOnMenuItemClickListener true
                    }



                }
                false
            }
            popupMenu.show()
        }



return true
    }


    private fun firstOpen(){

        val prefs =
            getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)
        if (firstStart) {
            addCategory()

        }
    }




    private fun addCategory(){
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
        val Editdate = getString(R.string.create_date) + " " + create_date + " " + getString(R.string.edit_at) + " " + getheure




         dbCategory = DBManagerCategory(this)
        val getCategory=dbCategory.getListOfCategory()

        if (getCategory.isEmpty()) {
            dbCategory.insert(getString(R.string.cat_personal))
            dbCategory.insert(getString(R.string.cat_business))
            dbCategory.insert(getString(R.string.cat_insurance))
            dbCategory.insert(getString(R.string.cat_school))
            dbCategory.insert(getString(R.string.cat_work))
        }
        dbManager = DBManagerNote(this)
        if (notes.isEmpty()) {
            dbManager.insert(
                getString(R.string.notemodeletitre),
                getString(R.string.notemodeleText),
                getString(R.string.cat_business),
                Editdate,
                Createddate,
                "#ffffff",
                1,
                0
            )

        }

        val prefs =
            getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("firstStart", false)
        editor.apply()



    }


    companion object {

        private val REQUEST_STYLE = 0
        private val REQUEST_RESTORE = 0

        private val REQUEST_EDIT = 0

    }
}




