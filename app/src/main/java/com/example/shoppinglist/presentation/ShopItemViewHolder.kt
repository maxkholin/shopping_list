package com.example.shoppinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.shoppinglist.R

class ShopItemViewHolder(itemView: View) : ViewHolder(itemView) {

    val itemName = itemView.findViewById<TextView>(R.id.tv_item_name)
    val itemCount = itemView.findViewById<TextView>(R.id.tv_item_count)
}