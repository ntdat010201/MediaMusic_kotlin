package com.example.mediamusickotlin.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.mediamusickotlin.utils.Const.CHANNEL_ID

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID,"Now Playing Song",
            NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = "this is a important channel for showing song !!"
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}