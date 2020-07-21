package com.jkantech.jadonotes.ui.database

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.jkantech.jadonotes.ui.databasehelper.DatabaseHelper
import com.jkantech.jadonotes.ui.models.Note
import com.jkantech.jadonotes.ui.utils.*


class DBManagerNote(val context: Context) {

    lateinit var dbHelper: DatabaseHelper
    lateinit var database: SQLiteDatabase

    @Throws(SQLException::class)
    fun open(): DBManagerNote {
        dbHelper = DatabaseHelper(context)
        database = dbHelper.writableDatabase
        return this
    }

    private fun close() {
        dbHelper.close()
    }

    /**
     * insert value in task table
     */
    fun insert(title: String, text: String, category: String,  editedate: String ,createdate: String ,notecolor:String,text_size:Int) {
        open()

        val contentValues = ContentValues()
        contentValues.put(NOTE_TITLE, title)
        contentValues.put(NOTE_TEXT, text)
        contentValues.put(NOTE_CATEGORY, category)
        contentValues.put(EDIT_DATE, editedate)
        contentValues.put(CREATE_DATE, createdate)
        contentValues.put(NOTE_COLOR, notecolor)
        contentValues.put(TEXT_SIZE,text_size)




        database.insert(TABLE_NOTES, null, contentValues)
        close()
    }

    /**
     * update value in task table
     */
    fun update(id: Int, title: String, text: String, category: String, editedate: String, createdate: String, notecolor:String,text_size: Int) {
        open()

        val contentValues = ContentValues()

        contentValues.put(NOTE_TITLE, title)
        contentValues.put(NOTE_TEXT, text)
        contentValues.put(NOTE_CATEGORY, category)
        contentValues.put(EDIT_DATE, editedate)
        contentValues.put(CREATE_DATE, createdate)
        contentValues.put(NOTE_COLOR, notecolor)
        contentValues.put(TEXT_SIZE,text_size)


        database.update(TABLE_NOTES, contentValues, ID + " = " + id, null)
        close()
    }


    /**
     * delete row in Note table
     */
    fun delete(id: Int) {
        open()
        database.delete(TABLE_NOTES, ID + "=" + id, null)
        close()
    }

    /**
     * get Notes list from Note table
     */


    fun getNotesList(): ArrayList<Note> {

        open()

        val notes = ArrayList<Note>()

        val query = "SELECT * FROM " + TABLE_NOTES
         database.rawQuery(query, null).use {cursor->
             while (cursor.moveToNext()) {


                 val note = Note(
                       //  user.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                         cursor.getInt(cursor.getColumnIndex(ID)),
                         cursor.getString(cursor.getColumnIndex(NOTE_TITLE)),
                         cursor.getString(cursor.getColumnIndex(NOTE_TEXT)),
                         cursor.getString(cursor.getColumnIndex(NOTE_CATEGORY)),
                         cursor.getString(cursor.getColumnIndex(EDIT_DATE)),
                         cursor.getString(cursor.getColumnIndex(CREATE_DATE)),
                         cursor.getString(cursor.getColumnIndex(NOTE_COLOR)),
                         cursor.getInt(cursor.getColumnIndex(TEXT_SIZE))



                 )

                 notes.add(note)


             }

        }
        return notes
    }






}