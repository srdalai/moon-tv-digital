package com.moontvdigital.app.ui

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.moontvdigital.app.R
import com.moontvdigital.app.adapter.DateAdapter
import com.moontvdigital.app.adapter.ShowTimeAdapter
import com.moontvdigital.app.api.ApiService
import com.moontvdigital.app.api.ServiceBuilder
import com.moontvdigital.app.data.ShowDate
import com.moontvdigital.app.data.ShowTime
import com.moontvdigital.app.data.ShowTimesResponse
import com.moontvdigital.app.databinding.FragmentHallDetailsBinding
import com.moontvdigital.app.utilities.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Date

class HallDetailsFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "HallDetailsFragment"
    }

    private var _binding: FragmentHallDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var curShowDate: ShowDate
    private lateinit var curShowCalDate: Date
    private lateinit var hallName: String
    private var hallId = 0

    private var showTimeList:MutableList<ShowTime?> = mutableListOf()
    private lateinit var showTimeAdapter: ShowTimeAdapter
    private val dateList: MutableList<ShowDate> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHallDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hallName = arguments?.getString("hall_name")!!
        hallId = arguments?.getInt("hall_id", 0)!!
        binding.tvHallName.text = hallName

        prepareDates()
        setDateRecycler()
        setShowTimeRecycler()

        getShowTimes()
    }

    private fun setDateRecycler() {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = DateAdapter(dateList, object : DateAdapter.DateClickedListener {
            override fun onDateClicked(showDate: ShowDate) {
                Log.d(TAG, "onDateClicked: ${showDate.getApiCallDate()}")
                showTimeList.clear()
                showTimeAdapter.currentPos = -1
                showTimeAdapter.notifyDataSetChanged()
                curShowDate = showDate
                getShowTimes()
            }
        })
        binding.dateRecycler.layoutManager = layoutManager
        binding.dateRecycler.adapter = adapter
    }

    private fun setShowTimeRecycler() {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        showTimeAdapter = ShowTimeAdapter(showTimeList, object : ShowTimeAdapter.ShowTimeListener {
            override fun onShowTimeClicked(showTime: ShowTime) {
                navigateToMovieDetails(showTime)
            }
        })
        binding.showTimeRecycler.layoutManager = layoutManager
        binding.showTimeRecycler.adapter = showTimeAdapter
        binding.showTimeRecycler.addItemDecoration(ShowTimeItemDecoration())
    }



    private fun getShowTimes() {
        val request = ServiceBuilder.buildService(ApiService::class.java)
        val call = request.getShowTimes(hallId.toString(), /*"2023-10-15"*/curShowDate.getApiCallDate())
        call.enqueue(object : Callback<ShowTimesResponse> {
            override fun onResponse(
                call: Call<ShowTimesResponse>,
                response: Response<ShowTimesResponse>
            ) {
                Log.d(TAG, "onResponse: " + Gson().toJson(response.body()))
                val showTimesResponse: ShowTimesResponse? = response.body()
                if (showTimesResponse?.code.equals("200")) {
                    showTimesResponse?.showTimes.let {
                        it?.toList()?.let { it1 -> showTimeList.addAll(it1) }
                        showTimeAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ShowTimesResponse>, t: Throwable) {
                Log.e(TAG, t.toString())
            }

        })
    }

    private fun prepareDates() {
        val calendar = Calendar.getInstance()
        val curDate = getShowDateFromCal(calendar)
        curShowCalDate = curDate.calDate
        curShowDate = curDate
        dateList.add(curDate)
        for (i in 0..5) {
            calendar.add(Calendar.DATE, 1)
            dateList.add(getShowDateFromCal(calendar))
        }
    }

    private fun navigateToMovieDetails(showTime: ShowTime) {
        startActivity(Intent(requireContext(), MovieDetailsActivity::class.java).apply {
            putExtra("movie_id", showTime.filmId.toString())
        })
    }

    private fun getShowDateFromCal(calendar: Calendar) : ShowDate {
        val date = calendar.get(Calendar.DATE)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val day = getDay(dayOfWeek)
        return ShowDate(calendar.time, date, month, year, day)
    }

    private fun getDay(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            1 -> "Sun"
            2 -> "Mon"
            3 -> "Tue"
            4 -> "Wed"
            5 -> "Thu"
            6 -> "Fri"
            7 -> "Sat"
            else -> ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class ShowTimeItemDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.set(
                    Util.dpToPx(parent.context, 32), 0,
                    Util.dpToPx(parent.context, 4), 0)
            } else if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) {
                outRect.set(
                    Util.dpToPx(parent.context, 4), 0,
                    Util.dpToPx(parent.context, 32), 0)
            } else {
                outRect.set(
                    Util.dpToPx(parent.context, 4), 0,
                    Util.dpToPx(parent.context, 4), 0)
            }
        }
    }

}