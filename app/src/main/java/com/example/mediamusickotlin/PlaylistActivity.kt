package com.example.mediamusickotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.mediamusickotlin.databinding.ActivityPlaylistBinding

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding :ActivityPlaylistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityPlaylistBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initData()
        initView()
        initListener()
    }

    private fun initData() {

    }

    private fun initView() {

    }

    private fun initListener() {
        clickBack()
    }

    private fun clickBack(){
        binding.backBtnPLA.setOnClickListener {
            finish()
        }
    }
}