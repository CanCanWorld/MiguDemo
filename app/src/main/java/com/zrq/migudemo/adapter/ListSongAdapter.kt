package com.zrq.migudemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.databinding.ItemListSongBinding
import com.zrq.migudemo.interfaces.OnItemClickListener
import com.zrq.migudemo.interfaces.OnSongDeleteListener

class ListSongAdapter(
    private val context: Context,
    private val list: ArrayList<SearchSong.MusicsDTO>,
    private var onItemClickListener: OnItemClickListener,
    private var onSongDeleteListener: OnSongDeleteListener
) : RecyclerView.Adapter<VH<ItemListSongBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemListSongBinding> {
        val mBinding =
            ItemListSongBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(mBinding)
    }

    override fun onBindViewHolder(holder: VH<ItemListSongBinding>, position: Int) {
        holder.binding.apply {
            tvSongName.text = list[position].songName
            tvSinger.text = list[position].singerName
            root.setOnClickListener {
                onItemClickListener.onItemClick(it, position)
            }
            ibDelete.setOnClickListener {
                onSongDeleteListener.onSongDelete(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}