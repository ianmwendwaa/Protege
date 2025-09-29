import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//package com.example.notessqlite.notification
//
//import android.annotation.SuppressLint
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.Context
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Bundle
//import android.os.PersistableBundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.compose.ui.platform.LocalContext
//import androidx.core.app.ActivityCompat
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import com.example.notessqlite.R
//import com.example.notessqlite.notes.CHANNEL_ID
//
//const val CHANNEL_ID = "33"
//class NotificationHandler:AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)
//        fun buildAndShowNotification(birthdayAmiesName:String, age:Int){
//            val builder = NotificationCompat.Builder(this, com.example.notessqlite.notification.CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_info)
//                .setContentTitle("It's $birthdayAmiesName's birthday!")
//                .setContentText("Wish $birthdayAmiesName a happy birthday as she turns $age years old")
//                .setAutoCancel(true)
//            createNotificationChannel()
//
//            with(NotificationManagerCompat.from(this)) {
//                if (ActivityCompat.checkSelfPermission(
//                        this@NotificationHandler,
//                        android.Manifest.permission.POST_NOTIFICATIONS
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    return@with
//                }
//                notify(10, builder.build())
//            }
//        }
//    }
//    @SuppressLint("ObsoleteSdkInt")
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = "Notification channel"
//            val descriptionText = "This is my notification channel"
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
//                description = descriptionText
//            }
//            val notificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//}

fun typeWriteMessage(lifecycleScope: LifecycleCoroutineScope, text:String, intervalMs:Long) {
    lifecycleScope.launch {
        text.forEach { char ->
            delay(intervalMs)
        }
    }
}