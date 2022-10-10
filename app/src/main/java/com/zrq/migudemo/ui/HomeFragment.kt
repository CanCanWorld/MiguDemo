package com.zrq.migudemo.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zrq.migudemo.R
import com.zrq.migudemo.adapter.ListSongAdapter
import com.zrq.migudemo.adapter.PlayBarAdapter
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.databinding.FragmentHomeBinding
import com.zrq.migudemo.interfaces.IPlayerViewControl
import com.zrq.migudemo.interfaces.OnItemClickListener
import com.zrq.migudemo.interfaces.OnSongChangeListener
import com.zrq.migudemo.interfaces.OnSongDeleteListener
import com.zrq.migudemo.view.VisualizeView
import kotlin.collections.ArrayList

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
    private lateinit var listAdapter: ListSongAdapter
    private var list = ArrayList<SearchSong.MusicsDTO>()
    lateinit var adapter: PlayBarAdapter
    private lateinit var mPlayerViewControl: IPlayerViewControl
    private var isPause = false

    override fun initData() {
        Log.d(TAG, "initData: ")
        initListDialog()
        adapter = PlayBarAdapter(requireActivity(), list)
        mBinding.viewPager2.adapter = adapter
    }

    override fun initEvent() {
        mPlayerViewControl = object : IPlayerViewControl {

            override fun onSeekChange(progress: Int, elapsedTime: Int) {
                Log.d(TAG, "onSeekChange: $progress")
            }

            override fun onSongPlayOver(position: Int) {
                Log.d(TAG, "onSongPlayOver: ")
                mBinding.viewPager2.setCurrentItem(position, true)
            }

            override fun onVisualizeView(mode: FloatArray) {
                mBinding.visualizeView.setColor(resources.getColor(R.color.red_et))
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

            viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Log.d(TAG, "onPageSelected: $position")
                    mainModel.nowPlaying.postValue(list[position])
                    mainModel.playThis(position)
                }
            })
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
    override fun onSongChange(position: Int) {
        mBinding.ibList.visibility = View.VISIBLE
        mBinding.ibPlayOrPause.visibility = View.VISIBLE
        list.clear()
        list.addAll(mainModel.getList()!!)
        mainModel.setList(list)
        mBinding.viewPager2.setCurrentItem(position, false)
        refreshListDialog()
        adapter.notifyDataSetChanged()
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
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val TAG = "HomeFragment"
    }

}