package com.zrq.migudemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zrq.migudemo.databinding.ItemPathBinding
import com.zrq.migudemo.interfaces.OnItemClickListener

class PathAdapter(
    var context: Context,
    var list: ArrayList<String>,
    var onItemClickListener: OnItemClickListener,
    var onEnsureClickListener: OnItemClickListener,
) : RecyclerView.Adapter<VH<ItemPathBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemPathBinding> {
        val mBinding = ItemPathBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(mBinding)
    }

    override fun onBindViewHolder(holder: VH<ItemPathBinding>, position: Int) {
        holder.binding.apply {
            tvPath.text = list[position]
            tvPath.setOnClickListener {
                onItemClickListener.onItemClick(it, position)
            }
            btnEnsure.setOnClickListener {
                onEnsureClickListener.onItemClick(it, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}