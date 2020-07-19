package com.jkantech.jadonotes.ui.adapters


import android.annotation.SuppressLint
import android.graphics.Color
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
import kotlin.collections.ArrayList

class NoteAdapter(private val notes: ArrayList<Note>, private val itemClickListener: View.OnClickListener)
    : RecyclerView.Adapter<NoteAdapter.ViewHolder>(),Filterable {
    lateinit var searchableList: MutableList<Note>
    private var onNothingFound: (() -> Unit)? = null



        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById(R.id.card_note_item) as CardView
        val titleView = cardView.findViewById(R.id.title) as TextView
        val excerptView = cardView.findViewById(R.id.excerpt) as TextView
        val date = cardView.findViewById(R.id.date) as TextView
            val noteCat=cardView.findViewById<TextView>(R.id.note_cat)
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
        holder.cardView.tag = position
        holder.titleView.text = note.title
        holder.excerptView.text = note.text
        holder.date.text = note.editdate
        holder.noteCat.text= note.category
        //Changer la couleur de note
        holder.cardView.setCardBackgroundColor(Color.parseColor(note.color))

        //holder.createdate.text=note.createdate
    }


            override fun getItemCount(): Int {
        return notes.size
    }



    override fun getFilter(): Filter {
        return filter
    }

    private val filter: Filter = object : Filter() {
        var filteredList: ArrayList<Note> = ArrayList(notes)
        override fun performFiltering(constraint: CharSequence): FilterResults {

            if (constraint.isEmpty()) {
                filteredList.addAll(notes)

            } else {
                val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' ' }
                for (item in 0..notes.size) {
                    if (notes[item].text!!.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(notes[item])
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
        //filteredList = filterResults.values as ArrayList<Note>

            notifyDataSetChanged()
        }
    }


}






