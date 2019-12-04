package mytestprogram

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import mytestprogram.customs.BackgroundService
import mytestprogram.models.DBModel

class NavigationActivity : AppCompatActivity() {

    lateinit var dbModel: DBModel

    private val appPreferences = "tasker"
    private lateinit var settings: SharedPreferences
    private lateinit var navView: BottomNavigationView

    //<service
    //android:name=".customs.BackgroundService"
    //android:enabled="true"
    //android:exported="true">
    //</service>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        // работа с базой данных

        dbModel = DBModel(baseContext)
        //dbModel.insertItem(Item(parentId = 0, action = "hi"))
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
                supportActionBar?.title = "Главная"
                supportFragmentManager.beginTransaction().replace(R.id.MainLayout, HomeSettings()).commit()
            }
            R.id.notes -> {
                supportActionBar?.title = "Записи"
                val fragment = NotesGroup()
                fragment.activity = this
                supportFragmentManager.beginTransaction().replace(R.id.MainLayout, fragment).commit()
            }
            R.id.add_note -> { }
        }
    }
}
