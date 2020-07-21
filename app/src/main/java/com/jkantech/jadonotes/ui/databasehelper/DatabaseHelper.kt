package com.jkantech.jadonotes.ui.databasehelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.jkantech.jadonotes.R
import com.jkantech.jadonotes.ui.utils.*
import com.kobakei.ratethisapp.RateThisApp.onCreate


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {



    /**************** Category ****************/


    private val CREATE_CATEGORY_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY + "(" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CATEGORY_NAME + " TEXT); "

    private val DROP_CATEGORY_TABLE = "DROP TABLE IF EXISTS " + TABLE_CATEGORY

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
                    TEXT_SIZE + " INTEGER); "

    private val DROP_NOTES_TABLE = "DROP TABLE IF EXISTS " + TABLE_NOTES



    /**************** Category ****************/


    private val CREATE_TASK_TABLE =
        "CREATE TABLE IF NOT EXISTS " + TABLE_TASK + "(" +
    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
    TASK_DETAILS + " TEXT, " +
    NOTIFY_TIME_MILL + " TEXT, " +
    TASK_COMPLETED + " INTEGER); "

    private val DROP_TASK_TABLE = "DROP TABLE IF EXISTS " + TABLE_TASK





    override fun onCreate(db: SQLiteDatabase?) {

        db!!.execSQL(CREATE_CATEGORY_TABLE)
        db.execSQL(CREATE_NOTES_TABLE)
        db.execSQL(CREATE_TASK_TABLE)



    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onCreate(db)
        }



}