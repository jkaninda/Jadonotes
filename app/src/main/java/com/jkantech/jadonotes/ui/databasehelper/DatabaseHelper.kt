package com.jkantech.jadonotes.ui.databasehelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.jkantech.jadonotes.ui.models.Note
import com.jkantech.jadonotes.ui.utils.*



class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {



    /**************** Category ****************/


    private val CREATE_CATEGORY_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY + "(" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CATEGORY_NAME + " TEXT); "

    //private val DROP_CATEGORY_TABLE = "DROP TABLE IF EXISTS " + TABLE_CATEGORY

    /****************** Note ******************/

    private val CREATE_NOTES_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NOTES + "(" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOTE_TITLE + " TEXT, " +
                    NOTE_TEXT + " TEXT, " +
                    NOTE_CATEGORY + " TEXT, " +
                    EDIT_DATE + " TEXT, " +
                    CREATE_DATE + " TEXT, "+
                    NOTE_COLOR + " TEXT,"+
                    TEXT_SIZE + " );"



   // private val DROP_NOTES_TABLE = "DROP TABLE IF EXISTS " + TABLE_NOTES



    override fun onCreate(db: SQLiteDatabase?) {

        db!!.execSQL(CREATE_CATEGORY_TABLE)
        db.execSQL(CREATE_NOTES_TABLE)

        val cv = ContentValues()
        cv.put(CATEGORY_NAME, "Personal")
        db.insert(TABLE_CATEGORY, null, cv)

        val cv1 = ContentValues()
        cv1.put(CATEGORY_NAME, "Business")
        db.insert(TABLE_CATEGORY, null, cv1)

        val cv2 = ContentValues()
        cv2.put(CATEGORY_NAME, "Insurance")
        db.insert(TABLE_CATEGORY, null, cv2)

        val cv3 = ContentValues()
        cv3.put(CATEGORY_NAME, "Shopping")
        db.insert(TABLE_CATEGORY, null, cv3)

        val cv4 = ContentValues()
        cv4.put(CATEGORY_NAME, "Banking")
        db.insert(TABLE_CATEGORY, null, cv4)
        insertDefaultNotes()

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY)
        onCreate(db)

    }

    private fun insertDefaultNotes(){

    }


}