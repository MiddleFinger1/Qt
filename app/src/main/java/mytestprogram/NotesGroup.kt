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
                try {
                    val fragment = AddNoteForm()
                    fragment.activity = activity
                    activity.supportFragmentManager.beginTransaction().replace(R.id.MainLayout, fragment).commit()
                }
                catch (e: Exception) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            try {
                val recyclerView = findViewById<RecyclerView>(R.id.notesGroup_recyclerView)
                recyclerView.setHasFixedSize(true)
                val adapter = CustomAdapter()
                adapter.activity = activity
                activity.dbModel.createGroup()
                adapter.infos = activity.dbModel.mainGroup.getNotes()
                recyclerView.adapter = adapter
            }
            catch (ex: Exception) {
                Toast.makeText(context, "ошибка", Toast.LENGTH_LONG).show()
                Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}