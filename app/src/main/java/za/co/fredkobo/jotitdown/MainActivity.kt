package za.co.fredkobo.jotitdown

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_note.view.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notes_recyclerview.layoutManager = LinearLayoutManager(this)
        notes_recyclerview.adapter = NotesRecyclerAdapter(getListOfNotes(), this)

    }

    private class NotesRecyclerAdapter(val noteList: List<Note>, val context: Context): RecyclerView.Adapter<NotesRecyclerAdapter.NoteViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)
            return (NoteViewHolder(view))
        }

        override fun getItemCount(): Int {
            return noteList.size
        }

        override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
            holder.tvTitle.text = noteList.get(position).title
            holder.tvBody.text = noteList.get(position).body
        }

        class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvTitle = view.tv_title
            val tvBody = view.tv_body
        }

    }

    private fun getListOfNotes() : List<Note> {
        val noteList = listOf(Note("Fluid Mechanics", "the study of forces and flow within fluids", Date()),
            Note("Electrical Engineering", "Electrical engineering is an engineering discipline concerned with the study, design and application of equipment, devices and systems which use electricity, electronics, and electromagnetism.", Date()))
        return noteList;
    }
}
