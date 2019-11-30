package mytestprogram.customs

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.widget.Button
import android.widget.TextView
import mytestprogram.NavigationActivity
import mytestprogram.R
import mytestprogram.models.Item

class CustomHolderItems(view: View): ViewHolder(view) {

    private val textAction: TextView
    private val textDescription: TextView
    private val editButton: Button
    private lateinit var activity: NavigationActivity
    private lateinit var item: Item
    var id: Int = -1

    init {
        view.apply {
            textAction = findViewById(R.id.cardItem_textAction)
            textDescription = findViewById(R.id.cardItem_textDescription)
            editButton = findViewById(R.id.cardItem_hideContent)
        }
    }

    fun createHolder(activity: NavigationActivity, item: Item){
        this.activity = activity
        this.item = item
        textAction.text = item.action
        textDescription.text = item.description
    }


}