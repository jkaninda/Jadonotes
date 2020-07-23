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
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jkantech.jadonotes.R
import com.jkantech.jadonotes.ui.adapters.NoteAdapter
import com.jkantech.jadonotes.ui.database.DBManagerNote
import com.jkantech.jadonotes.ui.models.Note
import kotlinx.android.synthetic.main.activity_deleted_note.*
import kotlinx.android.synthetic.main.containt_deleted_note.*
/**
 * Created by Jonas Kaninda on 10/07/2020.
 */
class DeletedNoteActivity : AppCompatActivity(),View.OnClickListener,View.OnLongClickListener {
    lateinit var note: Note
    var dbManager = DBManagerNote(this)
    var deletednotes: ArrayList<Note> = ArrayList()
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
        title=(getString(R.string.trash))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        dbManager = DBManagerNote(this)
        this.deletednotes = dbManager.getDeletedNotesList()
        adapter = NoteAdapter(this.deletednotes,this,this)

checkView()




        recyclerView = findViewById(R.id.recyclerViewdeletednote)
        gridLayoutManager = GridLayoutManager(applicationContext, 2, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)

        // recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onClick(v: View?) {
        showPopMenu(v)
    }

    override fun onLongClick(v: View?): Boolean {
        showPopMenu(v)
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
            exit.text = getString(R.string.delete_forever)
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
    private fun RestoreNoteDialog(v: View?) {
        val dialog = Dialog(this)
        dialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.exit_dialog)
            setCancelable(true)
            val exit = dialog.findViewById<TextView>(R.id.exit_msg)
            val confirm_btn = findViewById<Button>(R.id.confirm_btn)
            val cancel = findViewById<Button>(R.id.cancel_btn)
            exit.text = getString(R.string.restore_message)
            confirm_btn.setOnClickListener {
                restoreNote(v!!.tag as Int)





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
        //val note = this.notes.removeAt(noteIndex)
        val note = this.deletednotes.removeAt(noteIndex)
        note.id?.let { dbManager.delete(it) }

        adapter.notifyDataSetChanged()
        checkView()


    }
    fun restoreNote(noteIndex: Int) {

        if (noteIndex < 0) {
            return
        }
        val note = this.deletednotes.removeAt(noteIndex)
        dbManager.update(note.id!!,note.title!!,note.text!!,note.category!!,note.editdate!!,note.createdate!!,note.color!!,
            note.text_size,0)

        adapter.notifyDataSetChanged()
        checkView()



    }
    fun checkView(){

        if (deletednotes.isEmpty()) {
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
fun showPopMenu(v: View?){
    if (v!!.tag != null) {
        val popupMenu = PopupMenu(this,v)
        popupMenu.inflate(R.menu.menu_restore)
        popupMenu.menu.getItem(0).title = getString(R.string.restore)
        popupMenu.menu.getItem(1).title = getString(R.string.delete_note)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.pop_restore -> {
                    RestoreNoteDialog(v)



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


    }

}