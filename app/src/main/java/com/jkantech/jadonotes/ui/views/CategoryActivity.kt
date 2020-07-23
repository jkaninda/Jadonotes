package com.jkantech.jadonotes.ui.views

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jkantech.jadonotes.R
import com.jkantech.jadonotes.ui.adapters.CategoryAdapter
import com.jkantech.jadonotes.ui.database.DBManagerCategory
import com.jkantech.jadonotes.ui.models.CategoryModel
import com.jkantech.jadonotes.ui.utils.toastMessage
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.containt_category.*
/**
 * Created by Jonas Kaninda on 10/07/2020.
 */
class CategoryActivity : AppCompatActivity() ,View.OnClickListener{

    lateinit var categoryadapter: CategoryAdapter
    var categorylist:ArrayList<CategoryModel> = ArrayList()
    var dbManager = DBManagerCategory(this)
    lateinit var recyclerView: RecyclerView
    private val themeKey = "currentTheme"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var imgDeleteCategory:ImageView





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
            "ThemePref",
            Context.MODE_PRIVATE
        )
        applyStyle()
        setContentView(R.layout.activity_category)
        setSupportActionBar(toolbar)
        title = (getString(R.string.all_category))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        categorylist= dbManager.getCategoryList()
        //fabAddCategory=findViewById(R.id.fabAddCategory)
        fabAddCategory.setOnClickListener(this)

        categoryadapter= CategoryAdapter(this,categorylist,this)
         recyclerView = findViewById(R.id.recyclerViewCategory)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = categoryadapter




    }

    override fun onClick(v: View?) {
        /*
        if (v!!.tag != null) {
            //deleteCat(v.tag as Int)


        } else {

         */
            when (v!!.id) {
                R.id.fabAddCategory -> {
                    dialogAddCategory()
                }
                R.id.imgEditCategory->{
                    dialogUpdateCategory(v.tag as Int)
                   // dbManager.update(v.tag as Int, "Kaninda")


                }
                R.id.imgDeleteCategory->{
                    DeleteCatDialog(v)




                }

            }



    }
    fun dialogAddCategory() {

        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.alert_dialog_add_category, null)

        val alert = AlertDialog.Builder(this)
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

                    val dbManager = DBManagerCategory(this)
                    dbManager.insert(categoryName)
                     isAdded()



                    alertDialog.dismiss()
                } else {
                    toastMessage(this, getString(R.string.please_enter_cat))
                }
            }
        }

        alertDialog.show()
    }
    private fun isAdded() {
        categoryadapter.clearAdapter()
        categorylist= dbManager.getCategoryList()
        categoryadapter= CategoryAdapter(applicationContext,categorylist,this)
        recyclerView.adapter=categoryadapter
        categoryadapter.notifyDataSetChanged()

        if (categorylist.isEmpty()) {
            txtNoCategory.visibility=VISIBLE
        }

    }



    fun dialogUpdateCategory(id: Int) {

        val dbManager = DBManagerCategory(this)
        //val catName = dbManager.getCategoryName(id)
        val category= this.categorylist[id]
        //val Name= this.categorylist[id]
        val test=category.id


        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.alert_dialog_update_category, null)

        val alert = AlertDialog.Builder(this)
        alert.setView(promptsView)

        val input: EditText = promptsView.findViewById(R.id.edtUpdateCat) as EditText

        input.setText(category.categoryName)
       input.setSelection(input.text.length)

        alert.setPositiveButton(R.string.update) { _, _ -> }

        alert.setNegativeButton(R.string.cancel) { _, _ -> }
        val alertDialog = alert.create()

        alertDialog.setOnShowListener {

            val button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {

                val cat: String = input.text.toString()

                when {
                    cat.trim()=="" -> {


                        toastMessage(this, getString(R.string.please_enter_cat))


                    }
                    cat==category.categoryName -> {
                        toastMessage(this, getString(R.string.no_changed))

                    }
                    else -> {


                        dbManager.update(test!!, cat)
                        alertDialog.dismiss()
                        isAdded()


                    }
                }

            }
        }

        alertDialog.show()
    }

    fun deleteCat(noteIndex: Int) {


       val category= this.categorylist.removeAt(noteIndex)
        categoryadapter.notifyDataSetChanged()
        category.id?.let { dbManager.delete(it) }

    }
    private fun DeleteCatDialog(v: View?) {
        val dialog = Dialog(this)
        dialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.exit_dialog)
            setCancelable(true)
            val exit = dialog.findViewById<TextView>(R.id.exit_msg)
            val confirm_btn = findViewById<Button>(R.id.confirm_btn)
            val cancel = findViewById<Button>(R.id.cancel_btn)
            exit.text = getString(R.string.delete_cat)
            confirm_btn.setOnClickListener {
                deleteCat(v!!.tag as Int)
                dismiss()
                isAdded()


            }
            cancel.setOnClickListener {
                dismiss()
            }
            show()


        }
    }





    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    private fun applyStyle() {

        when (sharedPreferences.getInt(themeKey, 0)) {

            0 -> theme.applyStyle(R.style.Theme_JadoNotes, true)
            1 -> theme.applyStyle(R.style.Theme1, true)
            2 -> theme.applyStyle(R.style.Theme2, true)
            3 -> theme.applyStyle(R.style.Theme3, true)

        }


    }

}