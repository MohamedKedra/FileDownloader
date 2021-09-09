package com.kedra.filedownloader.main.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kedra.filedownloader.R
import com.kedra.filedownloader.databinding.ItemListLayoutBinding
import com.kedra.filedownloader.main.network.models.ItemsResponseItem

class MainAdapter : RecyclerView.Adapter<MainAdapter.ItemHolder>() {

    inner class ItemHolder(private val binding: ItemListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemsResponseItem) {
            with(binding) {

                tvTitle.text = item.name
                tvOther.text = item.type
                if (item.type == "VIDEO") {
                    ivFile.setImageResource(R.drawable.ic__video)
                } else {
                    ivFile.setImageResource(R.drawable.ic_pdf)
                }
            }
        }
    }

    var list: List<ItemsResponseItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = ItemListLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) = holder.bind(list[position])

    fun addList(list: List<ItemsResponseItem>) {
        this.list = list
    }

    override fun getItemCount(): Int = list.size
}