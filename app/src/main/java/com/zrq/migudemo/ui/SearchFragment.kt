package com.zrq.migudemo.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.zrq.migudemo.R
import com.zrq.migudemo.adapter.OnItemClickListener
import com.zrq.migudemo.adapter.SearchAdapter
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.databinding.FragmentSearchBinding
import com.zrq.migudemo.util.Constants.BASE_URL
import com.zrq.migudemo.util.Constants.SEARCH
import okhttp3.*
import java.io.IOException

class SearchFragment : BaseFragment<FragmentSearchBinding>(), OnItemClickListener {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    private val list = ArrayList<SearchSong.MusicsDTO>()
    private lateinit var adapter: SearchAdapter

    override fun initData() {
        adapter = SearchAdapter(requireContext(), list, this)
        mBinding.rvSearch.adapter = adapter
        mBinding.rvSearch.layoutManager = LinearLayoutManager(context)
    }

    override fun initEvent() {
        mBinding.btnSearch.setOnClickListener {
            val searchStr = mBinding.etSearch.text.toString()
            if (searchStr.isNotEmpty()) {
                val url = "$BASE_URL$SEARCH?key=$searchStr"
                Log.d(TAG, "initEvent: $url")
                val request: Request = Request.Builder()
                    .url(url)
                    .get()
                    .build()
                OkHttpClient().newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d(TAG, "onFailure: ")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.body != null) {
                            val json = response.body!!.string()
                            Log.d(TAG, "onResponse: $json")
                            val song = Gson().fromJson(json, SearchSong::class.java)
                            list.clear()
                            list.addAll(song.musics)
                            requireActivity().runOnUiThread {
                                adapter.notifyItemRangeInserted(0, list.size)
                            }
                        }
                    }
                })
            }
        }
    }

    companion object {
        const val TAG = "SearchFragment"
    }

    override fun onClick(view: View, position: Int) {
        mainModel.nowPlaying.postValue(list[position])
        Navigation.findNavController(requireActivity(), R.id.fragment_container)
            .navigate(R.id.action_global_playFragment)
    }

    override fun onLongClick(view: View, position: Int): Boolean {
        return true
    }

}