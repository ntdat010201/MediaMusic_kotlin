package com.example.mediamusickotlin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mediamusickotlin.PlayerActivity
import com.example.mediamusickotlin.databinding.FavouriteViewBinding
import com.example.mediamusickotlin.extension.showImgSong
import com.example.mediamusickotlin.model.Music

class FavouriteAdapter(
    private val context: Context,
    private var musicList: ArrayList<Music>
) : RecyclerView.Adapter<FavouriteAdapter.MyHolder>() {

    class MyHolder(binding: FavouriteViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.songImgFV
        val name = binding.songNameFV
        var root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(FavouriteViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.name.text = musicList[position].title
        showImgSong(context, musicList[position].path, holder.image)
        holder.root.setOnClickListener {
            sendIntent(position)
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    private fun sendIntent(pos: Int) {
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("index", pos)
        intent.putExtra("class", "FavouriteAdapter")
        ContextCompat.startActivity(context, intent, null)

    }

}