package mytestprogram.customs

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import mytestprogram.NavigationActivity
import mytestprogram.R
import java.util.*
import kotlin.collections.ArrayList


class BackgroundService : Service() {

    lateinit var checkTimes: ArrayList<Calendar>
    lateinit var deadLineTimes: ArrayList<Calendar>

    override fun onCreate(){

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //val calendar = Calendar.getInstance()

        Thread.sleep(5000)
        showNotification()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun showNotification(){
        val resultIntent = Intent(this, NavigationActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(baseContext)
            .setSmallIcon(R.drawable.task_note)
            .setContentTitle("Quick Tasker")
            .setContentText("Your task is over!!")
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true) // delete from status bar
            .setShowWhen(true)
        builder.color = Color.GRAY

        val notification = builder.build()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, notification)
    }



    override fun onBind(intent: Intent) = null
}
