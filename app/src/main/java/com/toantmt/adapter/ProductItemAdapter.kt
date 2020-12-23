package com.toantmt.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.toantmt.db.entities.ProductEntity
import com.toantmt.rxjavaexample.R
import com.toantmt.rxjavaexample.databinding.ViewholderFilteringItemBinding

class ProductItemAdapter: RecyclerView.Adapter<ProductItemAdapter.ProductItemViewHolder>() {

    private var onItemSelectedListener: ((content: ProductEntity) -> Unit) ?= null
    private val actualItems = arrayListOf<ProductEntity>()

    fun setItemSelectedListener(block: ((content: ProductEntity) -> Unit) ?= null) {
        onItemSelectedListener = block
    }

    init {
        setHasStableIds(true)
    }

    fun mergeItems(newItems: List<ProductEntity>) {
        if (actualItems.isEmpty()) {
            actualItems.addAll(newItems)
            notifyDataSetChanged()
            return
        }

        updateItems(newItems)
    }

    private fun updateItems(willDisplayingItems: List<ProductEntity>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun getOldListSize(): Int = this@ProductItemAdapter.actualItems.size

            override fun getNewListSize(): Int = willDisplayingItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = this@ProductItemAdapter.actualItems[oldItemPosition]
                val newItem = willDisplayingItems[newItemPosition]
                return oldItem.id == newItem.id && oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = this@ProductItemAdapter.actualItems[oldItemPosition]
                val newItem = willDisplayingItems[newItemPosition]
                return oldItem.name == newItem.name
            }
        }, true)

        this@ProductItemAdapter.actualItems.clear()
        this@ProductItemAdapter.actualItems.addAll(willDisplayingItems)
        diffResult.dispatchUpdatesTo(this@ProductItemAdapter)
    }

    inner class ProductItemViewHolder(private val binding: ViewholderFilteringItemBinding): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(item: ProductEntity) {
            binding.txtContent.text = "${item.id} - ${item.name}"
            binding.txtContent.setOnClickListener {
                onItemSelectedListener?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_filtering_item, parent, false)
        return ProductItemViewHolder(ViewholderFilteringItemBinding.bind(binding))
    }

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        holder.onBind(actualItems[position])
    }

    override fun getItemCount(): Int {
        return actualItems.size
    }

    override fun getItemId(position: Int): Long {
        return actualItems[position].id
    }
}