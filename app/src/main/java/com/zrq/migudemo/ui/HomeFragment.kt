package com.zrq.migudemo.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tencent.mmkv.MMKV
import com.zrq.migudemo.R
import com.zrq.migudemo.adapter.ListSongAdapter
import com.zrq.migudemo.adapter.PlayBarAdapter
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.databinding.FragmentHomeBinding
import com.zrq.migudemo.interfaces.*
import com.zrq.migudemo.util.Constants
import com.zrq.migudemo.util.Constants.PAGE_LOVE
import com.zrq.migudemo.util.Constants.PAGE_SEARCH
import com.zrq.migudemo.view.SingleDialog
import com.zrq.migudemo.view.VisualizeView
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment<FragmentHomeBinding>(),
    OnSongChangeListener, OnItemClickListener, OnSongDeleteListener,
    OnDialogItemClickListener {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    private lateinit var listDialog: BottomSheetDialog
    private lateinit var listView: View
    private lateinit var listAdapter: ListSongAdapter
    private var list = ArrayList<SearchSong.MusicsDTO>()
    lateinit var adapter: PlayBarAdapter
    private lateinit var mPlayerViewControl: IPlayerViewControl
    private var isPause = false
    private var nowPage = PAGE_LOVE
    private lateinit var bgDialog: SingleDialog
    private lateinit var colorDialog: SingleDialog
    private lateinit var rewardDialog: AlertDialog
    private var mPositionOffset = 0.0f


    override fun initData() {
        initListDialog()
        adapter = PlayBarAdapter(requireActivity(), list)
        mBinding.viewPager2.adapter = adapter
        bgDialog = SingleDialog(
            requireContext(),
            requireActivity(),
            R.layout.dialog_single_background,
            ArrayList<Int>().apply {
                add(R.id.rb_erCiYuan)
                add(R.id.rb_girl)
                add(R.id.rb_animal)
            },
            this
        )
        bgDialog.setTitle("壁纸类型")
        colorDialog = SingleDialog(
            requireContext(),
            requireActivity(),
            R.layout.dialog_single_color,
            ArrayList<Int>().apply {
                add(R.id.rb_pink)
                add(R.id.rb_red)
                add(R.id.rb_purple)
                add(R.id.rb_teal)
                add(R.id.rb_grey)
                add(R.id.rb_blank)
            },
            this
        )
        rewardDialog = AlertDialog.Builder(requireContext(), R.style.TransparentDialog)
            .setView(R.layout.dialog_reward)
            .create()
    }

    override fun initEvent() {
        mPlayerViewControl = object : IPlayerViewControl {

            override fun onSeekChange(progress: Int, elapsedTime: Int) {
                mBinding.seekBar.progress = progress
            }

            override fun onSongPlayOver(position: Int) {
                Log.d(TAG, "onSongPlayOver: ")
                mBinding.viewPager2.setCurrentItem(position, true)
            }

            override fun onVisualizeView(mode: FloatArray) {
                mBinding.visualizeView.setColor(
                    resources.getColor(
                        MMKV.defaultMMKV().decodeInt("color", R.color.pink)
                    )
                )
                mBinding.visualizeView.setData(mode)
                mBinding.visualizeView.setMode(VisualizeView.SINGLE)
            }
        }

        mainModel.registerViewControl(mPlayerViewControl)

        mainModel.onSongChangeListener = this@HomeFragment

        mainModel.isPause.observe(this@HomeFragment) {
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
                listDialog.show()
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
                    R.id.item_reward -> {
                        rewardDialog.show()
                    }
                    R.id.item_background -> {
                        bgDialog.show()
                    }
                    R.id.item_color -> {
                        colorDialog.show()
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

    @SuppressLint("InflateParams")
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
    override fun onSongChange(position: Int) {
        mBinding.ibList.visibility = View.VISIBLE
        mBinding.ibPlayOrPause.visibility = View.VISIBLE
        mBinding.seekBar.visibility = View.VISIBLE
        mBinding.visualizeView.visibility = View.VISIBLE

        list.clear()
        list.addAll(mainModel.getList()!!)
        mainModel.setList(list)
        mBinding.viewPager2.setCurrentItem(position, false)
        mainModel.nowPlaying.postValue(list[position])
        mainModel.playThis(position)
        refreshListDialog()
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

    override fun onDialogItemClick(view: View) {

        when (view.id) {
            R.id.rb_erCiYuan -> {
                MMKV.defaultMMKV().encode("background", Constants.ANIMATION)
            }
            R.id.rb_girl -> {
                MMKV.defaultMMKV().encode("background", Constants.GIRL)
            }
            R.id.rb_animal -> {
                MMKV.defaultMMKV().encode("background", Constants.ANIMAL)
            }
            R.id.rb_pink -> {
                MMKV.defaultMMKV().encode("color", R.color.pink)
                mBinding.visualizeView.setColor(
                    resources.getColor(
                        MMKV.defaultMMKV().decodeInt("color", R.color.pink)
                    )
                )
            }
            R.id.rb_red -> {
                MMKV.defaultMMKV().encode("color", R.color.red)
                mBinding.visualizeView.setColor(
                    resources.getColor(
                        MMKV.defaultMMKV().decodeInt("color", R.color.red)
                    )
                )
            }
            R.id.rb_purple -> {
                MMKV.defaultMMKV().encode("color", R.color.purple_200)
                mBinding.visualizeView.setColor(
                    resources.getColor(
                        MMKV.defaultMMKV().decodeInt("color", R.color.purple_200)
                    )
                )
            }
            R.id.rb_teal -> {
                MMKV.defaultMMKV().encode("color", R.color.teal_200)
                mBinding.visualizeView.setColor(
                    resources.getColor(
                        MMKV.defaultMMKV().decodeInt("color", R.color.teal_200)
                    )
                )
            }
            R.id.rb_grey -> {
                MMKV.defaultMMKV().encode("color", R.color.grey)
                mBinding.visualizeView.setColor(
                    resources.getColor(
                        MMKV.defaultMMKV().decodeInt("color", R.color.grey)
                    )
                )
            }
            R.id.rb_blank -> {
                MMKV.defaultMMKV().encode("color", R.color.black)
                mBinding.visualizeView.setColor(
                    resources.getColor(
                        MMKV.defaultMMKV().decodeInt("color", R.color.black)
                    )
                )
            }
            else -> {}
        }
    }

    companion object {
        const val TAG = "HomeFragment"
    }

}