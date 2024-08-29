package com.example.mediamusickotlin

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.mediamusickotlin.MainActivity.Companion.musicListMA
import com.example.mediamusickotlin.MainActivity.Companion.musicListSearch
import com.example.mediamusickotlin.MainActivity.Companion.search
import com.example.mediamusickotlin.adapter.MusicAdapter
import com.example.mediamusickotlin.databinding.ActivitySelectionBinding

class SelectionActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySelectionBinding
    private lateinit var adapter : MusicAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectionBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        binding.selectionRV.setItemViewCacheSize(10)
        binding.selectionRV.setHasFixedSize(true)

        adapter = MusicAdapter(this, musicListMA, selectionActivity = true)
        binding.selectionRV.adapter = adapter
    }

    private fun initView() {
        searchView()
    }

    private fun initListener() {
        clickBackSA()
    }

    private fun searchView() {
        binding.searchViewSA.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                musicListSearch = arrayListOf()

                if (newText != null) {
                    val userInput = newText.lowercase()
                    for (song in musicListMA) {
                        if (song.title.lowercase().contains(userInput)) {
                            musicListSearch.add(song)
                        }
                    }
                    search = true
                    adapter.updateMusicList(searchList = musicListSearch)
                }
                return true
            }
        })
    }
    private fun clickBackSA(){
        binding.backBtnSA.setOnClickListener {
            finish()
        }
    }
}