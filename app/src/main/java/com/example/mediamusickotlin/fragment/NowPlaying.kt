package com.example.mediamusickotlin.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mediamusickotlin.PlayerActivity
import com.example.mediamusickotlin.R
import com.example.mediamusickotlin.databinding.FragmentNowPlayingBinding
import com.example.mediamusickotlin.extension.showImgSong
import com.example.mediamusickotlin.utils.SetSongPositionUtil

class NowPlaying : Fragment() {
    private val setSongPositionUtil by lazy { SetSongPositionUtil() }

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var binding : FragmentNowPlayingBinding
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_now_playing,container,false)
        binding = FragmentNowPlayingBinding.bind(view)
        binding.root.visibility = View.INVISIBLE
        initData()
        initView()
        initListener()
        return view
    }

    private fun initData() {}

    private fun initView() {}

    private fun initListener() {
        clickPlayPauseNP()
        clickNextNP()
        clickShowPlayerA()
    }
    private fun clickShowPlayerA(){
        binding.root.setOnClickListener {
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            intent.putExtra("pos", PlayerActivity.songPosition)
            intent.putExtra("class", "NowPlaying")
            ContextCompat.startActivity(requireContext(),intent,null)
        }
    }

    private fun clickPlayPauseNP() {
        binding.playPauseBtnNP.setOnClickListener {
            if (PlayerActivity.isPlaying) {
                pauseMusic()
            } else {
                playMusic()
            }
        }
    }

    private fun clickNextNP(){
        binding.nextBtnNP.setOnClickListener {
            setSongPositionUtil.setSongPosition(increment = true)
            PlayerActivity.musicService!!.createMediaPlayer()
            showImgSong(
                requireContext(),
                PlayerActivity.musicListPA[PlayerActivity.songPosition].path,
                binding.songImgNP
            )
            binding.songNameNP.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].title
            PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
            playMusic()
        }
    }

    override fun onResume() {
        super.onResume()
        if (PlayerActivity.musicService != null) {
            binding.root.visibility = View.VISIBLE
            showImgSong(
                requireContext(), PlayerActivity.musicListPA[PlayerActivity.songPosition].path,
                binding.songImgNP
            )
            binding.songNameNP.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].title
            binding.songNameNP.isSelected = true
            if (PlayerActivity.isPlaying) {
                binding.playPauseBtnNP.setIconResource(R.drawable.ic_pause)
            } else {
                binding.playPauseBtnNP.setIconResource(R.drawable.ic_play)
            }
        }
    }
    private fun playMusic() {
        PlayerActivity.musicService!!.mediaPlayer!!.start()
        binding.playPauseBtnNP.setIconResource(R.drawable.ic_pause)
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
        PlayerActivity.binding.nextBtnPA.setIconResource(R.drawable.ic_pause)
        PlayerActivity.isPlaying = true
    }

    private fun pauseMusic() {
        PlayerActivity.musicService!!.mediaPlayer!!.pause()
        binding.playPauseBtnNP.setIconResource(R.drawable.ic_play)
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_play)
        PlayerActivity.binding.nextBtnPA.setIconResource(R.drawable.ic_play)
        PlayerActivity.isPlaying = false
    }

}