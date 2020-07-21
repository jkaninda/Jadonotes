package com.jkantech.jadonotes.ui.views

import android.content.ContentValues
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jkantech.jadonotes.R
import com.jkantech.jadonotes.ui.adapters.CategoryAdapter
import com.jkantech.jadonotes.ui.adapters.NoteAdapter
import com.jkantech.jadonotes.ui.database.DBManagerCategory
import com.jkantech.jadonotes.ui.models.CategoryModel
import com.jkantech.jadonotes.ui.utils.toastMessage
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.containt_category.*

class CategoryActivity : AppCompatActivity() ,View.OnClickListener{

    lateinit var categoryadapter: CategoryAdapter
    var categorylist:ArrayList<CategoryModel> = ArrayList()
    var dbManager = DBManagerCategory(this)
    lateinit var recyclerView: RecyclerView
    lateinit var category:CategoryModel
    lateinit var fabAddCategory:FloatingActionButton
    var id:Int?=null






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        setSupportActionBar(toolbar)
        title = (getString(R.string.all_category))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        categorylist= dbManager.getCategoryList()
        fabAddCategory=findViewById(R.id.fabAddCategory)
        fabAddCategory.setOnClickListener(this)

        categoryadapter= CategoryAdapter(applicationContext,categorylist)
         recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCategory)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = categoryadapter

        if (categorylist.isEmpty()) {
            txtNoCategory.visibility=VISIBLE
        }



    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                /*
                R.id.imgEditCategory -> {
                    dialogUpdateCategory(this, category.id!!)
                    Toast.makeText(this, "Edition", Toast.LENGTH_SHORT).show()
                }



                 */

                    // Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()



                R.id.fabAddCategory ->{
                    dialogAddCategory()
                }


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
                    toastMessage(this, getString(R.string.no_category))
                    alertDialog.dismiss()
                }
            }
        }

        alertDialog.show()
    }
    private fun isAdded() {
        categoryadapter.clearAdapter()
        categorylist= dbManager.getCategoryList()

        categoryadapter= CategoryAdapter(applicationContext,categorylist)
        recyclerView.adapter=categoryadapter
        categoryadapter.notifyDataSetChanged()





    }



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
                    //toastMessage(context, context.getString(R.string.please_enter_something_to_update))
                    toastMessage(context, "Veillez entrer pour modifier")
                }
            }
        }

        alertDialog.show()
    }






    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


}