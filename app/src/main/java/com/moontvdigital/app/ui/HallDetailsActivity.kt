package com.moontvdigital.app.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.moontvdigital.app.R
import com.moontvdigital.app.adapter.DateAdapter
import com.moontvdigital.app.adapter.ShowTimeAdapter
import com.moontvdigital.app.api.ApiService
import com.moontvdigital.app.api.ServiceBuilder
import com.moontvdigital.app.data.ShowDate
import com.moontvdigital.app.data.ShowTime
import com.moontvdigital.app.data.ShowTimesResponse
import com.moontvdigital.app.data.WalletBalanceResponse
import com.moontvdigital.app.databinding.ActivityHallDetailsBinding
import com.moontvdigital.app.databinding.CustomTicketStubLayoutBinding
import com.moontvdigital.app.utilities.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Date

class HallDetailsActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "HallDetailsActivity"
    }

    private lateinit var binding: ActivityHallDetailsBinding
    private lateinit var curShowDate: ShowDate
    private lateinit var curShowCalDate: Date
    private lateinit var hallName: String
    private var hallId = 0

    private var showTimeList:MutableList<ShowTime?> = mutableListOf()
    private lateinit var showTimeAdapter: ShowTimeAdapter
    private val dateList: MutableList<ShowDate> = mutableListOf()

    val poster = "https://scontent-ccu1-1.cdninstagram.com/v/t51.2885-15/387265536_315305014444045_7149496564671046963_n.webp?stp=dst-jpg_e35_s1080x1080&efg=eyJ2ZW5jb2RlX3RhZyI6ImltYWdlX3VybGdlbi4xNDQweDgxMC5zZHIifQ&_nc_ht=scontent-ccu1-1.cdninstagram.com&_nc_cat=101&_nc_ohc=aM3dHezSmcMAX8BHPge&edm=ACWDqb8BAAAA&ccb=7-5&ig_cache_key=MzIxMDQ5NTc0ODA2NDU2NzUzMw%3D%3D.2-ccb7-5&oh=00_AfBWwo5sDtBEK3CdzzPHH0bv2e8WTOdZcg5aSYmY1bWOGA&oe=653F488C&_nc_sid=ee9879";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHallDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*supportActionBar?.let {
            it.setTitle("")
            it.setDisplayHomeAsUpEnabled(true)
        }*/

        hallName = intent.getStringExtra("hall_name")!!
        hallId = intent.getIntExtra("hall_id", 0)
        binding.tvHallName.text = hallName

        Glide.with(this)
            .asBitmap()
            .load(poster)
            .addListener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    createPaletteAsync(resource!!)
                    return false
                }
            })
            .into(binding.imageView)

        prepareDates()
        setDateRecycler()
        setShowTimeRecycler()


        getWalletBalanceApi()
        getShowTimes()
    }

    private fun setDateRecycler() {
        val layoutManager = LinearLayoutManager(this, HORIZONTAL, false)
        val adapter = DateAdapter(dateList, object : DateAdapter.DateClickedListener {
            override fun onDateClicked(showDate: ShowDate) {
                Log.d(TAG, "onDateClicked: ${showDate.getApiCallDate()}")
                binding.ticketView.root.visibility = View.GONE
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
        val layoutManager = LinearLayoutManager(this, HORIZONTAL, false)
        showTimeAdapter = ShowTimeAdapter(showTimeList, object : ShowTimeAdapter.ShowTimeListener {
            override fun onShowTimeClicked(showTime: ShowTime) {
                showTicketDialog(showTime)
            }

        })
        binding.showTimeRecycler.layoutManager = layoutManager
        binding.showTimeRecycler.adapter = showTimeAdapter
        binding.showTimeRecycler.addItemDecoration(ShowTimeItemDecoration())
    }

    var walletBalance = "0"
    private fun getWalletBalanceApi() {
        val service = ServiceBuilder.buildService(ApiService::class.java)
        val walletBalCall = service.getWalletBalance("21")
        walletBalCall.enqueue(object : Callback<WalletBalanceResponse> {
            override fun onResponse(
                call: Call<WalletBalanceResponse>,
                response: Response<WalletBalanceResponse>
            ) {
                val walletBalanceResponse = response.body()
                walletBalanceResponse?.let {balanceResponse ->
                    if (balanceResponse.code.equals("200")) {
                        val walletData = balanceResponse.walletData?.get(0)
                        walletBalance = walletData?.walletBalance ?: ""
                    }
                }
            }

            override fun onFailure(call: Call<WalletBalanceResponse>, t: Throwable) {
            }
        })
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

    // Generate palette asynchronously and use it on a different thread using onGenerated().
    private fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            // Use generated instance.
            palette.let {
                it?.darkVibrantSwatch

            }.apply {
                Log.d(TAG, "createPaletteAsync: " + this?.bodyTextColor)
                binding.tvHallName.setBackgroundColor(this?.rgb ?: Color.parseColor("#000000"))
            }

        }
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

    private fun showTicketDialog(showTime: ShowTime) {
        /*val ticketStubFragment = TicketStubFragment(hallName, curShowDate, showTime, walletBalance)
        ticketStubFragment.show(supportFragmentManager, TicketStubFragment.TAG)*/
        startActivity(Intent(this, MovieDetailsActivity::class.java))

        /*val builder = AlertDialog.Builder(this, R.style.TicketDialog)
        builder.setView(R.layout.fragment_ticket_stub)
        builder.create().show()*/
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
                    Util.dpToPx(parent.context, 8), 0)
            } else if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) {
                outRect.set(
                    Util.dpToPx(parent.context, 8), 0,
                    Util.dpToPx(parent.context, 32), 0)
            } else {
                outRect.set(
                    Util.dpToPx(parent.context, 8), 0,
                    Util.dpToPx(parent.context, 8), 0)
            }
        }
    }
}