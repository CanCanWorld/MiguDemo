package com.zrq.migudemo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zrq.migudemo.bean.SearchSongList
import com.zrq.migudemo.databinding.ItemSongListBinding
import com.zrq.migudemo.interfaces.OnItemClickListener

class SearchSongListAdapter(
    var context: Context,
    var list: ArrayList<SearchSongList.SongListsBean>,
    var onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<VH<ItemSongListBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemSongListBinding> {
        val mBinding = ItemSongListBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(mBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VH<ItemSongListBinding>, position: Int) {
        val listsBean = list[position]
        holder.binding.apply {
            Glide.with(context)
                .load(listsBean.img)
                .into(ivAlbum)
            tvListName.text = listsBean.name
            tvDetail.text = "${listsBean.musicNum}首"
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}