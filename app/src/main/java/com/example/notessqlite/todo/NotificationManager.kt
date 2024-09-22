package com.example.notessqlite.todo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notessqlite.MainActivity
import com.example.notessqlite.R

class NotificationManager:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelId = "protege_reminder"
            val channelName = "Protege Reminders"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)

            channel.enableLights(true)
            channel.lightColor = Color.RED

            channel.enableVibration(true)

            val notificationManager = context?.getSystemService(NotificationManager::class.java)

            notificationManager?.createNotificationChannel(channel)
        }

        val notificationId = 1
        val title = "Protege Reminder"
        val content = "You have a new task"
        val intent = Intent(context,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val builder = context?.let {
            NotificationCompat.Builder(it, "protege_reminders")
                .setSmallIcon(R.drawable.protege)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

        val notificationManager = context?.let { NotificationManagerCompat.from(it) }
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.POST_NOTIFICATIONS
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        if (builder != null) {
            notificationManager?.notify(notificationId, builder.build())
        }
    }
}
