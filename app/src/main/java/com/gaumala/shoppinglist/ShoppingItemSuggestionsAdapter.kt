package com.gaumala.shoppinglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShoppingItemSuggestionsAdapter(private val onNewItemSubmitted: (String)->Unit) :
    RecyclerView.Adapter<ShoppingItemSuggestionsAdapter.ViewHolder>() {

    var suggestions: List<String> = emptyList()
        set (value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return suggestions[position].hashCode().toLong()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view as TextView

    }


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.suggestion_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = suggestions[position]
        holder.textView.text = item
        holder.textView.setOnClickListener {
            onNewItemSubmitted(item)
        }
    }

    override fun getItemCount() = suggestions.size
}