package com.example.notessqlite.todo

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.notessqlite.R

const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val contentExtra = "contentExtra"

class NotificationManagerr:BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification= NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_done)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(contentExtra))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
        manager.notify(notificationID, notification)
    }
}
