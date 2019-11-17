package mytestprogram

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import mytestprogram.customs.CustomAdapter


class NotesGroup: Fragment() {

    lateinit var activity: NavigationActivity
    lateinit var fabCreateNote: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_group, container, false).apply {

            fabCreateNote = findViewById(R.id.notesGroup_createNewNote)
            fabCreateNote.setOnClickListener {
                val fragment = ModalWindow()
                fragment.activity = activity
                fragment.showNow(activity.supportFragmentManager, fragment.javaClass.name)
            }
            try {
                //val recyclerView = findViewById<RecyclerView>(R.id.notesGroup_recyclerView)
                //recyclerView.setHasFixedSize(true)
                //val adapter = CustomAdapter()
                //adapter.activity = activity
                //activity.dbModel.createGroup()
                //adapter.infos = activity.dbModel.mainGroup.getNotes()
                //recyclerView.adapter = adapter

                activity.dbModel.createGroup()

                for (item in activity.dbModel.mainGroup.getNotes())
                    Toast.makeText(context, item.toString(), Toast.LENGTH_SHORT).show()
            }
            catch (ex: Exception) {
                Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}