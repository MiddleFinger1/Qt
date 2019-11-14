package mytestprogram

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.widget.Toast
import mytestprogram.customs.BackgroundService
import mytestprogram.models.DBModel


class NavigationActivity : AppCompatActivity() {

    lateinit var dbModel: DBModel

    private val appPreferences = "tasker"
    private lateinit var settings: SharedPreferences
    private lateinit var navView: BottomNavigationView

    companion object {
        //
        const val TYPE_LANGUAGE = "type_language"
    }

    //<service
    //android:name=".customs.BackgroundService"
    //android:enabled="true"
    //android:exported="true">
    //</service>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        // работа с базой данных

        try {
            dbModel = DBModel(baseContext)
            dbModel.selectNotes()

        }
        catch (ex: Exception) {
            Toast.makeText(baseContext, ex.toString(), Toast.LENGTH_LONG).show()
        }
        // В сетингах получаем настройки для последующей работы приложения
        //
        /*
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, null);
        // выводим английский текст на русской локали устройства
        setTitle(R.string.app_name);
         */

        settings = getSharedPreferences(appPreferences, Context.MODE_PRIVATE)

        navView = findViewById(R.id.nav_view)
        navView.apply {
            setOnNavigationItemSelectedListener(
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    switchFragment(item.itemId)
                    return@OnNavigationItemSelectedListener true
            })
        }
        switchFragment(R.id.home)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onDestroy() {
        startService(Intent(this, BackgroundService::class.java))
        super.onDestroy()
    }

    fun switchFragment(id: Int){
        val item = navView.menu.findItem(id)
        item.isChecked = true

        when (item.itemId) {
            R.id.home -> {
                supportFragmentManager.beginTransaction().replace(R.id.MainLayout, HomeSettings()).commit()
            }
            R.id.notes -> {
                val fragment = NotesGroup()
                fragment.activity = this
                supportFragmentManager.beginTransaction().replace(R.id.MainLayout, fragment).commit()
            }
            R.id.add_note -> { }
        }
    }
}
