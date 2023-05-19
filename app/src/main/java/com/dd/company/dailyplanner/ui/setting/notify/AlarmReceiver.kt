package com.dd.company.dailyplanner.ui.setting.notify

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.utils.DateUtil.toCalendar

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val CHANNEL_ID = "CHANNEL_ID"
        const val NAME_NOTIFICATION_CHANNEL = "NOTIFICATION_CHANNEL"
        const val NOTIFICATION_ID = 1
    }

    override fun onReceive(p0: Context, intent: Intent) {
        Log.d("abcc", "onReceive: ")
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        createNotification(p0, title ?: "", content ?: "")
    }

    private fun createNotification(context: Context, title: String, content: String) {
        val contentIntent =
            context.packageManager.getLaunchIntentForPackage(context.packageName)?.setPackage(null)
                ?.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                            or Intent.FLAG_ACTIVITY_SINGLE_TOP
                )
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, contentIntent, flags)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder =
            NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.drawable.ic_noti)
                .setContentTitle(title)
                .setContentText(content)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID)
            val channel = NotificationChannel(
                CHANNEL_ID,
                NAME_NOTIFICATION_CHANNEL,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}


fun setReminder(context: Context, title: String, content: String, time: Long) {
    Log.d("abcc", "setReminder: $title, ${time.toCalendar().time}")
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    intent.putExtra("title", title)
    intent.putExtra("content", content)
    val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
    val pendingIntent =
        PendingIntent.getBroadcast(context, AlarmReceiver.NOTIFICATION_ID, intent, flags)
    AlarmManagerCompat.setExactAndAllowWhileIdle(
        alarmManager, AlarmManager.RTC_WAKEUP, time, pendingIntent
    )
}

fun cancelReminder(context: Context?) {
    val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        AlarmReceiver.NOTIFICATION_ID, intent, flags
    )
    alarmManager.cancel(pendingIntent)
}