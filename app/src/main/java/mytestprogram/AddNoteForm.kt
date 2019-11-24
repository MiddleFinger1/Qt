package mytestprogram

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.LinearLayout.LayoutParams
import mytestprogram.models.*
import java.util.Calendar


class AddNoteForm: Fragment() {

    lateinit var activity: NavigationActivity
    private lateinit var buttonOk: Button
    private lateinit var buttonCancel: Button
    private lateinit var textSwitcher: TextView
    private lateinit var paramsLayout: LinearLayout

    private lateinit var importantLabel: CheckBox
    private lateinit var noticeDevice: CheckBox
    private lateinit var textAction: EditText
    private lateinit var textDescription: EditText
    private lateinit var textPassword: EditText
    private lateinit var buttonPublic: Button
    private lateinit var buttonReadOnly: Button
    private lateinit var buttonPrivate: Button
    private lateinit var privacyGroup: RadioGroup

    //states
    var container: Container? = null
    var mode = FORM_MODE
    private var stateSwitcherParams = false
    private var statePrivacy = Container.PUBLIC

    companion object {
        const val FORM_MODE = 0
        const val VIEW_MODE = 1
        const val EDIT_MODE = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val views = inflater.inflate(R.layout.fragment_add_note_form, container, false)

        views.apply {
            // initialization properties
            buttonOk = findViewById(R.id.addNote_ButtonOk)
            buttonCancel = findViewById(R.id.addNote_ButtonCancel)
            textSwitcher = findViewById(R.id.addNote_SwitcherParams)
            textAction = findViewById(R.id.addNote_TextAction)
            textDescription = findViewById(R.id.addNote_TextDescription)
            paramsLayout = findViewById(R.id.addNote_paramsLayout)
            textPassword = findViewById(R.id.addNote_textPassword)
            buttonPublic = findViewById(R.id.addNote_PublicButton)
            buttonReadOnly = findViewById(R.id.addNote_readOnlyButton)
            buttonPrivate = findViewById(R.id.addNote_privateButton)
            privacyGroup = findViewById(R.id.addNote_PrivacyGroup)
            importantLabel = findViewById(R.id.addNote_importantLabel)
            noticeDevice = findViewById(R.id.addNote_checkNoticeDevice)
        }
        //
        // check mode for form

        buttonCancel.setOnClickListener {
            activity.switchFragment(R.id.notes)
        }

        textSwitcher.setOnClickListener {
            paramsLayout.layoutParams =
                if (stateSwitcherParams){
                    stateSwitcherParams = false
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                }
                else{
                    stateSwitcherParams = true
                    LayoutParams(0, 0)
                }
        }
        setMode(mode, this.container)
        return views
    }

    private fun acceptCreating(oldInfo: Container? = null){
        try {
            val action = textAction.text.toString()
            if (action != ""){
                // check levelPrivacy by radio buttons
                val levelPrivacy = when {
                    (statePrivacy == Container.READONLY && textPassword.text.toString() != "") -> Container.READONLY
                    (statePrivacy == Container.PRIVATE && textPassword.text.toString() != "") -> Container.PRIVATE
                    (statePrivacy == Container.PUBLIC) -> Container.PUBLIC
                    else -> {
                        Toast.makeText(context, "fill password", Toast.LENGTH_SHORT).show()
                        return
                    }
                }
                val id = -1
                val description = textDescription.text.toString()
                val nameDevice = android.os.Build.MODEL
                val isImportant = importantLabel.isChecked
                val isInTrash = false
                val dateCreate = Calendar.getInstance()
                val password = textPassword.text.toString()

                // create a new info in db
                // initialize info for adding in database
                val container = Container(id, action, description, nameDevice, isImportant, isInTrash, dateCreate, levelPrivacy, password)
                if (mode == FORM_MODE)
                    activity.dbModel.insertContainer(container)
                else if (mode == EDIT_MODE)
                    activity.dbModel.updateContainer(oldInfo!!.id, container)
                val fragment = NotesGroup()
                fragment.activity = activity
                activity.supportFragmentManager.beginTransaction().replace(R.id.MainLayout, fragment).commit()
            }
        }
        catch (ex: Exception) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun setMode(mode: Int, container: Container? = null){
        this.mode = mode

        if ((mode == EDIT_MODE || mode == VIEW_MODE) && container != null){
            textAction.text.append(container.action)
            textDescription.text.append(container.description)
            noticeDevice.isChecked = container.nameDevice != ""
            importantLabel.isChecked = container.isImportant
            textPassword.text.append(container.password)
            statePrivacy = container.privacy
            privacyGroup.check(when(container.privacy){
                Container.READONLY -> R.id.addNote_readOnlyButton
                Container.PRIVATE -> R.id.addNote_privateButton
                else -> R.id.addNote_PublicButton
            })
        }
        if (mode == EDIT_MODE || mode == FORM_MODE)
            privacyGroup.setOnCheckedChangeListener{ group: RadioGroup, id: Int ->
                statePrivacy = when(id){
                    R.id.addNote_readOnlyButton -> Container.READONLY
                    R.id.addNote_privateButton -> Container.PRIVATE
                    else -> Container.PUBLIC
                }
            }
        when (mode){
            FORM_MODE -> buttonOk.setOnClickListener { acceptCreating() }
            EDIT_MODE -> buttonOk.setOnClickListener { acceptCreating(container) }
            VIEW_MODE -> {
                textAction.isFocusable = false
                textDescription.isFocusable = false
                textPassword.isFocusable = false
                buttonOk.setOnClickListener {
                    activity.switchFragment(R.id.notes)
                }
                buttonPublic.isFocusable = false
                buttonReadOnly.isFocusable = false
                buttonPrivate.isFocusable = false
            }
        }
    }
}