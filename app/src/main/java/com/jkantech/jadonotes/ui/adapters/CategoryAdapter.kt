package com.jkantech.jadonotes.ui.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jkantech.jadonotes.R
import com.jkantech.jadonotes.ui.models.CategoryModel
import com.jkantech.jadonotes.ui.models.Note
import com.jkantech.jadonotes.ui.views.CategoryActivity
import kotlin.collections.ArrayList

class CategoryAdapter(val context: Context,private val categories: ArrayList<CategoryModel>, private val itemClickListener: View.OnClickListener)
    : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(),Filterable {


     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val imgEditCategory: ImageView = itemView.findViewById(R.id.imgEditCategory)
         val imgDeleteCategory: ImageView = itemView.findViewById(R.id.imgDeleteCategory)
         val txtCategoryName = itemView.findViewById<TextView>(R.id.txtCategoryName)


     }


     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val viewItem = LayoutInflater.from(parent.context)
             .inflate(R.layout.row_category, parent, false)
         return ViewHolder(
             viewItem
         )

     }


    @SuppressLint("Range")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtCategoryName.text = categories[position].categoryName
        holder.imgEditCategory.setOnClickListener(itemClickListener)

        holder.imgDeleteCategory.setOnClickListener(itemClickListener)

    }

     override fun getItemCount(): Int {
         return categories.size
     }

     override fun getFilter(): Filter {
         return filter
     }

    }














