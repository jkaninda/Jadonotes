package com.jkantech.jadonotes.ui.views

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jkantech.jadonotes.R
import com.jkantech.jadonotes.ui.adapters.NoteAdapter
import com.jkantech.jadonotes.ui.database.DBManagerNote
import com.jkantech.jadonotes.ui.models.Note
import kotlinx.android.synthetic.main.activity_deleted_note.*
import kotlinx.android.synthetic.main.containt_deleted_note.*
/**
 * Created by Jonas Kaninda on 10/07/2020.
 */
class FavoritesNoteActivity : AppCompatActivity(),View.OnClickListener,View.OnLongClickListener {
    lateinit var note: Note
    var dbManager = DBManagerNote(this)
    var favnotes: ArrayList<Note> = ArrayList()
    lateinit var adapter:NoteAdapter
    lateinit var recyclerView:RecyclerView
    lateinit var gridLayoutManager:GridLayoutManager
    lateinit var sharedPreferences: SharedPreferences
    private val themeKey = "currentTheme"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
            "ThemePref",
            Context.MODE_PRIVATE
        )
        applyStyle()
        setContentView(R.layout.activity_deleted_note)
        setSupportActionBar(toolbar)
        title=(getString(R.string.favorites))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        dbManager = DBManagerNote(this)
        this.favnotes = dbManager.getFavNotesList()
        adapter = NoteAdapter(this.favnotes,this,this)

        checkView()




        recyclerView = findViewById(R.id.recyclerViewdeletednote)
        gridLayoutManager = GridLayoutManager(applicationContext, 2, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = adapter
    }

    override fun onClick(v: View?) {
        showPopMenu(v!!,v.tag as Int)
    }

    override fun onLongClick(v: View?): Boolean {
        showPopMenu(v!!,v.tag as Int)
        return true
    }
    private fun DeleteNoteDialog(v: View?) {
        val dialog = Dialog(this)
        dialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.exit_dialog)
            setCancelable(true)
            val exit = dialog.findViewById<TextView>(R.id.exit_msg)
            val confirm_btn = findViewById<Button>(R.id.confirm_btn)
            val cancel = findViewById<Button>(R.id.cancel_btn)
            exit.text = getString(R.string.delete_message)
            confirm_btn.setOnClickListener {

                deleteNote(v!!.tag as Int)

                dismiss()
            }
            cancel.setOnClickListener {
                dismiss()
            }
            show()


        }
    }



    fun deleteNote(noteIndex: Int) {

        if (noteIndex < 0) {
            return
        }
        val note = this.favnotes.removeAt(noteIndex)
        dbManager.update(note.id!!,note.title!!,note.text!!,note.category!!,note.editdate!!,note.createdate!!,note.color!!,
                note.text_size,1,0,0)


        adapter.notifyDataSetChanged()

        adapter.notifyDataSetChanged()
        checkView()


    }
    fun RemoveFromFavNote(noteIndex: Int) {

        if (noteIndex < 0) {
            return
        }
        val note = this.favnotes.removeAt(noteIndex)
        dbManager.update(note.id!!,note.title!!,note.text!!,note.category!!,note.editdate!!,note.createdate!!,note.color!!,
            note.text_size,0,0,0)

        adapter.notifyDataSetChanged()
        checkView()



    }
    fun checkView(){

        if (favnotes.isEmpty()) {
            txtNoDeletednotes.text=getString(R.string.pas_de_notes)
            txtNoDeletednotes.visibility= View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        actionRestore()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        actionRestore()
        super.onBackPressed()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            R.anim.activity_fade_in_animation,
            R.anim.activity_fade_out_animation
        )
    }
fun actionRestore(){
    intent = Intent(ACTION_RESTORE)
    setResult(Activity.RESULT_OK, intent)
    finish()
}
    private fun showPopMenu(view:View,id:Int){
        val note = this.favnotes[id]

        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.note_fav_popmenu)
        popupMenu.menu.getItem(0).title = getString(R.string.note_detail)
        popupMenu.menu.getItem(1).title = getString(R.string.remove_to_fav)

        popupMenu.menu.getItem(2).title = getString(R.string.delete_note)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.pop_detail -> {
                   startEditNotesActivity(view.tag as Int)



                    return@setOnMenuItemClickListener true
                }
                R.id.pop_add_to_fav->{
                    RemoveFromFavNote(view.tag as Int)
                    checkView()


                }

                R.id.pop_delete -> {
                    DeleteNoteDialog(view)
                    return@setOnMenuItemClickListener true
                }


            }
            false
        }
        popupMenu.show()

    }
    fun startEditNotesActivity(noteIndex: Int) {
        val note = if (noteIndex < 0) Note() else this.favnotes[noteIndex]

        val intent = Intent(this, NoteDetailActivity::class.java)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE, note as Parcelable)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, noteIndex)
        startActivityForResult(intent, NoteDetailActivity.REQUEST_EDIT_NOTE)
        overridePendingTransition(
                R.anim.activity_fade_in_animation,
                R.anim.activity_fade_out_animation
        )
        finish()
    }


    private fun applyStyle() {

        when (sharedPreferences.getInt(themeKey, 0)) {

            0 -> theme.applyStyle(R.style.Theme_JadoNotes, true)
            1 -> theme.applyStyle(R.style.Theme1, true)
            2 -> theme.applyStyle(R.style.Theme2, true)
            3 -> theme.applyStyle(R.style.Theme3, true)

        }
    }


    companion object {
        val REQUEST_RESTORE_NOTE = 1
        val ACTION_RESTORE = "com.jkantech.jadonotes.actions.ACTION_RESTORE"
        val REQUEST_EDIT_NOTE = 1



    }

}