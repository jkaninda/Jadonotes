package com.jkantech.jadonotes.ui.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jkantech.jadonotes.R
import com.jkantech.jadonotes.ui.adapters.CategoryAdapter
import com.jkantech.jadonotes.ui.database.DBManagerCategory
import com.jkantech.jadonotes.ui.database.DBManagerNote
import com.jkantech.jadonotes.ui.models.CategoryModel

class CategoryActivity : AppCompatActivity() ,View.OnClickListener{

    lateinit var categoryadapter: CategoryAdapter
    var categorylist:ArrayList<CategoryModel> = ArrayList()
    lateinit var dbManager: DBManagerCategory



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        categorylist= dbManager.getCategoryList2()

        categoryadapter= CategoryAdapter(applicationContext,categorylist,this)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCategory)
        recyclerView?.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = categoryadapter
/*
        categorylist.add(
            CategoryModel(
                0,"Exemple"
            )


        )
        categorylist.add(
            CategoryModel(
                1,"Exepmle2"
            )


        )

 */




    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}