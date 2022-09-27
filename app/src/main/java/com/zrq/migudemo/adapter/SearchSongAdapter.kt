package com.zrq.migudemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.databinding.ItemSearchSongBinding
import com.zrq.migudemo.interfaces.OnItemClickListener
import com.zrq.migudemo.interfaces.OnItemLongClickListener

class SearchSongAdapter(
    private val context: Context,
    private val list: ArrayList<SearchSong.MusicsDTO>,
    private var onItemClickListener: OnItemClickListener,
    private var onItemLongClickListener: OnItemLongClickListener
) : RecyclerView.Adapter<VH<ItemSearchSongBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemSearchSongBinding> {
        val mBinding =
            ItemSearchSongBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(mBinding)
    }

    override fun onBindViewHolder(holder: VH<ItemSearchSongBinding>, position: Int) {
        holder.binding.apply {
            tvSong.text = list[position].songName
            tvSinger.text = list[position].singerName
            tvAlbum.text = list[position].albumName
            itemRoot.setOnClickListener {
                onItemClickListener.onItemClick(it, position)
            }
            itemRoot.setOnLongClickListener {
                onItemLongClickListener.onItemLongClick(it, position)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}