package za.co.fredkobo.jotitdown

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.logout_item -> {
                signOut()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private class NotesRecyclerAdapter(val noteList: List<Note>, val context: Context) :
        RecyclerView.Adapter<NotesRecyclerAdapter.NoteViewHolder>() {

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

    private fun getListOfNotes(): List<Note> {
        val noteList = listOf(
            Note("123456789","Fluid Mechanics", "the study of forces and flow within fluids", System.currentTimeMillis())
        )
        return noteList;
    }

    fun addButtonClicked(view: View) {
        startActivity(Intent(this, CreateNoteActivity::class.java))
    }

    private fun signOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        // [END auth_sign_out]
    }
}
