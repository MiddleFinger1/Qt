package mytestprogram.customs

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import mytestprogram.AddNoteForm
import mytestprogram.NavigationActivity
import mytestprogram.SendListener
import mytestprogram.models.*
import android.widget.TextView
import mytestprogram.R


class CustomHolder(view: View): RecyclerView.ViewHolder(view), SendListener {

    private val textAction: TextView
    private val hideContent: Button
    private val textDescription: TextView
    private val privacyState: ImageView
    private val editMode: Button
    private val copyNote: Button
    private val viewMode: Button
    private val dateCreate: TextView
    private val menuBar: LinearLayout
    private lateinit var activity: NavigationActivity
    private lateinit var container: Container

    init {
        view.apply {
            textAction = findViewById(R.id.cardItem_textAction)
            hideContent = findViewById(R.id.cardItem_hideContent)
            textDescription = findViewById(R.id.cardItem_textDescription)
            privacyState = findViewById(R.id.cardView_privacyState)
            editMode = findViewById(R.id.cardView_editMode)
            copyNote = findViewById(R.id.cardView_copyNote)
            viewMode = findViewById(R.id.cardView_viewMode)
            dateCreate = findViewById(R.id.cardView_dateCreate)
            menuBar = findViewById(R.id.card_view_MenuBar)
        }
    }

    fun createHolder(activity: NavigationActivity, container: Container){
        this.activity = activity
        this.container = container
        textAction.text = container.action
        textDescription.text = container.description

        privacyState.setBackgroundResource(
            when (container.privacy) {
                1 -> R.drawable.flag_blue_active
                2 -> R.drawable.flag_red_active
                else -> R.drawable.flag_green_active
            }
        )
        //временно
        privacyState.setOnLongClickListener {
            activity.dbModel.deleteNote(container.id)
            true
        }

        dateCreate.text = calendarToString(container.dateCreate)

        editMode.setOnClickListener {
            try {
                if (container.privacy == Container.PUBLIC) onSend(AddNoteForm.EDIT_MODE)
                else {
                    val passwordUnlock = PasswordUnlock()
                    passwordUnlock.password = container.password
                    passwordUnlock.modeUnlock = AddNoteForm.EDIT_MODE
                    passwordUnlock.sender = this
                    passwordUnlock.showNow(activity.supportFragmentManager, passwordUnlock.javaClass.name)
                }
            }
            catch (ex: Exception){
                Toast.makeText(activity, ex.toString(), Toast.LENGTH_LONG).show()
            }
        }

        viewMode.setOnClickListener {
            if (container.privacy != Container.PRIVATE) onSend(AddNoteForm.VIEW_MODE)
            else {
                val passwordUnlock = PasswordUnlock()
                passwordUnlock.password = container.password
                passwordUnlock.sender = this
                passwordUnlock.modeUnlock = AddNoteForm.VIEW_MODE
                passwordUnlock.showNow(activity.supportFragmentManager, passwordUnlock.javaClass.name)
            }
        }

        hideContent.setOnClickListener {
            menuBar.visibility = if (menuBar.visibility == View.INVISIBLE)
                View.VISIBLE
            else View.INVISIBLE
        }
    }

    override fun onSend(data: Any?) {
        try {
            if (data is Int){
                val fragment = AddNoteForm()
                fragment.activity = activity
                fragment.container = container
                fragment.mode = data
                activity.supportFragmentManager.beginTransaction().replace(R.id.MainLayout, fragment).commit()
            }
        }
        catch (ex: Exception) {
            Toast.makeText(activity, ex.toString(), Toast.LENGTH_LONG).show()
        }
    }
}