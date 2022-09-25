package com.zrq.migudemo.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.zrq.migudemo.R
import com.zrq.migudemo.adapter.LoveSongAdapter
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.bean.SongOfSinger
import com.zrq.migudemo.dao.SongDaoImpl
import com.zrq.migudemo.databinding.FragmentLoveBinding
import com.zrq.migudemo.db.SongDatabaseHelper
import com.zrq.migudemo.interfaces.OnItemClickListener
import com.zrq.migudemo.interfaces.OnSongMoreClickListener

class LoveFragment : BaseFragment<FragmentLoveBinding>(), OnItemClickListener,
    OnSongMoreClickListener {
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
            ibAdd.setOnClickListener {
                Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .navigate(R.id.action_global_searchFragment)
            }
        }
    }

    companion object {
        const val TAG = "LoveFragment"
    }

    override fun onItemClick(view: View, position: Int) {
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSongMoreClick(song: SearchSong.MusicsDTO, position: Int) {
        songDaoImpl.deleteSong(song.id.toInt())
        listSong.removeAt(position)
        adapter.notifyDataSetChanged()
    }
}