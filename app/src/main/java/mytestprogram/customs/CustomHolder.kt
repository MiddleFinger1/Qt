package mytestprogram.customs

import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.*
import android.widget.LinearLayout.*
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
    private val dateCreate: TextView
    private lateinit var activity: NavigationActivity
    private lateinit var container: Container

    init {
        view.apply {
            textAction = findViewById(R.id.cardItem_textAction)
            hideContent = findViewById(R.id.cardItem_hideContent)
            textDescription = findViewById(R.id.cardItem_textDescription)
            privacyState = findViewById(R.id.cardView_privacyState)
            dateCreate = findViewById(R.id.cardView_dateCreate)
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

        hideContent.setOnClickListener {
            showSnackBar(it)
        }
    }

    private fun showSnackBar(view: View){

        val snackBar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE)
        val layout = snackBar.view as Snackbar.SnackbarLayout
        val dp32 = activity.resources.getDimension(R.dimen.dp32).toInt()
        val dp10 = activity.resources.getDimension(R.dimen.dp10).toInt()
        val linearLayout = LinearLayout(activity.baseContext)

        linearLayout.orientation = HORIZONTAL
        linearLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        linearLayout.gravity = Gravity.CENTER_VERTICAL

        linearLayout.addView(Button(activity.baseContext).apply {
            layoutParams = LayoutParams(dp32, dp32).apply {
                marginEnd = dp10
            }
            setBackgroundResource(R.drawable.pencil)
            setOnClickListener {
                try {
                    if (container.privacy == Container.PUBLIC) onSend(AddNoteForm.EDIT_MODE)
                    else {
                        val passwordUnlock = PasswordUnlock()
                        passwordUnlock.password = container.password
                        passwordUnlock.modeUnlock = AddNoteForm.EDIT_MODE
                        passwordUnlock.sender = this@CustomHolder
                        passwordUnlock.showNow(activity.supportFragmentManager, passwordUnlock.javaClass.name)
                    }
                }
                catch (ex: Exception){
                    Toast.makeText(activity, ex.toString(), Toast.LENGTH_LONG).show()
                }
            }
        })
        linearLayout.addView(Button(activity.baseContext).apply {
            layoutParams = LayoutParams(dp32, dp32).apply {
                marginEnd = dp10
            }
            setBackgroundResource(R.drawable.copy_note)
        })
        linearLayout.addView(Button(activity.baseContext).apply {
            layoutParams = LayoutParams(dp32, dp32).apply {
                marginEnd = dp10
            }
            setBackgroundResource(R.drawable.view_mode)
            setOnClickListener {
                if (container.privacy != Container.PRIVATE) onSend(AddNoteForm.VIEW_MODE)
                else {
                    val passwordUnlock = PasswordUnlock()
                    passwordUnlock.password = container.password
                    passwordUnlock.sender = this@CustomHolder
                    passwordUnlock.modeUnlock = AddNoteForm.VIEW_MODE
                    passwordUnlock.showNow(activity.supportFragmentManager, passwordUnlock.javaClass.name)
                }
            }
        })
        layout.addView(linearLayout)
        layout.setBackgroundResource(R.color.colorPrimaryDark)
        snackBar.show()
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