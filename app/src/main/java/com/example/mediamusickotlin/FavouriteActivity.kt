package com.example.mediamusickotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mediamusickotlin.adapter.FavouriteAdapter
import com.example.mediamusickotlin.databinding.ActivityFavouriteBinding
import com.example.mediamusickotlin.model.Music

class FavouriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavouriteBinding
    private lateinit var adapter:FavouriteAdapter
    companion object{
        var favouriteSongs : ArrayList<Music> = ArrayList()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityFavouriteBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initData()
        initView()
        initListener()
    }
    private fun initData() {
        adapter = FavouriteAdapter(this@FavouriteActivity, favouriteSongs)

        if(favouriteSongs.size<1){
            binding.shuffleBtnFA.visibility = View.INVISIBLE
        } else{
            binding.shuffleBtnFA.setOnClickListener {
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra("pos", 0)
                intent.putExtra("class", "FavouriteShuffle")
                startActivity(intent)
            }
        }
    }

    private fun initView() {
        dumpDataRcv()
    }

    private fun initListener() {
        clickBack()
    }

    private fun dumpDataRcv(){

        binding.favoriteRV.setHasFixedSize(true)
        binding.favoriteRV.setItemViewCacheSize(13)
        binding.favoriteRV.layoutManager = GridLayoutManager(this,3)
        binding.favoriteRV.adapter = adapter

    }
    private fun clickBack() {
        binding.backBtnFRA.setOnClickListener {
            finish()
        }
    }

}