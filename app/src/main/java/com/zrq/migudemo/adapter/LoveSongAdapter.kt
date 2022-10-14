package com.zrq.migudemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.databinding.ItemSongOfSingerBinding
import com.zrq.migudemo.interfaces.OnItemClickListener
import com.zrq.migudemo.interfaces.OnItemLongClickListener
import com.zrq.migudemo.interfaces.OnMoreClickListener

class LoveSongAdapter(
    private var context: Context,
    private var list: ArrayList<SearchSong.MusicsDTO>,
    private var onItemClickListener: OnItemClickListener,
    private var onMoreClickListener: OnMoreClickListener
) : RecyclerView.Adapter<VH<ItemSongOfSingerBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemSongOfSingerBinding> {
        val mBinding = ItemSongOfSingerBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(mBinding)
    }

    override fun onBindViewHolder(holder: VH<ItemSongOfSingerBinding>, position: Int) {
        holder.binding.apply {
            tvSong.text = list[position].songName
            tvNumber.text = position.toString()
            tvSinger.text = list[position].singerName
            ibMore.setOnClickListener {
                onMoreClickListener.onMoreClick(it, position)
            }
            root.setOnClickListener {
                onItemClickListener.onItemClick(it, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}