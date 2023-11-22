package com.moontvdigital.app.ui

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING
import androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE
import com.moontvdigital.app.adapter.CarouselAdapter
import com.moontvdigital.app.adapter.HallAdapter
import com.moontvdigital.app.adapter.HomeContentAdapter
import com.moontvdigital.app.api.ApiService
import com.moontvdigital.app.api.ServiceBuilder
import com.moontvdigital.app.data.BannerImageResponse
import com.moontvdigital.app.data.GetHallsResponse
import com.moontvdigital.app.data.HallData
import com.moontvdigital.app.data.HomeContent
import com.moontvdigital.app.data.HomeContentResponse
import com.moontvdigital.app.databinding.FragmentHomeBinding
import com.moontvdigital.app.utilities.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Timer
import java.util.TimerTask

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    private var _binding: FragmentHomeBinding? = null

    private lateinit var apiService: ApiService
    private lateinit var hallListCall: Call<GetHallsResponse>
    private lateinit var getMoviesCall: Call<HomeContentResponse>
    private lateinit var getSongsCall: Call<HomeContentResponse>
    private lateinit var getVideosCall: Call<HomeContentResponse>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val bannerImages: MutableList<String> = mutableListOf()
    private var isBannerSet = false

    private var curBannerPos = 0
    private var bannerTimer: Timer? = null

    private fun autoNextBanner() {
        if (curBannerPos < bannerImages.size) {
            curBannerPos++
        } else {
            curBannerPos = 0
        }
        updateBanner()
    }
    private fun nextBanner() {
        if (curBannerPos < bannerImages.size) {
            curBannerPos++
            updateBanner()
        }
    }

    private fun previousBanner() {
        if (curBannerPos > 0) {
            curBannerPos--
            updateBanner()
        }
    }

    private fun setBannerTimer() {
        bannerTimer?.cancel()
        bannerTimer = Timer()
        bannerTimer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                autoNextBanner()
            }
        }, 2000, 2000)
    }

    private fun updateBanner() {
        requireActivity().runOnUiThread {
            binding.viewPager.setCurrentItem(curBannerPos)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = ServiceBuilder.buildService(ApiService::class.java)

        callBannerDetailsAPi()
        getHallList()
        getMovies()
        getSongs()
        getVideos()
    }

    private fun callBannerDetailsAPi() {
        bannerImages.clear()
        val call = apiService.getBannerItems()
        call.enqueue(object : Callback<BannerImageResponse> {
            override fun onResponse(
                call: Call<BannerImageResponse>,
                response: Response<BannerImageResponse>
            ) {
                val bannerImageResponse = response.body()
                if (bannerImageResponse?.code.equals("200")) {
                    for (bannerItem in bannerImageResponse?.bannerItems!!) {
                        bannerImages.add(bannerItem?.filePath ?: "")
                    }
                    setBanner()
                }
            }

            override fun onFailure(call: Call<BannerImageResponse>, t: Throwable) {

            }
        })
    }
    
    private fun setBanner() {
        val carouselAdapter = CarouselAdapter(requireContext(), bannerImages)
        binding.viewPager.adapter = carouselAdapter
        binding.indicatorTabLayout.setupWithViewPager(binding.viewPager)
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                curBannerPos = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                //Log.d(TAG, "onPageScrollStateChanged: $state")
                if (state == SCROLL_STATE_DRAGGING) {
                    bannerTimer?.cancel()
                } else if (state == SCROLL_STATE_IDLE) {
                    setBannerTimer()
                }
            }
        })
        isBannerSet = true
        if (bannerImages.size > 1) {
            setBannerTimer()
        }
    }

    private fun getHallList() {
        binding.progressIndicator.visibility = View.VISIBLE
        hallListCall = apiService.getHallList()
        hallListCall.enqueue(object : Callback<GetHallsResponse> {
            override fun onResponse(
                call: Call<GetHallsResponse>,
                response: Response<GetHallsResponse>
            ) {
                if (hallListCall.isCanceled) return
                binding.progressIndicator.visibility = View.GONE
                val hallsResponse: GetHallsResponse? = response.body()
                if (hallsResponse?.code.equals("200")) {
                    hallsResponse?.hallDataList?.let { setHallRecycler(it) }
                }
            }

            override fun onFailure(call: Call<GetHallsResponse>, t: Throwable) {
                if (hallListCall.isCanceled) return
                binding.progressIndicator.visibility = View.GONE
                Log.e(TAG, t.toString())
            }
        })
    }

    private fun setHallRecycler(hallList: List<HallData?>) {
        val layoutManager =
            GridLayoutManager(requireContext(), 2)
        val adapter = HallAdapter(hallList, object : HallAdapter.OnItemClickListener {
            override fun onItemClicked(hallData: HallData) {
                /*Intent(requireContext(), HallDetailsActivity::class.java).apply {
                    putExtra("hall_id", hallData.hallId)
                    putExtra("hall_name", hallData.hallName)
                }.also {
                    startActivity(it)
                }*/
                val detailsFrag = HallDetailsFragment()
                val bundle = Bundle().apply {
                    putInt("hall_id", hallData.hallId ?: 0)
                    putString("hall_name", hallData.hallName)
                }
                detailsFrag.arguments = bundle
                detailsFrag.show(parentFragmentManager, "TAG")
            }

        })
        binding.allTheatersRecycler.layoutManager = layoutManager
        binding.allTheatersRecycler.adapter = adapter
    }

    private fun getMovies() {
        binding.progressIndicator.visibility = View.VISIBLE
        getMoviesCall = apiService.getMoviesList()
        getMoviesCall.enqueue(object : Callback<HomeContentResponse> {
            override fun onResponse(
                call: Call<HomeContentResponse>,
                response: Response<HomeContentResponse>
            ) {
                if (getMoviesCall.isCanceled) return
                binding.progressIndicator.visibility = View.GONE
                val homeContentResponse: HomeContentResponse? = response.body()
                if (homeContentResponse?.code.equals("200")) {
                    homeContentResponse?.homeContents?.let { setMoviesRecycler(it) }
                }
            }

            override fun onFailure(call: Call<HomeContentResponse>, t: Throwable) {
                if (getMoviesCall.isCanceled) return
                binding.progressIndicator.visibility = View.GONE
                Log.e(TAG, t.toString())
            }

        })
    }

    private fun setMoviesRecycler(homeContents: List<HomeContent>) {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = HomeContentAdapter(homeContents, object :
            HomeContentAdapter.OnItemClickListener {
            override fun onItemClicked(homeContent: HomeContent) {
                Toast.makeText(requireContext(), homeContent.filmName, Toast.LENGTH_SHORT).show()
            }
        })
        binding.moviesRecycler.layoutManager = layoutManager
        binding.moviesRecycler.adapter = adapter
        //binding.moviesRecycler.addItemDecoration(HomeContentItemDecoration())

        binding.tvMovies.visibility = View.VISIBLE
        binding.moviesRecycler.visibility = View.VISIBLE
    }

    private fun getSongs() {
        binding.progressIndicator.visibility = View.VISIBLE
        getSongsCall = apiService.getSongList()
        getSongsCall.enqueue(object : Callback<HomeContentResponse> {
            override fun onResponse(
                call: Call<HomeContentResponse>,
                response: Response<HomeContentResponse>
            ) {
                if (getSongsCall.isCanceled) return
                binding.progressIndicator.visibility = View.GONE
                val homeContentResponse: HomeContentResponse? = response.body()
                if (homeContentResponse?.code.equals("200")) {
                    homeContentResponse?.homeContents?.let { setSongsRecycler(it) }
                }
            }

            override fun onFailure(call: Call<HomeContentResponse>, t: Throwable) {
                if (getSongsCall.isCanceled) return
                binding.progressIndicator.visibility = View.GONE
                Log.e(TAG, t.toString())
            }

        })
    }

    private fun setSongsRecycler(homeContents: List<HomeContent>) {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = HomeContentAdapter(homeContents, object :
            HomeContentAdapter.OnItemClickListener {
            override fun onItemClicked(homeContent: HomeContent) {
                Toast.makeText(requireContext(), homeContent.filmName, Toast.LENGTH_SHORT).show()
            }
        })
        binding.songsRecycler.layoutManager = layoutManager
        binding.songsRecycler.adapter = adapter
        //binding.songsRecycler.addItemDecoration(HomeContentItemDecoration())

        binding.tvSongs.visibility = View.VISIBLE
        binding.songsRecycler.visibility = View.VISIBLE
    }

    private fun getVideos() {
        binding.progressIndicator.visibility = View.VISIBLE
        getVideosCall = apiService.getUpcomingVideos()
        getVideosCall.enqueue(object : Callback<HomeContentResponse> {
            override fun onResponse(
                call: Call<HomeContentResponse>,
                response: Response<HomeContentResponse>
            ) {
                if (getVideosCall.isCanceled) return
                binding.progressIndicator.visibility = View.GONE
                val homeContentResponse: HomeContentResponse? = response.body()
                if (homeContentResponse?.code.equals("200")) {
                    homeContentResponse?.homeContents?.let { setVideosRecycler(it) }
                }
            }

            override fun onFailure(call: Call<HomeContentResponse>, t: Throwable) {
                if (getVideosCall.isCanceled) return
                binding.progressIndicator.visibility = View.GONE
                Log.e(TAG, t.toString())
            }

        })
    }

    private fun setVideosRecycler(homeContents: List<HomeContent>) {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = HomeContentAdapter(homeContents, object :
            HomeContentAdapter.OnItemClickListener {
            override fun onItemClicked(homeContent: HomeContent) {
                Toast.makeText(requireContext(), homeContent.filmName, Toast.LENGTH_SHORT).show()
            }
        })
        binding.videosRecycler.layoutManager = layoutManager
        binding.videosRecycler.adapter = adapter
        //binding.videosRecycler.addItemDecoration(HomeContentItemDecoration())

        binding.tvVideos.visibility = View.VISIBLE
        binding.videosRecycler.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        if (isBannerSet) {
            setBannerTimer()
        }
    }

    override fun onPause() {
        super.onPause()
        bannerTimer?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (::hallListCall.isInitialized)
            hallListCall.cancel()
        if (::getMoviesCall.isInitialized)
            getMoviesCall.cancel()
        if (::getSongsCall.isInitialized)
            getSongsCall.cancel()
        if (::getVideosCall.isInitialized)
            getVideosCall.cancel()
    }

    private inner class HomeContentItemDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.set(
                    Util.dpToPx(parent.context, 20), 0,
                    Util.dpToPx(parent.context, 8), 0)
            } else if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) {
                outRect.set(
                    Util.dpToPx(parent.context, 8), 0,
                    Util.dpToPx(parent.context, 20), 0)
            } else {
                outRect.set(
                    Util.dpToPx(parent.context, 8), 0,
                    Util.dpToPx(parent.context, 8), 0)
            }
        }
    }
}