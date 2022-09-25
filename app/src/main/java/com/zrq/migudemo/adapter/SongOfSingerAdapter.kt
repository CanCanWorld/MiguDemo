package com.zrq.migudemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zrq.migudemo.bean.SongOfSinger
import com.zrq.migudemo.databinding.ItemSongOfSingerBinding
import com.zrq.migudemo.interfaces.OnItemClickListener

class SongOfSingerAdapter(
    private var context: Context,
    private var list: ArrayList<SongOfSinger.SongListBean>,
    private var onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<VH<ItemSongOfSingerBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemSongOfSingerBinding> {
        val mBinding = ItemSongOfSingerBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(mBinding)
    }

    override fun onBindViewHolder(holder: VH<ItemSongOfSingerBinding>, position: Int) {
        holder.binding.apply {
            tvSong.text = list[position].al.title
            tvNumber.text = position.toString()
            tvSinger.text = list[position].al.singer
            root.setOnClickListener {
                onItemClickListener.onItemClick(it, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}