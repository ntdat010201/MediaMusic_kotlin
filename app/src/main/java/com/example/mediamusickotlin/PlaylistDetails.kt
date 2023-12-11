package com.example.mediamusickotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediamusickotlin.adapter.MusicAdapter
import com.example.mediamusickotlin.databinding.ActivityPlaylistDetailsBinding
import com.example.mediamusickotlin.extension.showImgSong

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
        currentPlaylistPos = intent.extras?.get("index") as Int
        binding.playListDetailsRV.setItemViewCacheSize(10)
        binding.playListDetailsRV.setHasFixedSize(true)
        binding.playListDetailsRV.layoutManager = LinearLayoutManager(this)
        PlaylistActivity.musicPlaylist.ref[currentPlaylistPos].playlist.addAll(MainActivity.musicListMA)
        adapter =MusicAdapter(this,PlaylistActivity.musicPlaylist.ref[currentPlaylistPos].playlist,playlistDetails = true)
        binding.playListDetailsRV.adapter = adapter


    }

    private fun initView() {}

    private fun initListener() {
        clickBackPD()
        clickShufflePD()
        addPD()
    }

    @SuppressLint("SetTextI18n")
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
    private fun addPD(){
        binding.addBtnPD.setOnClickListener {
            startActivity(Intent(this,SelectionActivity::class.java))
        }
    }
}