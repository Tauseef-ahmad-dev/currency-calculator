package com.task.currencyconverter.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.task.currencyconverter.R
import com.task.currencyconverter.core.domain.model.History
import com.task.currencyconverter.databinding.ItemHistoryBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var listData = ArrayList<History>()

    var onItemClick : ((History) -> Unit)? = null

    fun setData(newListData: List<History>?){
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = ItemHistoryBinding.bind(itemView)
        fun bind(history: History){
            with(binding){
                val before = "${history.fromCode} ${history.fromValue}"
                val after = "${history.toValue} ${history.toCode}"
                tvHistoryBefore.text = before
                tvHistoryAfter.text = after
                btnDelete.setOnClickListener {
                    onItemClick?.invoke(history)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size
}