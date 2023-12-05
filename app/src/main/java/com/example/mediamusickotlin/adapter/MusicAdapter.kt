package com.example.mediamusickotlin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mediamusickotlin.MainActivity
import com.example.mediamusickotlin.PlayerActivity
import com.example.mediamusickotlin.R
import com.example.mediamusickotlin.databinding.MusicViewBinding
import com.example.mediamusickotlin.extension.getImageSong
import com.example.mediamusickotlin.model.Music
import com.example.mediamusickotlin.utils.FormatUtil

class MusicAdapter(
    private val context: Context,
    private var musicList: ArrayList<Music>
) : RecyclerView.Adapter<MusicAdapter.MyHolder>() {

    private val formatUtil by lazy { FormatUtil() }

    class MyHolder(binding: MusicViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.songNameMV
        val album = binding.songAlbumMV
        val image = binding.imageMV
        val duration = binding.songDuration
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            MusicViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.title.text = musicList[position].title
        holder.album.text = musicList[position].album
        holder.duration.text = formatUtil.formatDuration(musicList[position].duration)

        Glide.with(context).load(context.getImageSong(musicList[position].path))
            .placeholder(context.getDrawable(R.drawable.ic_default_music))
            .into(holder.image)

        holder.itemView.setOnClickListener {
            when {
                MainActivity.search -> sendIntent(ref = "MusicAdapterSearch",position)
                musicList[position].id == PlayerActivity.nowPlayingId ->
                    sendIntent(ref = "NowPlaying", pos = PlayerActivity.songPosition)
                else -> sendIntent(ref = "MusicAdapter",position)
            }

        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    fun updateMusicList(searchList :ArrayList<Music>){
        musicList = arrayListOf()
        musicList.addAll(searchList)
        notifyDataSetChanged()
    }

    private fun sendIntent(ref : String, pos : Int){
        val intent = Intent(context,PlayerActivity::class.java)
        intent.putExtra("pos",pos)
        intent.putExtra("class" ,ref)
        context.startActivity(intent)
    }

}