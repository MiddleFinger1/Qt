package mytestprogram

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.DisplayMetrics
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import mytestprogram.models.InfoType

class ModalWindow: DialogFragment() {

    private lateinit var buttonRecord: ImageView
    private lateinit var buttonTask: ImageView
    private lateinit var buttonList: ImageView
    private lateinit var buttonSchedule: ImageView
    private lateinit var buttonCancel: Button
    private lateinit var buttonOk: Button
    private lateinit var textMessage: TextView

    var state = ""
    lateinit var activity: NavigationActivity

    override fun onResume() {
        val params = dialog.window!!.attributes
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val screenWidth = metrics.widthPixels
        val screenHeight = metrics.heightPixels
        params.width = screenWidth * 5/6
        params.height = screenHeight / 2
        dialog.window!!.attributes = params as WindowManager.LayoutParams
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.modal_window, container, false).apply {
            buttonRecord = findViewById(R.id.modal_record)
            buttonRecord.setOnClickListener {
                setState(it as ImageView)
            }
            buttonTask = findViewById(R.id.modal_task)
            buttonTask.setOnClickListener {
                setState(it as ImageView)
            }
            buttonList = findViewById(R.id.modal_list)
            buttonList.setOnClickListener {
                setState(it as ImageView)
            }
            buttonSchedule = findViewById(R.id.modal_schedule)
            buttonSchedule.setOnClickListener {
                setState(it as ImageView)
            }
            buttonCancel = findViewById(R.id.modal_cancel)
            buttonCancel.setOnClickListener { dismiss() }
            buttonOk = findViewById(R.id.modal_ok)
            buttonOk.setOnClickListener {
                acceptingType()
            }
            textMessage = findViewById(R.id.modal_message)
        }
    }

    private fun setState(view: ImageView){
        if (state != view.tag) {
            state = view.tag.toString()
            textMessage.text = state
        }
        else {
            state = ""
            textMessage.text = ""
        }
    }

    private fun acceptingType(){
        if (state != "") {
            val fragment = AddNoteForm()
            fragment.activity = activity
            fragment.onSend(when (state) {
                buttonRecord.tag -> InfoType.RECORD
                buttonTask.tag -> InfoType.TASK
                buttonList.tag -> InfoType.LIST
                buttonSchedule.tag -> InfoType.SCHEDULE
                else -> InfoType.NONE
            })
            activity.supportFragmentManager.beginTransaction().replace(R.id.MainLayout, fragment).commit()
            dismiss()
        } else textMessage.text = getString(R.string.not_selected)
    }
}