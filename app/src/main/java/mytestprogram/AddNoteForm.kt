package mytestprogram

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.LinearLayout.LayoutParams
import mytestprogram.models.InfoPrototype
import mytestprogram.models.InfoPrototype.LevelPrivacy
import mytestprogram.models.InfoType


class AddNoteForm: Fragment(), SendListener {

    private var type = InfoType.NONE
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
    var info: InfoPrototype? = null
    var mode = FORM_MODE
    private var stateSwitcherParams = false
    private var statePrivacy = "public"

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

        when (type) {
            InfoType.RECORD -> { }
            InfoType.TASK -> { }
            InfoType.LIST -> { }
            InfoType.SCHEDULE -> { }
            else -> {}
        }

        setMode(mode, info)
        return views
    }

    private fun acceptCreating(oldInfo: InfoPrototype? = null){
        val action = textAction.text.toString()
        if (action != ""){
            // check levelPrivacy by radio buttons
            val levelPrivacy = when {
                (statePrivacy == "readonly" && textPassword.text.toString() != "")
                -> LevelPrivacy.READONLY
                (statePrivacy == "private" && textPassword.text.toString() != "")
                -> LevelPrivacy.PRIVATE
                (statePrivacy == "public") -> LevelPrivacy.PUBLIC
                else -> {
                    Toast.makeText(context, "fill password", Toast.LENGTH_SHORT).show()
                    return
                }
            }
            // initialize info for adding in database
            val info = InfoPrototype(
                type = type,
                action = action,
                description = textDescription.text.toString(),
                levelPrivacy = levelPrivacy,
                isImportant = importantLabel.isChecked,
                nameOfDevice = if (noticeDevice.isChecked) android.os.Build.MODEL else ""
            )
            // create a new info in db
            try {
                if (mode == FORM_MODE)
                    activity.dbModel.createNote(info)
                else if (mode == FORM_MODE)
                    activity.dbModel.updateNote(oldInfo!!.id, info)
            }
            catch (ex: Exception) {
                Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show()
            }
            val fragment = NotesGroup()
            fragment.activity = activity
            activity.supportFragmentManager.beginTransaction().replace(R.id.MainLayout, fragment).commit()
        }
    }

    private fun setMode(mode: Int, info: InfoPrototype? = null){
        this.mode = mode

        if ((mode == EDIT_MODE || mode == VIEW_MODE) && info != null){
            textAction.text.append(info.action)
            textDescription.text.append(info.description)
            noticeDevice.isChecked = info.nameOfDevice != ""
            importantLabel.isChecked = info.isImportant
            textPassword.text.append(info.password)
        }
        if (mode == EDIT_MODE || mode == FORM_MODE) {
            privacyGroup.setOnCheckedChangeListener{ group: RadioGroup, id: Int ->
                statePrivacy = when(id){
                    R.id.addNote_PublicButton -> "public"
                    R.id.addNote_readOnlyButton -> "readonly"
                    R.id.addNote_privateButton -> "private"
                    else -> ""
                }
            }
        }

        when (mode){
            FORM_MODE -> buttonOk.setOnClickListener { acceptCreating() }
            EDIT_MODE -> buttonOk.setOnClickListener { acceptCreating(info) }
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

    override fun onSend(data: Any?) {
        if (data is InfoType) type = data
    }

}
