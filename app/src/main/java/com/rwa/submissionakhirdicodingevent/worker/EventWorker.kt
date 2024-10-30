package com.rwa.submissionakhirdicodingevent.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.rwa.submissionakhirdicodingevent.R

class EventWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    companion object {
        private val TAG = EventWorker::class.java.simpleName
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "EventNotificationChannel"
        const val CHANNEL_NAME = "Event Notifications"
    }

    override fun doWork(): Result {
        return getFirstActiveEvent()
    }

    private fun getFirstActiveEvent(): Result {
        Log.d(TAG, "getFirstActiveEvent: Mulai.....")

        return try {
            val sharedPreferences =
                applicationContext.getSharedPreferences("EventData", Context.MODE_PRIVATE)
            val title = sharedPreferences.getString("title", "Default Title") ?: "Default Title"
            val description = sharedPreferences.getString("description", "Default Description")
                ?: "Default Description"

            showNotification(title, description)
            Result.success()
        } catch (err: Exception) {
            showNotification("Something went wrong!", err.message.toString())
            Result.failure()
        }
    }

    private fun showNotification(title: String, description: String?) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}