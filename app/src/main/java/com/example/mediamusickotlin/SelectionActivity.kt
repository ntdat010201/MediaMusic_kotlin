package com.example.mediamusickotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.mediamusickotlin.databinding.ActivitySelectionBinding

class SelectionActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySelectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectionBinding.inflate(LayoutInflater.from(this))
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

    }
}