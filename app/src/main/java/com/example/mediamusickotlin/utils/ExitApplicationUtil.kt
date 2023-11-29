package com.example.mediamusickotlin.utils

import com.example.mediamusickotlin.PlayerActivity
import kotlin.system.exitProcess

class ExitApplicationUtil {
    companion object{
        fun exitApplication(){
            if (PlayerActivity.musicService != null) {
                PlayerActivity.musicService!!.stopForeground(true)
                PlayerActivity.musicService!!.mediaPlayer!!.release()
                PlayerActivity.musicService = null
                exitProcess(1)
            }
        }
    }
}