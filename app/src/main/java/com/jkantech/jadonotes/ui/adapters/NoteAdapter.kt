package com.jkantech.jadonotes.ui.adapters


import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Movie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jkantech.jadonotes.R
import com.jkantech.jadonotes.ui.models.Note
import java.util.*
/**
 * Created by Jonas Kaninda on 10/07/2020.
 */

class NoteAdapter(private var notes: ArrayList<Note>, private val itemClickListener: View.OnClickListener,val longClickListener: View.OnLongClickListener)
    : RecyclerView.Adapter<NoteAdapter.ViewHolder>(),Filterable {
    var searchableList: ArrayList<Note> = notes


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById(R.id.card_note_item) as CardView
        val titleView = cardView.findViewById(R.id.title) as TextView
        val excerptView = cardView.findViewById(R.id.excerpt) as TextView
        val date = cardView.findViewById(R.id.date) as TextView
        val noteCat = cardView.findViewById<TextView>(R.id.note_cat)
        // val createdate = cardView.findViewById(R.id.create_date) as TextView


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return ViewHolder(
            viewItem
        )

    }

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.cardView.setOnClickListener(itemClickListener)
        holder.cardView.setOnLongClickListener(longClickListener)
        holder.cardView.tag = position
        holder.titleView.text = note.title
        holder.excerptView.text = note.text
        holder.date.text = note.editdate
        holder.noteCat.text = note.category
        //Changer la couleur de note
        holder.cardView.setCardBackgroundColor(Color.parseColor(note.color))

        //holder.createdate.text=note.createdate
    }


    override fun getItemCount(): Int {
        return notes.size
    }

    fun clearAdapter() {
        this.notes.clear()
        notifyDataSetChanged()
    }






    override fun getFilter(): Filter {
        return object : Filter() {
            private val filterResults = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                searchableList.clear()


                if (constraint.isNullOrBlank()) {
                    searchableList.addAll(notes)

                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' ' }
                    for (item in 0..notes.size) {
                        if (notes[item].title!!.toLowerCase(Locale.ROOT).contains(filterPattern) || notes[item].text!!.toLowerCase(Locale.ROOT).contains(filterPattern)) {

                            searchableList.add(notes[item])

                        }
                    }
                }
                return filterResults
                    .also {
                    it.values = searchableList
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
               //searchableList= results!!.values as ArrayList<Note>

                notifyDataSetChanged()

            }
        }
    }

    /*

                     if (newText!!.isEmpty()){
                         display.clear()
                         val search=newText.toLowerCase(Locale.getDefault())
                         notes.forEach{
                             if ( it.title!!.toLowerCase(Locale.getDefault()).contains(search) || it.text!!.toLowerCase(Locale.getDefault()).contains(search)){
                                 display.add(it)
                             }

                             }
                         recyclerView!!.adapter = adapter
                         adapter.notifyDataSetChanged()


                     }
                     else{
                         display.clear()
                       display.addAll(dbManager.getNotesList())
                         adapter.notifyDataSetChanged()





                     }
                     return true
                     */




}










