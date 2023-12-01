package com.example.mediamusickotlin

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.media.MediaPlayer
import android.media.audiofx.AudioEffect
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.mediamusickotlin.databinding.ActivityPlayerBinding
import com.example.mediamusickotlin.extension.getImageSong
import com.example.mediamusickotlin.extension.showImgSong
import com.example.mediamusickotlin.model.Music
import com.example.mediamusickotlin.service.MusicService
import com.example.mediamusickotlin.utils.Const.REQUEST_EQUALIZER
import com.example.mediamusickotlin.utils.ExitApplicationUtil.Companion.exitApplication
import com.example.mediamusickotlin.utils.FormatUtil
import com.example.mediamusickotlin.utils.SetSongPositionUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PlayerActivity : AppCompatActivity(), ServiceConnection, MediaPlayer.OnCompletionListener {

    private val setSongPositionUtil by lazy { SetSongPositionUtil() }
    private val formatUtil by lazy { FormatUtil() }

    companion object {
        lateinit var musicListPA: ArrayList<Music>
        var songPosition: Int = 0
        var musicService: MusicService? = null
        var isPlaying: Boolean = false
        var repeat: Boolean = false
        var min15: Boolean = false
        var min30: Boolean = false
        var min60: Boolean = false

        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityPlayerBinding
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityPlayerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        initializeLayout()
        /*createMediaPlayer()*/
        startService()
    }

    private fun initView() {
        /*setLayout()*/
    }

    private fun initListener() {
        clickPlayOrPause()
        clickPrevious()
        clickNext()
        clickSeekBar()
        clickRepeat()
        clickBack()
        clickEqualizer()
        clickTimer()
        clickShare()
    }

    private fun initializeLayout() {
        songPosition = intent.getIntExtra("pos", 0)
        when (intent.getStringExtra("class")) {
            "MusicAdapterSearch" -> {
                musicListPA = arrayListOf()
                musicListPA.addAll(MainActivity.musicListSearch)
                setLayout()
            }
            "MusicAdapter" -> {
                musicListPA = arrayListOf()
                musicListPA.addAll(MainActivity.musicListMA)
                setLayout()
            }
            "MainActivity" -> {
                musicListPA = arrayListOf()
                musicListPA.addAll(MainActivity.musicListMA)
                musicListPA.shuffle()
                setLayout()
            }
        }
    }

    private fun createMediaPlayer() {
        try {
            if (musicService!!.mediaPlayer == null) {
                musicService!!.mediaPlayer = MediaPlayer()
            }
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
            isPlaying = true
            binding.playPauseBtnPA.setIconResource(R.drawable.ic_pause)
            musicService!!.showNotification(R.drawable.ic_pause)
            createSeekBar()
        } catch (e: java.lang.Exception) {
            return
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setLayout() {
        showImgSong(this,musicListPA[songPosition].path, binding.songImgPA)
//        Glide.with(this).load(this.getImageSong(musicListPA[songPosition].path))
//            .placeholder(this.getDrawable(R.drawable.ic_default_music)).into(binding.songImgPA)
        binding.songNamePA.text = musicListPA[songPosition].title
        if (repeat) {
            binding.repeatBtnPA.setColorFilter(ContextCompat.getColor(this, R.color.purple_500))
        }
        if (min15 || min30 || min60) {
            binding.timerBtnPA.setColorFilter(ContextCompat.getColor(this, R.color.purple_500))
        }
    }

    private fun playMusic() {
        binding.playPauseBtnPA.setIconResource(R.drawable.ic_pause)
        musicService!!.showNotification(R.drawable.ic_pause)
        isPlaying = true
        musicService!!.mediaPlayer!!.start()
    }

    private fun pauseMusic() {
        binding.playPauseBtnPA.setIconResource(R.drawable.ic_play)
        musicService!!.showNotification(R.drawable.ic_play)
        isPlaying = false
        musicService!!.mediaPlayer!!.pause()
    }

    private fun prevNextSong(increment: Boolean) {
        if (increment) {
            setSongPositionUtil.setSongPosition(increment = true)
            setLayout()
            createMediaPlayer()
        } else {
            setSongPositionUtil.setSongPosition(increment = false)
            setLayout()
            createMediaPlayer()
        }
    }

    private fun clickPlayOrPause() {
        binding.playPauseBtnPA.setOnClickListener {
            if (isPlaying) {
                pauseMusic()
            } else {
                playMusic()
            }
        }
    }

    private fun clickPrevious() {
        binding.previousBtnPA.setOnClickListener {
            prevNextSong(increment = false)
        }
    }

    private fun clickNext() {
        binding.nextBtnPA.setOnClickListener {
            prevNextSong(increment = true)
        }
    }

    private fun clickBack() {
        binding.backBtnPA.setOnClickListener {
            finish()
        }
    }

    private fun clickRepeat() {
        binding.repeatBtnPA.setOnClickListener {
            if (!repeat) {
                repeat = true
                binding.repeatBtnPA.setColorFilter(ContextCompat.getColor(this, R.color.purple_500))
            } else {
                repeat = false
                binding.repeatBtnPA.setColorFilter(ContextCompat.getColor(this, R.color.cool_pink))
            }
        }
    }

    private fun clickEqualizer() {
        binding.equalizerBtnPA.setOnClickListener {
            try {
                val eqIntent = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
                eqIntent.putExtra(
                    AudioEffect.EXTRA_AUDIO_SESSION, musicService!!.mediaPlayer!!.audioSessionId
                )
                eqIntent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, this.packageName)
                eqIntent.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
                startActivityForResult(eqIntent, REQUEST_EQUALIZER)
            } catch (e: Exception) {
                Toast.makeText(this, "not supported", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EQUALIZER || resultCode == RESULT_OK) {
            return
        }
    }

    private fun clickTimer() {
        binding.timerBtnPA.setOnClickListener {
            val timer = min15 || min30 || min60
            if (!timer) {
                showBottomSheetDialog()
            } else {
                val build = MaterialAlertDialogBuilder(this)
                build.setTitle("Stop Timer")
                    .setMessage("bạn muốn có muốn dừng hẹn giờ ?")
                    .setPositiveButton("đúng") { _, _ ->
                        min15 = false
                        min30 = false
                        min15 = false
                        binding.timerBtnPA.setColorFilter(ContextCompat.getColor(this, R.color.cool_pink))
                    }.setNegativeButton("không") { dialog, _ ->
                        dialog.dismiss()
                    }
                val customDialog = build.create()
                customDialog.show()
                customDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(Color.RED)
                customDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.RED)
            }
        }
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.bottom_sheet_dialog)
        dialog.show()
        dialog.findViewById<LinearLayout>(R.id.min_15)?.setOnClickListener {
            Toast.makeText(this, "nhạc dừng sau 15 phút", Toast.LENGTH_SHORT).show()
            binding.timerBtnPA.setColorFilter(ContextCompat.getColor(this, R.color.purple_500))
            min15 = true
             Thread {
                Thread.sleep(15*60*1000)
                if (min15) {
                    exitApplication()
                }
            }.start()
            dialog.dismiss()
        }

        dialog.findViewById<LinearLayout>(R.id.min_30)?.setOnClickListener {
            Toast.makeText(this, "nhạc dừng sau 30 phút", Toast.LENGTH_SHORT).show()
            binding.timerBtnPA.setColorFilter(ContextCompat.getColor(this, R.color.purple_500))
            min30 = true
            Thread {
                Thread.sleep(30*60*1000)
                if (min30) {
                    exitApplication()
                }
            }.start()
            dialog.dismiss()
        }

        dialog.findViewById<LinearLayout>(R.id.min_60)?.setOnClickListener {
            binding.timerBtnPA.setColorFilter(ContextCompat.getColor(this, R.color.purple_500))
            min60 = true
            Thread {
                Thread.sleep(60*60*1000)
                if (min60) {
                    exitApplication()
                }
            }.start()
            Toast.makeText(this, "nhạc dừng sau 60 phút", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }

    private fun clickShare(){
        binding.shareBtnPA.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "audio/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, musicListPA[songPosition].path.toUri())
            startActivity(Intent.createChooser(shareIntent,"Sharing Music File!!"))
            Toast.makeText(this, "loading...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createSeekBar() {
        binding.tvSeekBarStart.text =
            formatUtil.formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
        binding.tvSeekbarEnd.text = formatUtil.formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
        binding.seekBarPA.progress = 0
        binding.seekBarPA.max = musicService!!.mediaPlayer!!.duration
        musicService!!.mediaPlayer!!.setOnCompletionListener(this)
    }

    private fun clickSeekBar() {
        binding.seekBarPA.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    musicService!!.mediaPlayer!!.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) = Unit
            override fun onStopTrackingTouch(p0: SeekBar?) = Unit
        })
    }

    override fun onCompletion(p0: MediaPlayer?) {
        setSongPositionUtil.setSongPosition(increment = true)
        createMediaPlayer()
        try {
            setLayout()
        } catch (e: Exception) {
            return
        }
    }


    private fun startService() {
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        createMediaPlayer()
        musicService!!.seekBarSetup()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }


}