package com.example.mediamusickotlin.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.example.mediamusickotlin.PlayerActivity
import com.example.mediamusickotlin.R
import com.example.mediamusickotlin.extension.showImgSong
import com.example.mediamusickotlin.fragment.NowPlaying
import com.example.mediamusickotlin.utils.Const
import com.example.mediamusickotlin.utils.ExitApplicationUtil.Companion.exitApplication
import com.example.mediamusickotlin.utils.FavouriteCheckerUtil
import com.example.mediamusickotlin.utils.SetSongPositionUtil

class NotificationReceiver : BroadcastReceiver() {
    private val setSongPositionUtil by lazy { SetSongPositionUtil() }
    private val favouriteCheckerUtil by lazy { FavouriteCheckerUtil() }
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
        NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.ic_pause)

    }

    private fun pauseMusic(){
        PlayerActivity.isPlaying = false
        PlayerActivity.musicService!!.mediaPlayer!!.pause()
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_play)
        PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.ic_play)
        NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.ic_play)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun prevNextSong(increment : Boolean, context: Context){
        setSongPositionUtil.setSongPosition(increment = increment)
        PlayerActivity.musicService!!.createMediaPlayer()
        showImgSong(context,PlayerActivity.musicListPA[PlayerActivity.songPosition].path,PlayerActivity.binding.songImgPA)
        PlayerActivity.binding.songNamePA.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].title
        showImgSong(context,PlayerActivity.musicListPA[PlayerActivity.songPosition].path,NowPlaying.binding.songImgNP)
        NowPlaying.binding.songNameNP.text = PlayerActivity.musicListPA[PlayerActivity.songPosition] .title
        playMusic()
        PlayerActivity.fIndex = favouriteCheckerUtil.favouriteChecker(PlayerActivity.musicListPA[PlayerActivity.songPosition].id)
        if (PlayerActivity.isFavourite){
            PlayerActivity.binding.favouriteBtnPA.setImageResource(R.drawable.ic_favorite)
        } else{
            PlayerActivity.binding.favouriteBtnPA.setImageResource(R.drawable.ic_favorite_empty)

        }

    }

}