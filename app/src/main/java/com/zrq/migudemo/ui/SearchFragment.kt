package com.zrq.migudemo.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zrq.migudemo.R
import com.zrq.migudemo.adapter.ListSongAdapter
import com.zrq.migudemo.adapter.SearchResultAdapter
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.databinding.FragmentSearchBinding
import com.zrq.migudemo.interfaces.OnItemClickListener
import com.zrq.migudemo.interfaces.OnSongChangeListener
import com.zrq.migudemo.interfaces.OnSongDeleteListener
import com.zrq.migudemo.util.Constants.TYPE_ALBUM
import com.zrq.migudemo.util.Constants.TYPE_LYRICS
import com.zrq.migudemo.util.Constants.TYPE_MV
import com.zrq.migudemo.util.Constants.TYPE_SINGER
import com.zrq.migudemo.util.Constants.TYPE_SONG
import com.zrq.migudemo.util.Constants.TYPE_SONG_LIST

class SearchFragment : BaseFragment<FragmentSearchBinding>(), OnSongChangeListener,
    OnItemClickListener, OnSongDeleteListener {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    lateinit var searchAdapter: SearchResultAdapter
    lateinit var listAdapter: ListSongAdapter
    private lateinit var animation: ValueAnimator
    private lateinit var listDialog: BottomSheetDialog
    private lateinit var listView: View
    var list = ArrayList<SearchSong.MusicsDTO>()

    override fun initData() {

        initTabLayout()

        initListDialog()

        initAnimation()
    }

    override fun initEvent() {

        mBinding.apply {

            btnSearch.setOnClickListener {
                val key = etSearch.text.toString()
                mainModel.key.postValue(key)
            }

            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    btnSearch.callOnClick()
                }
                false
            }

            ibDelete.setOnClickListener {
                etSearch.setText("")
            }

            ibList.setOnClickListener {
                listDialog.show()
            }

            llPlayBar.setOnClickListener {
                Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .navigate(R.id.action_global_playFragment)
            }
        }

        mainModel.apply {
            setOnSongChangeListener(this@SearchFragment)
            nowPlaying.observe(this@SearchFragment) {
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

    private fun initTabLayout() {
        val fragmentList = ArrayList<SearchResultFragment>()
        val titles = ArrayList<String>()
        fragmentList.add(SearchResultFragment.newInstance(TYPE_SONG))
        fragmentList.add(SearchResultFragment.newInstance(TYPE_SINGER))
        fragmentList.add(SearchResultFragment.newInstance(TYPE_SONG_LIST))
        fragmentList.add(SearchResultFragment.newInstance(TYPE_ALBUM))
        fragmentList.add(SearchResultFragment.newInstance(TYPE_MV))
        fragmentList.add(SearchResultFragment.newInstance(TYPE_LYRICS))
        titles.add("歌曲")
        titles.add("歌手")
        titles.add("歌单")
        titles.add("专辑")
        titles.add("MV")
        titles.add("歌词")
        searchAdapter = SearchResultAdapter(childFragmentManager, fragmentList, titles)
        mBinding.viewPager.adapter = searchAdapter
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)
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

    private fun refreshPlayBar(song: SearchSong.MusicsDTO) {
        mBinding.apply {
            llPlayBar.visibility = View.VISIBLE
            ivAlbum.visibility = View.VISIBLE
            tvSongName.text = song.songName
            tvSinger.text = song.singerName
            Glide.with(this@SearchFragment)
                .load(song.cover)
                .into(ivAlbum)
            animation.start()
        }
    }

    companion object {
        const val TAG = "SearchFragment"
    }

    override fun onResume() {
        super.onResume()
        if (mainModel.playSong != null) {
            refreshPlayBar(mainModel.playSong!!)
        }
    }

    override fun onSongChange(song: SearchSong.MusicsDTO) {
        list.clear()
        list.addAll(mainModel.playList)
        mainModel.playThis(list.indexOf(song))
        mainModel.playSong = song
        refreshListDialog()
        refreshPlayBar(song)
    }

    override fun onItemClick(view: View, position: Int) {
    }

    override fun onSongDelete(song: SearchSong.MusicsDTO) {
        list.remove(song)
        mainModel.playList.clear()
        mainModel.playList.addAll(list)
        refreshListDialog()
    }

}