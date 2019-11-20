package mytestprogram.customs

import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import mytestprogram.R
import mytestprogram.NavigationActivity
import mytestprogram.models.Container


class CustomAdapter: Adapter<ViewHolder>() {

    lateinit var activity: NavigationActivity
    private lateinit var layoutInflater: LayoutInflater
    lateinit var infos: ArrayList<Container>

    override fun getItemCount() = infos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = infos[position]
        holder as CustomHolder
        holder.createHolder(activity, info)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.card_note_view, parent, false)
        return CustomHolder(view)
    }
}