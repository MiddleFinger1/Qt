package mytestprogram.customs

import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import mytestprogram.models.InfoPrototype
import mytestprogram.AddNoteForm
import mytestprogram.NavigationActivity
import mytestprogram.R
import mytestprogram.models.calendarToString

class CustomHolder(view: View): RecyclerView.ViewHolder(view), View.OnLongClickListener{

    val imageTypeNote: ImageView
    val textAction: TextView
    val hideContent: Button
    val textDescription: TextView
    val privacyState: Button
    val editMode: Button
    val copyNote: Button
    val viewMode: Button
    val dateCreate: TextView
    val menuBar: LinearLayout

    init {
        view.apply {
            imageTypeNote = findViewById(R.id.cardView_typeNote)
            textAction = findViewById(R.id.cardView_textAction)
            hideContent = findViewById(R.id.cardView_hideContent)
            textDescription = findViewById(R.id.cardView_textDescription)
            privacyState = findViewById(R.id.cardView_privacyState)
            editMode = findViewById(R.id.cardView_editMode)
            copyNote = findViewById(R.id.cardView_copyNote)
            viewMode = findViewById(R.id.cardView_viewMode)
            dateCreate = findViewById(R.id.cardView_dateCreate)
            menuBar = findViewById(R.id.card_view_MenuBar)
        }
    }

    fun createHolder(activity: NavigationActivity, info: InfoPrototype){

        imageTypeNote.setImageResource(
            when (info.type) {
                InfoType.RECORD -> R.drawable.record_note
                InfoType.TASK -> R.drawable.task_note
                InfoType.LIST -> R.drawable.list_note
                InfoType.SCHEDULE -> R.drawable.schedule_note
                else -> 0
            }
        )
        textAction.text = info.action
        textDescription.text = info.description

        privacyState.setBackgroundResource(
            when (info.levelPrivacy) {
                InfoPrototype.LevelPrivacy.PUBLIC -> R.drawable.flag_green_active
                InfoPrototype.LevelPrivacy.READONLY -> R.drawable.flag_blue_active
                InfoPrototype.LevelPrivacy.PRIVATE -> R.drawable.flag_red_active
            }
        )
        dateCreate.text = calendarToString(info.dateCreate)

        editMode.setOnClickListener {
            try {
                if (info.levelPrivacy == InfoPrototype.LevelPrivacy.PRIVATE)
                    Toast.makeText(activity.baseContext, "недоступен данный режим", Toast.LENGTH_SHORT).show()
                else if (info.levelPrivacy == InfoPrototype.LevelPrivacy.PUBLIC) {
                    val fragment = AddNoteForm()
                    fragment.info = info
                    fragment.mode = AddNoteForm.EDIT_MODE
                    activity.supportFragmentManager.beginTransaction().replace(R.id.MainLayout, fragment).commit()
                }
            }
            catch (ex: Exception){
                Toast.makeText(activity, ex.toString(), Toast.LENGTH_LONG).show()
            }
        }

        viewMode.setOnClickListener {
            if (info.levelPrivacy != InfoPrototype.LevelPrivacy.PRIVATE){
                val fragment = AddNoteForm()
                fragment.info = info
                fragment.mode = AddNoteForm.VIEW_MODE
                activity.supportFragmentManager.beginTransaction().replace(R.id.MainLayout, fragment).commit()
            }
        }

        hideContent.setOnClickListener {
            menuBar.visibility = if (menuBar.visibility == View.INVISIBLE)
                View.VISIBLE
            else View.INVISIBLE
        }

    }

    override fun onLongClick(view: View?): Boolean {
        Snackbar.make(view!!, "", Snackbar.LENGTH_INDEFINITE).show()
        return true
    }


}