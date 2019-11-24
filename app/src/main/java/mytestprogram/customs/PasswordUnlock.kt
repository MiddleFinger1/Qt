package mytestprogram.customs

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.DisplayMetrics
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import mytestprogram.R
import mytestprogram.SendListener


class PasswordUnlock: DialogFragment() {

    var password = ""
    lateinit var sender: SendListener
    var modeUnlock = 0
    private lateinit var textPassword: EditText
    private lateinit var buttonCancel: Button
    private lateinit var buttonOk: Button

    override fun onResume() {
        val params = dialog.window!!.attributes
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val screenWidth = metrics.widthPixels
        val screenHeight = metrics.heightPixels
        params.width = screenWidth * 3/4
        params.height = screenHeight / 2
        dialog.window!!.attributes = params as WindowManager.LayoutParams
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        inflater.inflate(R.layout.layout_password_unlock, container).apply {

            textPassword = findViewById(R.id.passwordUnlock_textPassword)
            buttonCancel = findViewById(R.id.passwordUnlock_buttonCancel)
            buttonOk = findViewById(R.id.passwordUnlock_buttonOk)

            buttonCancel.setOnClickListener {
                dismiss()
            }
            buttonOk.setOnClickListener {
                when(textPassword.text.toString()){
                    password -> {
                        sender.onSend(modeUnlock)
                        dismiss()
                    }
                    "" -> Toast.makeText(context, "пустой текст", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(context, "неправильный запрос", Toast.LENGTH_SHORT).show()
                }
            }
            return this
        }
    }
}