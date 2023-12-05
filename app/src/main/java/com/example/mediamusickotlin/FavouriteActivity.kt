package com.example.mediamusickotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mediamusickotlin.adapter.FavouriteAdapter
import com.example.mediamusickotlin.databinding.ActivityFavouriteBinding

class FavouriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavouriteBinding
    private lateinit var adapter:FavouriteAdapter
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
        val temp = ArrayList<String>()
        temp.add("song1")
        temp.add("song2")
        temp.add("song3")
        temp.add("song4")
        temp.add("song5")
        temp.add("song6")
        temp.add("song7")
        temp.add("song8")
        adapter = FavouriteAdapter(this@FavouriteActivity,temp)
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