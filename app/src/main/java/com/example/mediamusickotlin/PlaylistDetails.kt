package com.example.mediamusickotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediamusickotlin.adapter.MusicAdapter
import com.example.mediamusickotlin.databinding.ActivityPlaylistDetailsBinding
import com.example.mediamusickotlin.extension.showImgSong
import com.example.mediamusickotlin.model.MusicPlaylist
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder

class PlaylistDetails : AppCompatActivity() {
    private lateinit var binding : ActivityPlaylistDetailsBinding
    private lateinit var adapter : MusicAdapter
    companion object{
        var currentPlaylistPos : Int = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityPlaylistDetailsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        currentPlaylistPos = intent.extras?.getInt("index")!!
        binding.playListDetailsRV.setItemViewCacheSize(10)
        binding.playListDetailsRV.setHasFixedSize(true)
        binding.playListDetailsRV.layoutManager = LinearLayoutManager(this)
        adapter =MusicAdapter(this,PlaylistActivity.musicPlaylist.ref[currentPlaylistPos].playlist,playlistDetails = true)
        binding.playListDetailsRV.adapter = adapter


    }

    private fun initView() {}

    private fun initListener() {
        clickBackPD()
        clickShufflePD()
        clickAddPD()
        clickRemovePD()
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        binding.playlistNamePD.text = PlaylistActivity.musicPlaylist.ref[currentPlaylistPos].name
        binding.moreInfoPD.text = "\tTotal ${adapter.itemCount} Songs.\n\n" +
                "\tCreated On:\n\t${PlaylistActivity.musicPlaylist.ref[currentPlaylistPos].createdOn}\n\n" +
                "\t-- ${PlaylistActivity.musicPlaylist.ref[currentPlaylistPos].createdBy}"
        if (adapter.itemCount>0){
            showImgSong(this,PlaylistActivity.musicPlaylist.ref[currentPlaylistPos].playlist[0].path,binding.playlistImgPD)
            binding.shuffleBtnPD.visibility = View.VISIBLE
        }
        adapter.notifyDataSetChanged()


        val editor = getSharedPreferences("FAVOURITES", MODE_PRIVATE).edit()
        // lưu trữ dữ liệu playlist sd Persistent
        val jsonStringPlaylist = GsonBuilder().create().toJson(PlaylistActivity.musicPlaylist)
        editor.putString("MusicPlaylist",jsonStringPlaylist)
        editor.apply()

    }

    private fun clickBackPD(){
        binding.backBtnPD.setOnClickListener {
            finish()
        }
    }

    private fun clickShufflePD(){
        binding.shuffleBtnPD.setOnClickListener {
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("pos", 0)
            intent.putExtra("class", "PlaylistDetailsShuffle")
            startActivity(intent)
        }
    }
    private fun clickAddPD(){
        binding.addBtnPD.setOnClickListener {
            startActivity(Intent(this,SelectionActivity::class.java))
        }
    }

    private fun clickRemovePD(){
        binding.removeAllBtnPD.setOnClickListener {
            val build = MaterialAlertDialogBuilder(this)
            build.setTitle("Remove")
                .setMessage("bạn muốn xóa tất cả nhạc ở playlist ?")
                .setPositiveButton("đúng") { dialog, _ ->
                    PlaylistActivity.musicPlaylist.ref[currentPlaylistPos].playlist.clear()
                    adapter.refreshPlaylist()
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
    }
}