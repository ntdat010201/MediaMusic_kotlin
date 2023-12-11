package com.example.mediamusickotlin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mediamusickotlin.PlaylistActivity
import com.example.mediamusickotlin.PlaylistDetails
import com.example.mediamusickotlin.databinding.PlaylistViewBinding
import com.example.mediamusickotlin.extension.showImgSong
import com.example.mediamusickotlin.model.Playlist
import com.example.mediamusickotlin.utils.ExitApplicationUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.system.exitProcess

class PlaylistViewAdapter(
    private val context: Context,
    private var playlistList: ArrayList<Playlist>
) : RecyclerView.Adapter<PlaylistViewAdapter.MyHolder>() {

    class MyHolder(binding : PlaylistViewBinding) : RecyclerView.ViewHolder(binding.root){
        val image = binding.playlistImg
        val name = binding.playlistName
        val root = binding.root
        val delete = binding.playlistDeleteBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(PlaylistViewBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.name.text = playlistList[position].name

        holder.name.isSelected = true

        holder.delete.setOnClickListener {
            val build = MaterialAlertDialogBuilder(context)
            build.setTitle(playlistList[position].name)
                .setMessage("bạn muốn đóng xóa playlist ?")
                .setPositiveButton("đúng") { dialog, _ ->
                    PlaylistActivity.musicPlaylist.ref.removeAt(position)
                    refreshPlaylist()
                    dialog.dismiss()
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

        holder.root.setOnClickListener {
            val intent = Intent(context,PlaylistDetails::class.java)
            intent.putExtra("index",position)
            ContextCompat.startActivity(context,intent,null)
        }

        if (PlaylistActivity.musicPlaylist.ref[position].playlist.size > 0){
            showImgSong(context,PlaylistActivity.musicPlaylist.ref[position].playlist[0].path,holder.image)
        }

    }

    override fun getItemCount(): Int {
        return playlistList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshPlaylist(){
        playlistList = ArrayList()
        playlistList.addAll(PlaylistActivity.musicPlaylist.ref)
        notifyDataSetChanged()
    }


}