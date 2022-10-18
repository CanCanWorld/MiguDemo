package com.zrq.migudemo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zrq.migudemo.bean.SearchAlbum
import com.zrq.migudemo.databinding.ItemAlbumBinding
import com.zrq.migudemo.interfaces.OnItemClickListener

class SearchAlbumAdapter(
    var context: Context,
    var list: ArrayList<SearchAlbum.AlbumsDTO>,
    var onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<VH<ItemAlbumBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemAlbumBinding> {
        val mBinding = ItemAlbumBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(mBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VH<ItemAlbumBinding>, position: Int) {
        val listsBean = list[position]
        holder.binding.apply {
            Glide.with(context)
                .load(listsBean.albumPicS)
                .into(ivAlbum)
            tvListName.text = listsBean.title
            tvNumber.text = "${listsBean.songNum}首"
            tvTime.text = "发布时间: ${listsBean.publishDate}"
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}