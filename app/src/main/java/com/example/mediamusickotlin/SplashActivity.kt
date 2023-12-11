package com.example.mediamusickotlin

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mediamusickotlin.databinding.ActivitySplashBinding
import com.example.mediamusickotlin.model.Music
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    var storagePermissionLauncher: ActivityResultLauncher<String>? = null
    private val permission = Manifest.permission.READ_EXTERNAL_STORAGE
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        handler = Handler()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun initView() {
        checkPermission()
    }

    private fun initListener() {}

    private fun splashMedia() {
        handler!!.postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            getDataFavourite()
            finish()
        }, 1300)
    }

    private fun userResponses() {
        if (ContextCompat.checkSelfPermission(
                this, permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            splashMedia()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(permission)) {
                AlertDialog.Builder(this).setTitle("Requesting permission")
                    .setMessage("Cho phép chúng tôi tìm bài hát trên thiết bị của bạn ")
                    .setPositiveButton("Cho phép") { _, _ ->
                        storagePermissionLauncher!!.launch(permission)
                    }.setNegativeButton("Hủy bỏ") { dialogInterface, _ ->
                        Toast.makeText(this, "bạn đã từ chối tìm các bài hát", Toast.LENGTH_SHORT)
                            .show()
                        dialogInterface.dismiss()
                    }.show()
            }
        } else {
            Toast.makeText(this, "bạn đã từ chối tìm các bài hát", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermission() {
        storagePermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted: Boolean ->
            if (granted) {
                splashMedia()
            } else {
                userResponses()
            }
        }
        storagePermissionLauncher!!.launch(permission)
    }

    private fun getDataFavourite() {
//        truy xuất dữ liệu Favourite sd Persistent
        val editor = getSharedPreferences("FAVOURITES", MODE_PRIVATE)
        val jsonString = editor.getString("FavouriteSongs", null)
        val typeToken = object : TypeToken<ArrayList<Music>>() {}.type
        if (jsonString != null) {
            val data: ArrayList<Music> = GsonBuilder().create().fromJson(jsonString, typeToken)
            FavouriteActivity.favouriteSongs.addAll(data)
        }
    }
}