package com.zrq.migudemo.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zrq.migudemo.R
import com.zrq.migudemo.adapter.ListSongAdapter
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.databinding.FragmentPlayBarBinding
import com.zrq.migudemo.interfaces.OnItemClickListener
import com.zrq.migudemo.interfaces.OnSongChangeListener
import com.zrq.migudemo.interfaces.OnSongDeleteListener

class PlayBarFragment : BaseFragment<FragmentPlayBarBinding>(),
    OnSongChangeListener, OnItemClickListener, OnSongDeleteListener {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayBarBinding {
        return FragmentPlayBarBinding.inflate(inflater, container, false)
    }

    private lateinit var animation: ValueAnimator
    private lateinit var listDialog: BottomSheetDialog
    private lateinit var listView: View
    lateinit var listAdapter: ListSongAdapter
    var list = ArrayList<SearchSong.MusicsDTO>()

    override fun initData() {

        initListDialog()

        initAnimation()

    }

    override fun initEvent() {
        mBinding.apply {

            ibList.setOnClickListener {
                listDialog.show()
            }

            llPlayBar.setOnClickListener {
                Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .navigate(R.id.playFragment)
            }


        }

        mainModel.apply {
            onSongChangeListener = this@PlayBarFragment
            nowPlaying.observe(this@PlayBarFragment) {
                if (it != null) {
                    playSong = it
                    refreshPlayBar(it)
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

    private fun refreshPlayBar(song: SearchSong.MusicsDTO) {
        mBinding.apply {
            llPlayBar.visibility = View.VISIBLE
            ivAlbum.visibility = View.VISIBLE
            tvSongName.text = song.songName
            tvSinger.text = song.singerName
            Glide.with(this@PlayBarFragment)
                .load(song.cover)
                .into(ivAlbum)
            animation.start()
        }
    }

    private fun initAnimation() {
        animation = ObjectAnimator.ofFloat(mBinding.ivAlbum, "rotation", .0f, 360.0f)
        animation.apply {
            duration = 6000
            interpolator = LinearInterpolator()
            repeatCount = -1
            repeatMode = ObjectAnimator.RESTART
        }
    }

    override fun onResume() {
        super.onResume()
        if (mainModel.playSong != null) {
            refreshPlayBar(mainModel.playSong!!)
        }
    }


    override fun onSongChange(song: SearchSong.MusicsDTO) {
        list.clear()
        list.addAll(mainModel.getList()!!)
        mainModel.playThis(list.indexOf(song))
        mainModel.playSong = song
        refreshListDialog()
        refreshPlayBar(song)
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
        mainModel.playerControl?.setList(list)
        mainModel.onSongChangeListener?.onSongChange(list[position])
    }

    companion object {
        const val TAG = "PlayBarFragment"

        fun getInstance() = PlayBarFragment()
    }

}