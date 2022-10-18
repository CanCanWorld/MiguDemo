package com.zrq.migudemo.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import com.zrq.migudemo.R
import com.zrq.migudemo.adapter.LoveSongAdapter
import com.zrq.migudemo.bean.Picture
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.dao.SongDaoImpl
import com.zrq.migudemo.databinding.FragmentLoveBinding
import com.zrq.migudemo.db.SongDatabaseHelper
import com.zrq.migudemo.interfaces.OnBackgroundChangeListener
import com.zrq.migudemo.interfaces.OnItemClickListener
import com.zrq.migudemo.interfaces.OnMoreClickListener
import com.zrq.migudemo.util.Constants
import com.zrq.migudemo.view.DownloadDialog
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class LoveFragment : BaseFragment<FragmentLoveBinding>(), OnItemClickListener,
    OnMoreClickListener, OnBackgroundChangeListener {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoveBinding {
        return FragmentLoveBinding.inflate(inflater, container, false)
    }

    private lateinit var songDaoImpl: SongDaoImpl
    private lateinit var adapter: LoveSongAdapter
    private val listSong = ArrayList<SearchSong.MusicsDTO>()
    private var random = 0
    private var downloadDialog: DownloadDialog? = null

    override fun initData() {
        loadBg()
        songDaoImpl = SongDaoImpl(SongDatabaseHelper(requireContext()))
        listSong.clear()
        listSong.addAll(songDaoImpl.listAllSongs())
        adapter = LoveSongAdapter(requireContext(), listSong, this, this)
        mBinding.rvLoveSong.adapter = adapter
        mBinding.rvLoveSong.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun initEvent() {

        mainModel.onBackgroundChangeListener = this

        mBinding.apply {

        }

    }

    companion object {
        const val TAG = "LoveFragment"
    }

    override fun onItemClick(view: View, position: Int) {
        mainModel.setList(listSong)
        mainModel.onSongChangeListener?.onSongChange(position)
    }

    @SuppressLint( "NotifyDataSetChanged")
    private fun showPopMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(requireContext(), view, Gravity.END)
        popupMenu.menuInflater.inflate(R.menu.menu_love_long_click, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_not_love -> {
                    songDaoImpl.deleteSong(listSong[position].id.toInt())
                    listSong.removeAt(position)
                    adapter.notifyDataSetChanged()
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

    private fun loadBg() {
        random = Random(Date().time).nextInt(1000)
        val bgCategory = MMKV.defaultMMKV().decodeString("background", Constants.ANIMATION)
            ?: Constants.ANIMATION
        val url = Constants.getPicByCategory(bgCategory, 1, random)
        Log.d(TAG, "load: $url")
        val request: Request = Request.Builder()
            .url(url)
            .method("GET", null)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "onFailure: ")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
                    val string = response.body?.string()
                    val result = Gson().fromJson(string, Picture::class.java)
                    if (result != null && result.res != null && result.res.vertical != null) {
                        requireActivity().runOnUiThread {
                            Glide.with(requireActivity())
                                .load(result.res.vertical[0].img)
                                .into(mBinding.ivLoveBg)
                        }
                    }
                }
            }
        })
    }

    override fun onMoreClick(view: View, position: Int) {
        showPopMenu(view, position)
    }

    override fun onBackgroundChange() {
        loadBg()
    }
}