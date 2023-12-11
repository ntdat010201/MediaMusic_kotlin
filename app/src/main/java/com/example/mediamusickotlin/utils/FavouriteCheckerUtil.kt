package com.example.mediamusickotlin.utils

import com.example.mediamusickotlin.FavouriteActivity
import com.example.mediamusickotlin.PlayerActivity

class FavouriteCheckerUtil {

    fun favouriteChecker(id: String): Int {
        PlayerActivity.isFavourite = false
        FavouriteActivity.favouriteSongs.forEachIndexed { index, music ->
            if (id == music.id) {
                PlayerActivity.isFavourite = true
                return index
            }
        }
        return -1
    }
}