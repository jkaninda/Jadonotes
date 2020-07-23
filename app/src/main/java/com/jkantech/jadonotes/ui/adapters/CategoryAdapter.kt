package com.jkantech.jadonotes.ui.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.jkantech.jadonotes.R
import com.jkantech.jadonotes.ui.models.CategoryModel
import kotlin.collections.ArrayList
/**
 * Created by Jonas Kaninda on 10/07/2020.
 */

class CategoryAdapter(val context: Context,private val categories: ArrayList<CategoryModel>,val itemClickListener:View.OnClickListener)
    : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(),Filterable {


     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         //val cardView:CardView=itemView.findViewById(R.id.card_cat_item)
         val imgEditCategory: ImageView = itemView.findViewById(R.id.imgEditCategory)
         val imgDeleteCategory: ImageView = itemView.findViewById(R.id.imgDeleteCategory)
         val txtCategoryName:TextView = itemView.findViewById(R.id.txtCategoryName)

     }


     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val viewItem = LayoutInflater.from(parent.context)
             .inflate(R.layout.item_category, parent, false)
         return ViewHolder(
             viewItem
         )

     }
    fun clearAdapter() {
        this.categories.clear()
        notifyDataSetChanged()
    }


    @SuppressLint("Range")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtCategoryName.text = categories[position].categoryName
        holder.imgEditCategory.setOnClickListener(itemClickListener)
        holder.imgDeleteCategory.setOnClickListener(itemClickListener)
        holder.imgEditCategory.tag = position
        holder.imgDeleteCategory.tag = position




    }

     override fun getItemCount(): Int {
         return categories.size
     }

     override fun getFilter(): Filter {
         return filter
     }

    }














