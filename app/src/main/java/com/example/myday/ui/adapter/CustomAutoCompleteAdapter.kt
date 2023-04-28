package com.example.myday.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.myday.databinding.ResItemAutoCompleteCityBinding

class CustomAutoCompleteAdapter(context: Context) : ArrayAdapter<String>(context, 0) {

    private val itemList: MutableList<String> = mutableListOf()
    private var clickListener: ((String) -> Unit)? = null

    fun setItems(dataList: List<String>) {
        itemList.clear()
        itemList.addAll(dataList)
        notifyDataSetChanged()
    }

    fun setClickListener(clickListener: (String) -> Unit) {
        this.clickListener = clickListener
    }

    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItem(position: Int): String {
        return itemList[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView ?: ResItemAutoCompleteCityBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            root.tag = ViewHolder(this)
        }.root

        val viewHolder = view.tag as ViewHolder

        val item = getItem(position)
        viewHolder.binding.cityName.text = item
        viewHolder.binding.root.setOnClickListener {
            clickListener?.invoke(item)
        }

        return view
    }

    private inner class ViewHolder(val binding: ResItemAutoCompleteCityBinding)
}