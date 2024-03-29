package com.zrq.migudemo.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.zrq.migudemo.R
import com.zrq.migudemo.adapter.SearchAlbumAdapter
import com.zrq.migudemo.adapter.SearchSingerAdapter
import com.zrq.migudemo.adapter.SearchSongAdapter
import com.zrq.migudemo.adapter.SearchSongListAdapter
import com.zrq.migudemo.bean.SearchAlbum
import com.zrq.migudemo.bean.SearchSinger
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.bean.SearchSongList
import com.zrq.migudemo.dao.SongDaoImpl
import com.zrq.migudemo.databinding.FragmentSearchResultBinding
import com.zrq.migudemo.db.SongDatabaseHelper
import com.zrq.migudemo.interfaces.OnItemClickListener
import com.zrq.migudemo.interfaces.OnMoreClickListener
import com.zrq.migudemo.util.Constants.BASE_URL
import com.zrq.migudemo.util.Constants.SEARCH
import com.zrq.migudemo.util.Constants.TYPE_ALBUM
import com.zrq.migudemo.util.Constants.TYPE_SINGER
import com.zrq.migudemo.util.Constants.TYPE_SONG
import com.zrq.migudemo.util.Constants.TYPE_SONG_LIST
import com.zrq.migudemo.view.DownloadDialog
import okhttp3.*
import java.io.IOException

class SearchResultFragment(
    var type: Int
) : BaseFragment<FragmentSearchResultBinding>() {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchResultBinding {
        return FragmentSearchResultBinding.inflate(inflater, container, false)
    }

    private lateinit var songAdapter: SearchSongAdapter
    private lateinit var singerAdapter: SearchSingerAdapter
    private lateinit var songListAdapter: SearchSongListAdapter
    private lateinit var albumAdapter: SearchAlbumAdapter
    private val listSong = ArrayList<SearchSong.MusicsDTO>()
    private val listSinger = ArrayList<SearchSinger.ArtistsDTO>()
    private val listSongList = ArrayList<SearchSongList.SongListsBean>()
    private val listAlbum = ArrayList<SearchAlbum.AlbumsDTO>()
    private var key = ""
    private var offset = 1
    private lateinit var songDaoImpl: SongDaoImpl
    private var downloadDialog: DownloadDialog? = null

    override fun initData() {
        songDaoImpl = SongDaoImpl(SongDatabaseHelper(requireContext()))
        songAdapter = SearchSongAdapter(requireContext(), listSong, object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                mainModel.playerControl?.setList(listSong)
                mainModel.onSongChangeListener?.onSongChange(position)
            }
        }, object : OnMoreClickListener {
            override fun onMoreClick(view: View, position: Int) {
                showPopMenu(view, position)
            }
        })
        singerAdapter =
            SearchSingerAdapter(requireContext(), listSinger, object : OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    mainModel.artistId.postValue(listSinger[position].id)
                    Navigation.findNavController(requireActivity(), R.id.fragment_home_container)
                        .navigate(R.id.action_global_singerInfoFragment)
                }

            })
        songListAdapter =
            SearchSongListAdapter(requireContext(), listSongList, object : OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {

                }
            })
        albumAdapter =
            SearchAlbumAdapter(requireContext(), listAlbum, object : OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {

                }
            })
        when (type) {
            TYPE_SONG -> {
                mBinding.recyclerView.adapter = songAdapter
            }
            TYPE_SINGER -> {
                mBinding.recyclerView.adapter = singerAdapter
            }
            TYPE_SONG_LIST -> {
                mBinding.recyclerView.adapter = songListAdapter
            }
            TYPE_ALBUM -> {
                mBinding.recyclerView.adapter = albumAdapter
            }
            else -> {}
        }
        mBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mBinding.refreshLayout.setRefreshHeader(ClassicsHeader(requireContext()))
        mBinding.refreshLayout.setRefreshFooter(ClassicsFooter(requireContext()))
    }

    override fun initEvent() {
        mainModel.key.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                if (t != null) {
                    key = t
                    offset = 1
                    load()
                }
            }
        })

        mBinding.refreshLayout.setOnRefreshListener {
            offset = 1
            load()
        }
        mBinding.refreshLayout.setOnLoadMoreListener {
            offset++
            load()
        }

    }

    private fun load() {
        if (key != "") {
            val url = "$BASE_URL$SEARCH?key=$key&type=$type&offset=$offset"
            Log.d(SearchFragment.TAG, "initEvent: $url")
            val request: Request = Request.Builder()
                .url(url)
                .get()
                .build()
            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(TAG, "onFailure: ")
                    finishRefreshOrLoad()
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call, response: Response) {
                    if (response.body != null) {
                        val json = response.body!!.string()
                        Log.d(TAG, "onResponse: $json")
                        when (type) {
                            TYPE_SONG -> {
                                val song = Gson().fromJson(json, SearchSong::class.java)
                                if (song != null && song.musics != null) {
                                    if (offset == 1)
                                        listSong.clear()
                                    listSong.addAll(song.musics)
                                    requireActivity().runOnUiThread {
                                        songAdapter.notifyDataSetChanged()
                                        finishRefreshOrLoad()
                                    }
                                }else{
                                    finishRefreshOrLoad()
                                }
                            }
                            TYPE_SINGER -> {
                                val singer = Gson().fromJson(json, SearchSinger::class.java)
                                if (singer != null && singer.artists != null) {
                                    if (offset == 1)
                                        listSinger.clear()
                                    listSinger.addAll(singer.artists)
                                    requireActivity().runOnUiThread {
                                        singerAdapter.notifyDataSetChanged()
                                        finishRefreshOrLoad()
                                    }
                                }else{
                                    finishRefreshOrLoad()
                                }
                            }
                            TYPE_SONG_LIST -> {
                                val songList = Gson().fromJson(json, SearchSongList::class.java)
                                if (songList != null && songList.songLists != null) {
                                    if (offset == 1)
                                        listSongList.clear()
                                    listSongList.addAll(songList.songLists)
                                    requireActivity().runOnUiThread {
                                        songListAdapter.notifyDataSetChanged()
                                        finishRefreshOrLoad()
                                    }
                                }else{
                                    finishRefreshOrLoad()
                                }
                            }
                            TYPE_ALBUM -> {
                                val album = Gson().fromJson(json, SearchAlbum::class.java)
                                if (album != null && album.albums != null) {
                                    if (offset == 1)
                                        listAlbum.clear()
                                    listAlbum.addAll(album.albums)
                                    requireActivity().runOnUiThread {
                                        albumAdapter.notifyDataSetChanged()
                                        finishRefreshOrLoad()
                                    }
                                }else{
                                    finishRefreshOrLoad()
                                }
                            }
                            else -> {}
                        }
                    }else{
                        finishRefreshOrLoad()
                    }
                }
            })
        }
        else{
            mBinding.refreshLayout.finishRefresh()
            mBinding.refreshLayout.finishLoadMore()
        }
    }

    private fun finishRefreshOrLoad() {
        requireActivity().runOnUiThread {
            mBinding.refreshLayout.finishRefresh()
            mBinding.refreshLayout.finishLoadMore()
        }
    }
    companion object {
        const val TAG = "SearchResultFragment"
        fun newInstance(type: Int) = SearchResultFragment(type)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showPopMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(requireContext(), view, Gravity.END)
        popupMenu.menuInflater.inflate(R.menu.menu_song_long_click, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_love -> {
                    songDaoImpl.addSong(listSong[position])
                }
                R.id.menu_download -> {
                    if (downloadDialog == null) {
                        downloadDialog = DownloadDialog(requireContext(), requireActivity())
                    }
                    downloadDialog!!.downloadSong = listSong[position]
                    downloadDialog!!.setTitle("选择下载音质")
                    downloadDialog!!.show()
                }
                else -> {}
            }
            true
        }
        popupMenu.show()
    }
}