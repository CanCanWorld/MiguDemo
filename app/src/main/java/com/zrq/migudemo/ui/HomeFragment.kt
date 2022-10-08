package com.zrq.migudemo.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zrq.migudemo.R
import com.zrq.migudemo.adapter.ListSongAdapter
import com.zrq.migudemo.adapter.PlayBarAdapter
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.databinding.FragmentHomeBinding
import com.zrq.migudemo.interfaces.OnItemClickListener
import com.zrq.migudemo.interfaces.OnSongChangeListener
import com.zrq.migudemo.interfaces.OnSongDeleteListener

class HomeFragment : BaseFragment<FragmentHomeBinding>(),
    OnSongChangeListener, OnItemClickListener, OnSongDeleteListener {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    private lateinit var listDialog: BottomSheetDialog
    private lateinit var listView: View
    lateinit var listAdapter: ListSongAdapter
    var list = ArrayList<SearchSong.MusicsDTO>()
    lateinit var adapter: PlayBarAdapter

    override fun initData() {
        initListDialog()
        adapter = PlayBarAdapter(requireActivity(), list)
        mBinding.viewPager2.adapter = adapter
    }

    override fun initEvent() {
        mainModel.apply {
            onSongChangeListener = this@HomeFragment
        }

        mBinding.apply {

            ibList.setOnClickListener {
                listDialog.show()
            }

            rbLove.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    Navigation.findNavController(requireActivity(), R.id.fragment_home_container)
                        .navigate(R.id.action_global_loveFragment)
                    rbSearch.isChecked = false
                }
            }

            rbSearch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    Navigation.findNavController(requireActivity(), R.id.fragment_home_container)
                        .navigate(R.id.action_global_searchFragment)
                    rbLove.isChecked = false
                }
            }


        }
    }

    private fun initListDialog() {
        listView = layoutInflater.inflate(R.layout.dialog_bottom_play_list, null)
        listDialog = BottomSheetDialog(requireContext())
        listDialog.setContentView(listView)
        listAdapter = ListSongAdapter(requireContext(), list, this, this)
        listView.findViewById<RecyclerView>(R.id.rv_song_list).apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        refreshListDialog()
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun refreshListDialog() {
        listAdapter.notifyDataSetChanged()
        listView.findViewById<TextView>(R.id.tv_list_size).text = "(${list.size})"
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSongChange(song: SearchSong.MusicsDTO) {
        list.clear()
        list.addAll(mainModel.getList()!!)
        mainModel.playThis(list.indexOf(song))
        mainModel.playSong = song
        refreshListDialog()
        adapter.notifyDataSetChanged()
//        refreshPlayBar(song)

    }


    override fun onSongDelete(song: SearchSong.MusicsDTO) {
        list.remove(song)
        mainModel.playerControl?.setList(list)
        refreshListDialog()
    }

    override fun onItemClick(view: View, position: Int) {
//        mainModel.playList.clear()
//        mainModel.playList.addAll(list)
        //todo
        Toast.makeText(requireContext(), "$position", Toast.LENGTH_SHORT).show()
        mainModel.playerControl?.setList(list)
        mainModel.onSongChangeListener?.onSongChange(list[position])
    }

    companion object {
        const val TAG = "HomeFragment"
    }

}