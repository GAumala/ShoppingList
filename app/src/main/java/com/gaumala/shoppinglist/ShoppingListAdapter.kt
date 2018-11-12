package com.gaumala.shoppinglist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView

class ShoppingListAdapter(private val model: ShoppingListViewModel) :
    RecyclerView.Adapter<ShoppingListAdapter.ShoppingItemViewHolder>() {

    private val items = model.items

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return items[position].hashCode().toLong()
    }

    class ShoppingItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkbox)
        val textView: TextView = view.findViewById(R.id.textview)

    }


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ShoppingItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopping_item_view, parent, false)
        return ShoppingItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item.text
        holder.checkBox.isChecked = item.checked
        holder.checkBox.setOnCheckedChangeListener { _, checked ->
            model.onItemChecked(position, checked)
        }
    }

    override fun getItemCount() = items.size
}