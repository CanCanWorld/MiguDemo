package com.zrq.migudemo.ui

import android.annotation.SuppressLint
import android.view.*
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.zrq.migudemo.R
import com.zrq.migudemo.adapter.LoveSongAdapter
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.dao.SongDaoImpl
import com.zrq.migudemo.databinding.FragmentLoveBinding
import com.zrq.migudemo.db.SongDatabaseHelper
import com.zrq.migudemo.interfaces.OnItemClickListener
import com.zrq.migudemo.interfaces.OnItemLongClickListener

class LoveFragment : BaseFragment<FragmentLoveBinding>(), OnItemClickListener,
    OnItemLongClickListener {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoveBinding {
        return FragmentLoveBinding.inflate(inflater, container, false)
    }

    private lateinit var songDaoImpl: SongDaoImpl
    private lateinit var adapter: LoveSongAdapter
    private val listSong = ArrayList<SearchSong.MusicsDTO>()

    override fun initData() {
        songDaoImpl = SongDaoImpl(SongDatabaseHelper(requireContext()))
        listSong.clear()
        listSong.addAll(songDaoImpl.listAllSongs())
        adapter = LoveSongAdapter(requireContext(), listSong, this, this)
        mBinding.rvLoveSong.adapter = adapter
        mBinding.rvLoveSong.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun initEvent() {
        mBinding.apply {


        }

    }

    companion object {
        const val TAG = "LoveFragment"
    }

    override fun onItemClick(view: View, position: Int) {
        mainModel.playerControl?.setList(listSong)
        mainModel.onSongChangeListener?.onSongChange(listSong[position])
    }

    override fun onItemLongClick(view: View, position: Int) {
        showPopMenu(view, position)
    }

    @SuppressLint("RtlHardcoded", "NotifyDataSetChanged")
    private fun showPopMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(requireContext(), view, Gravity.RIGHT)
        popupMenu.menuInflater.inflate(R.menu.menu_love_long_click, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_not_love -> {
                    songDaoImpl.deleteSong(listSong[position].id.toInt())
                    listSong.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
                else -> {}
            }
            true
        }
        popupMenu.show()
    }
}