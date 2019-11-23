package mytestprogram

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.view.*
import android.support.v4.app.NotificationManagerCompat


class HomeSettings: Fragment() {

    private val NOTIFY_ID = 101
    private val CHANNEL_ID = "Cat channel"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_home_settings, container, false).apply {

            //val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            //    .setSmallIcon(R.drawable.list_note)
            //    .setContentTitle("Напоминание")
            //    .setContentText("Пора покормить кота")
            //    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            //val notificationManager = NotificationManagerCompat.from(context)
            //notificationManager.notify(NOTIFY_ID, builder.build())

        }
    }
}
