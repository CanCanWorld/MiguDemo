package com.zrq.migudemo.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import com.zrq.migudemo.R
import com.zrq.migudemo.adapter.ListSongAdapter
import com.zrq.migudemo.adapter.PlayBarAdapter
import com.zrq.migudemo.bean.Picture
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.databinding.FragmentHomeBinding
import com.zrq.migudemo.interfaces.*
import com.zrq.migudemo.util.Constants
import com.zrq.migudemo.util.Constants.PAGE_LOVE
import com.zrq.migudemo.util.Constants.PAGE_SEARCH
import com.zrq.migudemo.view.*
import okhttp3.*
import java.io.IOException
import java.util.*


class HomeFragment : BaseFragment<FragmentHomeBinding>(),
    OnSongChangeListener, OnItemClickListener, OnSongDeleteListener, OnThemeChangeListener {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    private var listDialog: BottomSheetDialog? = null
    private var listView: View? = null
    private lateinit var listAdapter: ListSongAdapter
    private var list = ArrayList<SearchSong.MusicsDTO>()
    lateinit var adapter: PlayBarAdapter
    private var mPlayerViewControl: IPlayerViewControl? = null
    private var isPause = false
    private var nowPage = PAGE_LOVE
    private var bgDialog: BackgroundDialog? = null
    private var themeDialog: ThemeDialog? = null
    private var playQualityDialog: PlayQualityDialog? = null
    private var pathDialog: PathDialog? = null
    private var rewardDialog: AlertDialog? = null
    private var mPositionOffset = 0.0f

    override fun initData() {
        adapter = PlayBarAdapter(requireActivity(), list)
        mBinding.viewPager2.adapter = adapter
    }

    override fun initEvent() {
        mPlayerViewControl = object : IPlayerViewControl {

            override fun onSeekChange(progress: Int, elapsedTime: Int) {
                mBinding.seekBar.progress = progress
                mainModel.onLyricChangeListener?.onLyricChange(elapsedTime)
            }

            override fun onSongPlayOver(position: Int) {
                mBinding.viewPager2.setCurrentItem(position, true)
            }

            override fun onVisualizeView(mode: FloatArray) {
                mBinding.visualizeView.setData(mode)
            }
        }

        mainModel.registerViewControl(mPlayerViewControl!!)
        mainModel.onSongChangeListener = this
        mainModel.isPause.observe(this) {
            if (it != null) {
                isPause = it
                if (it) {
                    mBinding.ibPlayOrPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    mainModel.playerControl?.pause()
                } else {
                    mBinding.ibPlayOrPause.setImageResource(R.drawable.ic_baseline_pause_24)
                    mainModel.playerControl?.play()
                }
            }
        }

        mBinding.apply {

            ibPlayOrPause.setOnClickListener {
                mainModel.isPause.postValue(!isPause)
            }

            ibList.setOnClickListener {
                if (listView == null) {
                    listView = layoutInflater.inflate(R.layout.dialog_bottom_play_list, null)
                }
                if (listDialog == null) {
                    listDialog = BottomSheetDialog(requireContext())
                }
                listDialog!!.setContentView(listView!!)
                listAdapter =
                    ListSongAdapter(requireContext(), list, this@HomeFragment, this@HomeFragment)
                listView!!.findViewById<RecyclerView>(R.id.rv_song_list).apply {
                    adapter = listAdapter
                    layoutManager = LinearLayoutManager(requireContext())
                }
                refreshListDialog()
                listDialog!!.show()
            }

            ibLove.setOnClickListener {
                if (nowPage != PAGE_LOVE) {
                    nowPage = PAGE_LOVE
                    Navigation.findNavController(requireActivity(), R.id.fragment_home_container)
                        .navigate(R.id.action_global_loveFragment)
                }
            }

            ibSearch.setOnClickListener {
                if (nowPage != PAGE_SEARCH) {
                    nowPage = PAGE_SEARCH
                    Navigation.findNavController(requireActivity(), R.id.fragment_home_container)
                        .navigate(R.id.action_global_searchFragment)
                }
            }

            ibSetting.setOnClickListener {
                val headerView = mBinding.navigationView.getHeaderView(0)
                val iv = headerView.findViewById<ImageView>(R.id.iv_head)
                loadHeadPic(iv)
                drawerLayout.openDrawer(GravityCompat.START)
            }

            viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Log.d(TAG, "mPositionOffset: $mPositionOffset")
                    if (mPositionOffset > 0) {
                        mainModel.nowPlaying.postValue(list[position])
                        mainModel.playThis(position)
                    }
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    mPositionOffset = positionOffset
                }

            })


            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mainModel.playerControl?.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })

            navigationView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.item_home -> {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    R.id.item_background -> {
                        if (bgDialog == null) {
                            bgDialog = BackgroundDialog(
                                requireContext(),
                                requireActivity(),
                                mainModel.onBackgroundChangeListener
                            )
                            bgDialog!!.setTitle("壁纸类型")
                        }
                        bgDialog!!.show()
                    }
                    R.id.item_color -> {
                        if (themeDialog == null) {
                            themeDialog =
                                ThemeDialog(requireContext(), requireActivity(), this@HomeFragment)
                            themeDialog!!.setTitle("配色选择")
                        }
                        themeDialog!!.show()
                    }
                    R.id.item_quality -> {
                        if (playQualityDialog == null) {
                            playQualityDialog =
                                PlayQualityDialog(requireContext(), requireActivity())
                            playQualityDialog!!.setTitle("播放音质")
                        }
                        playQualityDialog!!.show()
                    }
                    R.id.item_download_path -> {
                        if (pathDialog == null) {
                            pathDialog = PathDialog(requireContext(), requireActivity())
                        }
                        pathDialog!!.show()
                    }
                    R.id.item_update -> {
                        Toast.makeText(requireContext(), "已是最新版本", Toast.LENGTH_SHORT).show()
                    }
                    R.id.item_reward -> {
                        if (rewardDialog == null) {
                            rewardDialog =
                                AlertDialog.Builder(requireContext(), R.style.TransparentDialog)
                                    .setView(R.layout.dialog_reward)
                                    .create()
                        }
                        rewardDialog?.show()
                        Toast.makeText(requireContext(), "给点钱吧，孩子吃不起饭了", Toast.LENGTH_SHORT).show()
                    }
                    R.id.item_customer -> {
                        val url = "mqqwpa://im/chat?chat_type=wpa&uin=3254956566&version=1"
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    }
                    R.id.item_exit -> {
                        requireActivity().finish()
                    }
                    else -> {

                    }
                }
                false
            }

        }
    }


    private fun loadHeadPic(iv: ImageView) {
        val random = Random(Date().time).nextInt(1000)
        val bgCategory = MMKV.defaultMMKV().decodeString("background", Constants.ANIMATION)
            ?: Constants.ANIMATION
        val url = Constants.getPicByCategory(bgCategory, 1, random)
        Log.d(TAG, "loadHeadPic: $url")
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
                                .load(result.res.vertical[0].thumb)
                                .into(iv)
                        }
                    }
                }
            }
        })
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun refreshListDialog() {
        listAdapter.notifyDataSetChanged()
        listView!!.findViewById<TextView>(R.id.tv_list_size).text = "(${list.size})"
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSongChange(position: Int) {
        mBinding.ibList.visibility = View.VISIBLE
        mBinding.ibPlayOrPause.visibility = View.VISIBLE
        mBinding.seekBar.visibility = View.VISIBLE
        mBinding.visualizeView.visibility = View.VISIBLE
        mBinding.visualizeView.setMode(VisualizeView.SINGLE)

        list.clear()
        list.addAll(mainModel.getList()!!)
        mainModel.setList(list)
        mBinding.viewPager2.setCurrentItem(position, false)
        mainModel.nowPlaying.postValue(list[position])
        mainModel.playThis(position)
        adapter.notifyDataSetChanged()

        mainModel.isPause.postValue(false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSongDelete(position: Int) {
        list.removeAt(position)
        mainModel.setList(list)
        refreshListDialog()
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemClick(view: View, position: Int) {
        mBinding.viewPager2.setCurrentItem(position, false)
        mainModel.nowPlaying.postValue(list[position])
        mainModel.playThis(position)
    }

    override fun onThemeChange() {
        requireActivity().apply {
            finish()
            startActivity(intent.apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK })
            overridePendingTransition(0, 0)
        }
    }

    companion object {
        const val TAG = "HomeFragment"
    }

}