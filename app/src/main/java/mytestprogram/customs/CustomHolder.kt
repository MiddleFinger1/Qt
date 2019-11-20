package mytestprogram.customs

import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import mytestprogram.AddNoteForm
import mytestprogram.NavigationActivity
import mytestprogram.R
import mytestprogram.models.*

class CustomHolder(view: View): RecyclerView.ViewHolder(view), View.OnLongClickListener{

    private val imageTypeNote: ImageView
    private val textAction: TextView
    private val hideContent: Button
    private val textDescription: TextView
    private val privacyState: Button
    private val editMode: Button
    private val copyNote: Button
    private val viewMode: Button
    private val dateCreate: TextView
    private val menuBar: LinearLayout

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

    fun createHolder(activity: NavigationActivity, container: Container){

        textAction.text = container.action
        textDescription.text = container.description

        privacyState.setBackgroundResource(
            when (container.privacy) {
                1 -> R.drawable.flag_blue_active
                2 -> R.drawable.flag_red_active
                else -> R.drawable.flag_green_active
            }
        )
        dateCreate.text = calendarToString(container.dateCreate)

        editMode.setOnClickListener {
            try {
                if (container.privacy == 2)
                    Toast.makeText(activity.baseContext, "недоступен данный режим", Toast.LENGTH_SHORT).show()
                else if (container.privacy == 0) {
                    val fragment = AddNoteForm()
                    fragment.container = container
                    fragment.mode = AddNoteForm.EDIT_MODE
                    activity.supportFragmentManager.beginTransaction().replace(R.id.MainLayout, fragment).commit()
                }
            }
            catch (ex: Exception){
                Toast.makeText(activity, ex.toString(), Toast.LENGTH_LONG).show()
            }
        }

        viewMode.setOnClickListener {
            if (container.privacy != 2){
                val fragment = AddNoteForm()
                fragment.container = container
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