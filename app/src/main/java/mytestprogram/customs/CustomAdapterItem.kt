package mytestprogram.customs

import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import mytestprogram.NavigationActivity
import mytestprogram.R
import mytestprogram.models.Item

class CustomAdapterItem: Adapter<ViewHolder>() {

    lateinit var activity: NavigationActivity
    private lateinit var layoutInflater: LayoutInflater
    lateinit var infos: ArrayList<Item>

    override fun getItemCount() = infos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = infos[position]
        holder as CustomHolderItems
        holder.createHolder(activity, info)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.card_note_view, parent, false)
        return CustomHolder(view)
    }


}