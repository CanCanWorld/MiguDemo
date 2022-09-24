package com.zrq.migudemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zrq.migudemo.bean.SearchSinger
import com.zrq.migudemo.databinding.ItemSearchSingerBinding
import com.zrq.migudemo.interfaces.OnItemClickListener

class SearchSingerAdapter(
    var context: Context,
    var list: ArrayList<SearchSinger.ArtistsDTO>,
    var listener: OnItemClickListener
) : RecyclerView.Adapter<VH<ItemSearchSingerBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemSearchSingerBinding> {
        val mBinding = ItemSearchSingerBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(mBinding)
    }

    override fun onBindViewHolder(holder: VH<ItemSearchSingerBinding>, position: Int) {
        holder.binding.apply {
            tvSinger.text = list[position].title
            Glide.with(context)
                .load(list[position].artistPicL)
                .into(ivHead)
            root.setOnClickListener {
                listener.onItemClick(it, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}