package com.jkantech.jadonotes.ui.utils

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.jkantech.jadonotes.ui.models.Note
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*
import kotlin.collections.ArrayList


private const val TAG = "storage"

fun persistNote(context: Context, note: Note) : Boolean {
    val saved = true

    if (TextUtils.isEmpty(note.filename)) {
        note.filename = UUID.randomUUID().toString() + ".note"
    }
    Log.i(TAG, "Sauvegarde de note $note")
    val fileOutput = context.openFileOutput(note.filename, Context.MODE_APPEND)
    val outputStream = ObjectOutputStream(fileOutput)
    outputStream.writeObject(note)

    return saved
}

fun loadNotes(context: Context) : ArrayList<Note> {
    val notes = ArrayList<Note>()
    Log.i(TAG, "Chargemets de notes...")

    val notesDir = context.filesDir
    for(filename in notesDir.list()) {
        val note = loadNote(context, filename)
        Log.i(TAG, "Chargement de note $note")
        notes.add(note)
    }
    return notes
}



fun deleteNote(context: Context, note: Note): Boolean {
    return context.deleteFile(note.filename)
}
private fun loadNote(context: Context, filename: String) : Note {
    val fileInput = context.openFileInput(filename)
    val inputStream = ObjectInputStream(fileInput)
    val note = inputStream.readObject() as Note
    return note
}

