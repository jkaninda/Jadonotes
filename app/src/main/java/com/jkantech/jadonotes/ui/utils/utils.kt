package com.jkantech.jadonotes.ui.utils

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.jkantech.jadonotes.R
import com.jkantech.jadonotes.ui.database.DBManagerCategory


    fun dialogUpdateCategory(context: Context, id: Int) {

        val dbManager = DBManagerCategory(context)
        val catName = dbManager.getCategoryName(id)

        val li = LayoutInflater.from(context)
        val promptsView = li.inflate(R.layout.alert_dialog_update_category, null)

        val alert = AlertDialog.Builder(context)
        alert.setView(promptsView)

        val input: EditText = promptsView.findViewById(R.id.edtUpdateCat) as EditText

        input.setText(catName)
        input.setSelection(input.text.length)

        alert.setPositiveButton(R.string.update) { _, _ -> }

        alert.setNegativeButton(R.string.cancel) { _, _ -> }
        val alertDialog = alert.create()

        alertDialog.setOnShowListener {

            val button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {

                val cat: String = input.text.toString().trim()

                Log.e(ContentValues.TAG, "Category : " + cat)

                if (cat != "") {
                    if (cat != catName) {

                        dbManager.update(id, cat)

                        val mArrayList = dbManager.getCategoryList()

                        alertDialog.dismiss()
                    } else {
                        toastMessage(context, "Modifiaction")
                    }
                } else {
                    toastMessage(context, "Veillez entrer pour modifier")
                }
            }
        }

        alertDialog.show()
    }

    fun dialogDeleteCategory(context: Context, id: Int) {

        val dbManager = DBManagerCategory(context)

        val alert = AlertDialog.Builder(context)
        alert.setTitle("Delete Category")
        alert.setMessage("Do you want to delete this category?")

        alert.setPositiveButton(R.string.delete_note, { _, _ -> })

        alert.setNegativeButton(R.string.cancel, { _, _ -> })
        val alertDialog = alert.create()

        //val categoryDeleted = categoryDelete

        alertDialog.setOnShowListener {

            val button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {

                dbManager.delete(id)

                // val mArrayList = dbManager.getCategoryList()
                //categoryDeleted.isCategoryDeleted(true, mArrayList)

                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }


    fun delete(context: Context, id: Int) {
        val dbManager = DBManagerCategory(context)
        dbManager.delete(id)

    }


    fun dialogAddCategory1(context: Context) {

        val li = LayoutInflater.from(context)
        val promptsView = li.inflate(R.layout.alert_dialog_add_category, null)

        val alert = AlertDialog.Builder(context)
        alert.setView(promptsView)

        val input: EditText = promptsView.findViewById(R.id.edtAddCat) as EditText

        alert.setPositiveButton(R.string.add) { _, _ -> }

        alert.setNegativeButton(R.string.cancel) { _, _ -> }
        val alertDialog = alert.create()

        alertDialog.setOnShowListener {

            val button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {

                val categoryName: String = input.text.toString().trim()

                Log.e(ContentValues.TAG, "Category : " + categoryName)

                if (categoryName != "") {

                    val dbManager = DBManagerCategory(context)
                    dbManager.insert(categoryName)




                    alertDialog.dismiss()
                } else {
                    toastMessage(context, "Erreur")
                }
            }
        }

        alertDialog.show()
    }


    fun toastMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

