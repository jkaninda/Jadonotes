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
import com.jkantech.jadonotes.ui.utils.delete
import kotlin.collections.ArrayList

class CategoryAdapter(val context: Context,private val categories: ArrayList<CategoryModel>)
    : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(),Filterable {


     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val imgEditCategory: ImageView = itemView.findViewById(R.id.imgEditCategory)
         val imgDeleteCategory: ImageView = itemView.findViewById(R.id.imgDeleteCategory)
         val txtCategoryName = itemView.findViewById<TextView>(R.id.txtCategoryName)

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
        holder.imgEditCategory.setOnClickListener{

        }
        holder.imgDeleteCategory.setOnClickListener{
            delete(context, categories[position].id!!)
            notifyDataSetChanged()


        }

    }

     override fun getItemCount(): Int {
         return categories.size
     }

     override fun getFilter(): Filter {
         return filter
     }

    }














