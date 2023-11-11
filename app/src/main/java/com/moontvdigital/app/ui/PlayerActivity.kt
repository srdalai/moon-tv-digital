package com.moontvdigital.app.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.ArrayAdapter
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL
import androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT
import androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moontvdigital.app.R
import com.moontvdigital.app.databinding.ActivityPlayerBinding


class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "PlayerActivity"
    }

    lateinit var binding: ActivityPlayerBinding

    lateinit var videoUrl: String

    var player: ExoPlayer? = null
    var selectedResizeMode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toggleFullscreenButton : ImageButton = findViewById(R.id.toggleFullscreenButton)


        val windowInsetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        // Configure the behavior of the hidden system bars.
        // Configure the behavior of the hidden system bars.
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Add a listener to update the behavior of the toggle fullscreen button when
        // the system bars are hidden or revealed.
        // Add a listener to update the behavior of the toggle fullscreen button when
        // the system bars are hidden or revealed.
        window.decorView.setOnApplyWindowInsetsListener { view: View, windowInsets: WindowInsets ->
            // You can hide the caption bar even when the other system bars are visible.
            // To account for this, explicitly check the visibility of navigationBars()
            // and statusBars() rather than checking the visibility of systemBars().
            if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
                || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())
            ) {
                toggleFullscreenButton.setOnClickListener { v ->
                    // Hide both the status bar and the navigation bar.
                    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
                }
            } else {
                toggleFullscreenButton.setOnClickListener { v ->
                    // Show both the status bar and the navigation bar.
                    windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
                }
            }
            view.onApplyWindowInsets(windowInsets)
        }

        val btnResizeMode : ImageButton = findViewById(R.id.resize_mode)
        btnResizeMode.setOnClickListener {
            showResizeModeDialog()
        }

        player = ExoPlayer.Builder(this).build()
        val mediaItem = MediaItem.fromUri("https://cdn.theoplayer.com/video/big_buck_bunny/big_buck_bunny.m3u8")
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
        binding.playerView.player = player

        setTurnScreenOn(true)
    }

    @androidx.media3.common.util.UnstableApi
    private fun showResizeModeDialog() {
        val resizeModeList = listOf("Fit", "Fill", "Zoom")
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.select_dialog_singlechoice, resizeModeList)
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Resize Mode")
        builder.setSingleChoiceItems(adapter, selectedResizeMode
        ) { dialog, pos ->
            when (pos) {
                0 -> {
                    binding.playerView.resizeMode = RESIZE_MODE_FIT
                }

                1 -> {
                    binding.playerView.resizeMode = RESIZE_MODE_FILL
                }

                2 -> {
                    binding.playerView.resizeMode = RESIZE_MODE_ZOOM
                }
            }
            selectedResizeMode = pos
            dialog.dismiss()
        }
        builder.create().show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        player?.release()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        Log.d(TAG, "onUserLeaveHint: ")
    }
}