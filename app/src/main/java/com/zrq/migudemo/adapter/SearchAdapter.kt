package com.zrq.migudemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zrq.migudemo.bean.Song
import com.zrq.migudemo.databinding.ItemSearchSongBinding

class SearchAdapter(
    private val context: Context,
    private val list: ArrayList<Song.MusicsDTO>,
    var onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<VH<ItemSearchSongBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemSearchSongBinding> {
        val mBinding =
            ItemSearchSongBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(mBinding)
    }

    override fun onBindViewHolder(holder: VH<ItemSearchSongBinding>, position: Int) {
        holder.binding.tvSong.text = list[position].songName
        holder.binding.tvSinger.text = list[position].singerName
        holder.binding.itemRoot.setOnClickListener {
            onItemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}