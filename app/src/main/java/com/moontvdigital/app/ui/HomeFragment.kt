package com.moontvdigital.app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.moontvdigital.app.adapter.CarouselAdapter
import com.moontvdigital.app.adapter.HallAdapter
import com.moontvdigital.app.api.ApiService
import com.moontvdigital.app.api.ServiceBuilder
import com.moontvdigital.app.data.BannerImageResponse
import com.moontvdigital.app.data.GetHallsResponse
import com.moontvdigital.app.data.HallData
import com.moontvdigital.app.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val bannerImages: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callBannerDetailsAPi()
        getHallList()
    }

    private fun callBannerDetailsAPi() {
        val request = ServiceBuilder.buildService(ApiService::class.java)
        val call = request.getBannerItems()
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
                    val carouselAdapter =
                        CarouselAdapter(requireContext(), bannerImages)
                    binding.viewPager.adapter = carouselAdapter
                    binding.indicatorTabLayout.setupWithViewPager(binding.viewPager)
                }
            }

            override fun onFailure(call: Call<BannerImageResponse>, t: Throwable) {

            }
        })
    }

    private lateinit var hallListCall: Call<GetHallsResponse>
    private fun getHallList() {
        binding.progressIndicator.visibility = View.VISIBLE
        val request = ServiceBuilder.buildService(ApiService::class.java)
        hallListCall = request.getHallList()
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
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = HallAdapter(hallList, object : HallAdapter.OnItemClickListener {
            override fun onItemClicked(hallData: HallData) {
                Intent(requireContext(), HallDetailsActivity::class.java).apply {
                    putExtra("hall_id", hallData.hallId)
                    putExtra("hall_name", hallData.hallName)
                }.also {
                    startActivity(it)
                }
            }

        })
        binding.allTheatersRecycler.layoutManager = layoutManager
        binding.allTheatersRecycler.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        hallListCall.cancel()
    }
}