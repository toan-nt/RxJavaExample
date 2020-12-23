package com.toantmt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.toantmt.adapter.MenuItemViewAdapter.MenuItemViewHolder
import com.toantmt.rxjavaexample.databinding.ViewholderFilteringItemBinding

class MenuItemViewAdapter: RecyclerView.Adapter<MenuItemViewHolder>() {

    private var onItemSelectedListener: ((content: String) -> Unit) ?= null
    val actualItems = arrayListOf<String>()

    fun setItemSelectedListener(block: ((content: String) -> Unit) ?= null) {
        onItemSelectedListener = block
    }

    inner class MenuItemViewHolder(private val binding: ViewholderFilteringItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBind(content: String) {
            binding.txtContent.text = content
            binding.txtContent.setOnClickListener {
                onItemSelectedListener?.invoke(content)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        val binding = ViewholderFilteringItemBinding.inflate(LayoutInflater.from(parent.context))
        return MenuItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        holder.onBind(actualItems[position])
    }

    override fun getItemCount(): Int {
        return actualItems.size
    }

}