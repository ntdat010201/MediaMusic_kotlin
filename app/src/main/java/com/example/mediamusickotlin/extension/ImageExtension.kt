package com.example.mediamusickotlin.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mediamusickotlin.R
import com.example.mediamusickotlin.adapter.MusicAdapter
import java.io.File

fun Context.loadThumbnailBitmap(path: String, result: ((Bitmap?) -> Unit)) {
    Glide.with(this)
        .asBitmap()
        .load(path)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .addListener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                if (e != null) {
                    Log.d("DAT", "onLoadFailed: " + e.message)
                }
                result(null)
                return false
            }

            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                result(resource)
                return false
            }
        })
        .centerCrop()
        .submit(256, 256)
}


fun Context.getImageSong(path: String): Bitmap? {
    var art: Bitmap? = null
    var rawArt: ByteArray? = null
    try {
        val mmr = MediaMetadataRetriever()
        val file = File(path)
        if (file.length() > 0 && file.exists()) {
            mmr.setDataSource(this, Uri.parse(path))
            try {
                rawArt = mmr.embeddedPicture
            } catch (e: Exception) {
            }
        }
        if (rawArt != null) {
            art = try {
                BitmapFactory.decodeByteArray(rawArt, 0, rawArt.size, BitmapFactory.Options())
            } catch (e: OutOfMemoryError) {
                null
            }
        }
    } catch (e: Exception) {
    }
    return art
}

fun Context.getImgArt(path: String) : ByteArray?{
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(path)
    return  retriever.embeddedPicture
}

