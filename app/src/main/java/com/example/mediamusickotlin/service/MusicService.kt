package com.example.mediamusickotlin.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.media.app.NotificationCompat
import com.example.mediamusickotlin.PlayerActivity
import com.example.mediamusickotlin.R
import com.example.mediamusickotlin.extension.getImgArt
import com.example.mediamusickotlin.receiver.NotificationReceiver
import com.example.mediamusickotlin.utils.Const
import com.example.mediamusickotlin.utils.Const.CHANNEL_ID
import com.example.mediamusickotlin.utils.Const.NOTIFICATION_ID
import com.example.mediamusickotlin.utils.FormatUtil

class MusicService : Service() {
    private val myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var runnable: Runnable

    private val formatUtil by lazy { FormatUtil() }

    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(this, "My Music")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    fun showNotification(playPauseBtn: Int) {

        val prevIntent = Intent(this, NotificationReceiver::class.java).setAction(Const.PREVIOUS)
        val prevPendingIntent =
            PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val playIntent = Intent(this, NotificationReceiver::class.java).setAction(Const.PLAY)
        val playPendingIntent =
            PendingIntent.getBroadcast(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent = Intent(this, NotificationReceiver::class.java).setAction(Const.NEXT)
        val nextPendingIntent =
            PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val exitIntent = Intent(this, NotificationReceiver::class.java).setAction(Const.EXIT)
        val exitPendingIntent =
            PendingIntent.getBroadcast(this, 0, exitIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val imgArt = getImgArt(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)
        val image = if (imgArt != null){
            BitmapFactory.decodeByteArray(imgArt,0,imgArt.size)
        } else{
            BitmapFactory.decodeResource(resources,R.drawable.ic_default_music)
        }

        val notification = androidx.core.app.NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(PlayerActivity.musicListPA[PlayerActivity.songPosition].title)
            .setContentText(PlayerActivity.musicListPA[PlayerActivity.songPosition].artist)
            .setSmallIcon(R.drawable.ic_music_note)
            .setLargeIcon(image)
            .setStyle(NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
            .setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.ic_skip_previous, "Previous", prevPendingIntent)
            .addAction(playPauseBtn, "Play", playPendingIntent)
            .addAction(R.drawable.ic_skip_next, "Next", nextPendingIntent)
            .addAction(R.drawable.ic_exit, "Exit", exitPendingIntent)
            .build()
        Log.d(
            "DAT",
            "showNotification: " + PlayerActivity.musicListPA[PlayerActivity.songPosition].artist
        )
        startForeground(NOTIFICATION_ID, notification)
    }

    fun createMediaPlayer() {
        try {
            if (PlayerActivity.musicService!!.mediaPlayer == null) {
                PlayerActivity.musicService!!.mediaPlayer = MediaPlayer()
            }
            PlayerActivity.musicService!!.mediaPlayer!!.reset()
            PlayerActivity.musicService!!.mediaPlayer!!.setDataSource(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)
            PlayerActivity.musicService!!.mediaPlayer!!.prepare()

            PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.ic_pause)
            PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)

            PlayerActivity.binding.tvSeekBarStart.text =
                formatUtil.formatDuration(mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.tvSeekbarEnd.text =
                formatUtil.formatDuration(mediaPlayer!!.duration.toLong())
            PlayerActivity.binding.seekBarPA.progress = 0
            PlayerActivity.binding.seekBarPA.max = mediaPlayer!!.duration

        } catch (e: java.lang.Exception) {
            return
        }
    }

    fun seekBarSetup(){
        runnable = Runnable {
            PlayerActivity.binding.tvSeekBarStart.text = formatUtil.formatDuration(mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.seekBarPA.progress = mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable,200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,0)

    }
}