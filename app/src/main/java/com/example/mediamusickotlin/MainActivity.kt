package com.example.mediamusickotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import com.example.mediamusickotlin.adapter.MusicAdapter
import com.example.mediamusickotlin.databinding.ActivityMainBinding
import com.example.mediamusickotlin.model.Music
import com.example.mediamusickotlin.utils.Const
import com.example.mediamusickotlin.utils.ExitApplicationUtil.Companion.exitApplication
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var musicAdapter: MusicAdapter
    private lateinit var musicList: ArrayList<String>

    companion object {
        lateinit var musicListMA: ArrayList<Music>
        lateinit var musicListSearch: ArrayList<Music>
        var search : Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPinkNav)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        musicList = arrayListOf()
        search = false
        musicListMA = getAllAudio()
        musicAdapter = MusicAdapter(this, musicListMA)
        dumpDataRcv()
    }

    private fun initView() {
        forNavDrawer()
    }

    private fun initListener() {
        intentActivity()
    }

    private fun intentActivity() {
        binding.shuffleBtn.setOnClickListener {
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("pos", 0)
            intent.putExtra("class", "MainActivity")
            startActivity(intent)
        }
        binding.favoriteBtn.setOnClickListener {
            startActivity(Intent(this, FavouriteActivity::class.java))
        }
        binding.playlistBtn.setOnClickListener {
            startActivity(Intent(this, PlaylistActivity::class.java))
        }
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navFeedback -> Toast.makeText(this, "1", Toast.LENGTH_SHORT).show()
                R.id.navSettings -> Toast.makeText(this, "2", Toast.LENGTH_SHORT).show()
                R.id.navAbout -> Toast.makeText(this, "3", Toast.LENGTH_SHORT).show()
                R.id.navExit -> {
                    val build = MaterialAlertDialogBuilder(this)
                    build.setTitle("Exit")
                        .setMessage("bạn muốn đóng ứng dụng ?")
                        .setPositiveButton("đúng") { _, _ ->
                            exitApplication()
                            exitProcess(1)
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
            true
        }
    }

    private fun forNavDrawer() {
        toggle = ActionBarDrawerToggle(this, binding.root, R.string.open, R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    private fun dumpDataRcv() {
        binding.musicRV.setHasFixedSize(true)
        binding.musicRV.setItemViewCacheSize(13)
        binding.musicRV.adapter = musicAdapter
        binding.totalSongs.text = "Bai Hat : " + musicAdapter.itemCount
    }

    @SuppressLint("Recycle", "Range")
    private fun getAllAudio(): ArrayList<Music> {

        val tempList = ArrayList<Music>()
        val mediaStoreUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID,
        )
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC "

        val cursor = this.contentResolver.query(
            mediaStoreUri, projection, selection,
            null, sortOrder, null,
        )

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val titleC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val albumC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val artistC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val durationC =
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))

                    val music = Music(
                        id = idC, title = titleC, album = albumC, artist = artistC,
                        path = pathC, duration = durationC
                    )

                    val file = File(music.path)
                    if (file.exists()) {
                        tempList.add(music)
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        return tempList
    }

    @SuppressLint("CommitPrefEdits")
    override fun onDestroy() {
        super.onDestroy()
        if (!PlayerActivity.isPlaying && PlayerActivity.musicService != null) {
            exitApplication()
        }
    }

    override fun onResume() {
        super.onResume()
        val editor = getSharedPreferences("FAVOURITES", MODE_PRIVATE).edit()
        // lưu trữ dữ liệu Favourite sd Persistent
        val jsonString = GsonBuilder().create().toJson(FavouriteActivity.favouriteSongs)
        editor.putString("FavouriteSongs",jsonString)
        // lưu trữ dữ liệu playlist sd Persistent
        val jsonStringPlaylist = GsonBuilder().create().toJson(PlaylistActivity.musicPlaylist)
        editor.putString("MusicPlaylist",jsonStringPlaylist)

        editor.apply()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_view_menu, menu)
        val searchView = menu?.findItem(R.id.searchView)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
                    musicAdapter.updateMusicList(searchList = musicListSearch)
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

}