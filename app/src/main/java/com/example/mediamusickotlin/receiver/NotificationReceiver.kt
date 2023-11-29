package com.example.mediamusickotlin.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.bumptech.glide.Glide
import com.example.mediamusickotlin.PlayerActivity
import com.example.mediamusickotlin.R
import com.example.mediamusickotlin.extension.getImageSong
import com.example.mediamusickotlin.utils.Const
import com.example.mediamusickotlin.utils.ExitApplicationUtil.Companion.exitApplication
import com.example.mediamusickotlin.utils.SetSongPositionUtil
import kotlin.system.exitProcess

class NotificationReceiver : BroadcastReceiver() {
    private val setSongPositionUtil by lazy { SetSongPositionUtil() }
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Const.PREVIOUS -> prevNextSong(increment = false,context = context!!)
            Const.PLAY -> if (PlayerActivity.isPlaying){
                pauseMusic()
            } else {
                playMusic()
            }
            Const.NEXT -> prevNextSong(increment = true,context =context!!)
            Const.EXIT -> {
                exitApplication()
            }
        }
    }
    private fun playMusic(){
        PlayerActivity.isPlaying = true
        PlayerActivity.musicService!!.mediaPlayer!!.start()
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
        PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.ic_pause)

    }

    private fun pauseMusic(){
        PlayerActivity.isPlaying = false
        PlayerActivity.musicService!!.mediaPlayer!!.start()
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_play)
        PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.ic_play)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun prevNextSong(increment : Boolean, context: Context){
        setSongPositionUtil.setSongPosition(increment = increment)
        /*PlayerActivity.musicService!!.mediaPlayer!!.setDataSource(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)
        PlayerActivity.musicService!!.mediaPlayer!!.prepare()
        PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.ic_pause)
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)*/

        PlayerActivity.musicService!!.createMediaPlayer()
        Glide.with(context)
            .load(context.getImageSong(PlayerActivity.musicListPA[PlayerActivity.songPosition].path))
            .placeholder(context.getDrawable(R.drawable.ic_default_music))
            .into(PlayerActivity.binding.songImgPA)
        PlayerActivity.binding.songNamePA.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].title

    }

}