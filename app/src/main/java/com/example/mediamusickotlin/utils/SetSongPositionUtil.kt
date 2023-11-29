package com.example.mediamusickotlin.utils

import com.example.mediamusickotlin.PlayerActivity

class SetSongPositionUtil {
        fun setSongPosition(increment: Boolean) {
            if (!PlayerActivity.repeat) {
                if (increment) {
                    if (PlayerActivity.musicListPA.size - 1 == PlayerActivity.songPosition) {
                        PlayerActivity.songPosition = 0
                    } else {
                        PlayerActivity.songPosition++
                    }
                } else {
                    if (PlayerActivity.songPosition == 0) {
                        PlayerActivity.songPosition = PlayerActivity.musicListPA.size - 1
                    } else {
                        PlayerActivity.songPosition--
                    }
                }

        }
    }
}