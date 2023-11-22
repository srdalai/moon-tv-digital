package com.moontvdigital.app.ui

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moontvdigital.app.R
import com.moontvdigital.app.adapter.CarouselAdapter
import com.moontvdigital.app.adapter.CastAdapter
import com.moontvdigital.app.adapter.DetailsCarouselAdapter
import com.moontvdigital.app.api.ApiService
import com.moontvdigital.app.api.ServiceBuilder
import com.moontvdigital.app.data.CastItem
import com.moontvdigital.app.data.MovieDetailsResponse
import com.moontvdigital.app.data.MovieItem
import com.moontvdigital.app.data.ShowDate
import com.moontvdigital.app.data.ShowTime
import com.moontvdigital.app.databinding.ActivityMovieDetailsBinding
import com.moontvdigital.app.databinding.DialogCastDetailsBinding
import com.moontvdigital.app.databinding.DialogMovieDescLayoutBinding
import com.moontvdigital.app.utilities.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class MovieDetailsActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MovieDetailsActivity"
    }

    private lateinit var binding: ActivityMovieDetailsBinding

    val poster =
        "https://scontent-ccu1-1.cdninstagram.com/v/t51.2885-15/387265536_315305014444045_7149496564671046963_n.webp?stp=dst-jpg_e35_s1080x1080&efg=eyJ2ZW5jb2RlX3RhZyI6ImltYWdlX3VybGdlbi4xNDQweDgxMC5zZHIifQ&_nc_ht=scontent-ccu1-1.cdninstagram.com&_nc_cat=101&_nc_ohc=aM3dHezSmcMAX8BHPge&edm=ACWDqb8BAAAA&ccb=7-5&ig_cache_key=MzIxMDQ5NTc0ODA2NDU2NzUzMw%3D%3D.2-ccb7-5&oh=00_AfBWwo5sDtBEK3CdzzPHH0bv2e8WTOdZcg5aSYmY1bWOGA&oe=653F488C&_nc_sid=ee9879";

    var isLoggedIn = false
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var movieId: String
    private var movieItem: MovieItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieId = intent.getStringExtra("movie_id")!!

        preferenceManager = PreferenceManager.getInstance(this)

        setSupportActionBar(binding.materialToolbar)

        supportActionBar?.let {
            it.title = ""
            it.setDisplayHomeAsUpEnabled(true)
            //it.setBackgroundDrawable(getDrawable(R.drawable.details_toolbar_bg))
            it.setHomeAsUpIndicator(R.drawable.default_back_light)
        }

        /*Glide.with(this)
            .asBitmap()
            .load(poster)
            .into(binding.imageViewBanner)*/

        /*val adapter = CastAdapter()
        binding.castRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.castRecycler.adapter = adapter*/

        setListeners()
        callMovieDetailsApi()
    }

    private fun setListeners() {
        binding.tvMovieDesc.setOnClickListener { showStoryDialog() }
        binding.trailerLinear.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java).apply {
                putExtra("vide_url", movieItem?.trailerPath)
            })
        }
        binding.playLinear.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java).apply {
                putExtra("vide_url", movieItem?.filmPath)
            })
        }
        binding.rentLinear.setOnClickListener {
            if (isLoggedIn) {
                showTicketDialog()
            } else {
                startActivity(Intent(this, AuthActivity::class.java))
            }
        }
    }

    private fun callMovieDetailsApi() {
        val service = ServiceBuilder.buildService(ApiService::class.java)
        val detailsCall = service.getFilmDetails(movieId)
        detailsCall.enqueue(object : Callback<MovieDetailsResponse> {
            override fun onResponse(
                call: Call<MovieDetailsResponse>,
                response: Response<MovieDetailsResponse>
            ) {
                if (response.isSuccessful) {
                    val detailsResponse = response.body()
                    if (detailsResponse?.code.equals("200")) {
                        movieItem = detailsResponse?.data?.get(0)
                        handleMovieDetails()
                    }
                }
            }

            override fun onFailure(call: Call<MovieDetailsResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ", t)
            }

        })
    }

    private fun handleMovieDetails() {
        setCarousel()
        movieItem?.let {
            binding.tvMovieName.text = it.filmName
            binding.tvRuntime.text = it.filmDuration
            binding.tvGenres.text = it.filmGenre
            binding.tvMovieDesc.text =
                Html.fromHtml(it.filmLongDescription, Html.FROM_HTML_MODE_LEGACY)

            if (it.hasTrailer.equals("1")) {
                binding.trailerLinear.visibility = View.VISIBLE
            } else {
                binding.trailerLinear.visibility = View.GONE
            }

            val adapter = CastAdapter(it.castItems!!, object : CastAdapter.CastClickListener {
                override fun onCastItemClick(castItem: CastItem) {
                    //showCastSheetDialog(castItem)
                }
            })
            binding.castRecycler.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.castRecycler.adapter = adapter
        }

    }

    private fun setCarousel() {
        val bannerUrls: MutableList<String> = mutableListOf()
        movieItem?.bannerItems?.stream()?.forEach {
            bannerUrls.add(it.bannerPath!!)
        }
        val carouselAdapter =
            DetailsCarouselAdapter(this, bannerUrls)
        binding.viewPager.adapter = carouselAdapter
        binding.indicatorTabLayout.setupWithViewPager(binding.viewPager)
    }

    private fun showTicketDialog() {
        val ticketStubFragment = TicketStubFragment(
            "INOX",
            ShowDate(Calendar.getInstance().time, 6, 11, 23, "MON"),
            ShowTime(filmName = "Malyagiri", showStartTime = "03:25 PM", ticketRate = 200)
        )
        ticketStubFragment.show(supportFragmentManager, TicketStubFragment.TAG)
    }

    override fun onResume() {
        super.onResume()
        isLoggedIn = preferenceManager.userId != null
    }

    private fun showCastSheetDialog(castItem: CastItem) {
        val castDialogBinding = DialogCastDetailsBinding.inflate(layoutInflater)
        //val dialogView = layoutInflater.inflate(R.layout.dialog_cast_details, null)

        //val ivCastImage: ImageView = dialogView.findViewById(R.id.ivCastImage)

        Glide.with(this).load(castItem.actorImagePath).into(castDialogBinding.ivCastImage)

        val castDialog = BottomSheetDialog(this, R.style.CastDialog)
        castDialog.setContentView(castDialogBinding.root)
        castDialog.show()
    }

    private fun showStoryDialog() {
        val dialogMovieDescBinding = DialogMovieDescLayoutBinding.inflate(layoutInflater)
        dialogMovieDescBinding.textView.text = movieItem?.filmLongDescription
        val dialogBuilder = MaterialAlertDialogBuilder(this)
        dialogBuilder.setView(dialogMovieDescBinding.root)
        dialogBuilder.create().show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}