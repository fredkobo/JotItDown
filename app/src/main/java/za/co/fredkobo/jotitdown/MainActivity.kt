package za.co.fredkobo.jotitdown

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_note.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val initialNoteList = mutableListOf<Note>()
        notes_recyclerview.layoutManager = LinearLayoutManager(this)
        notes_recyclerview.adapter = NotesRecyclerAdapter(initialNoteList, this)

        getLatestListOfNotes()

    }

    private fun getLatestListOfNotes() {

        val notes = mutableListOf<Note>()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                notes.clear()
                for (child in dataSnapshot.children) {
                    val note = child.getValue(Note::class.java)
                    if (note != null) {
                        notes.add(note)
                    }
                }
                (notes_recyclerview.adapter as NotesRecyclerAdapter).updateData(notes)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        val userId = auth.currentUser?.uid
        if (userId != null) {
            var ref = database.ref.child("users").child(userId).child("notes")
            ref.addValueEventListener(postListener)
        }

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

    private class NotesRecyclerAdapter(var noteList: List<Note>, val context: Context) :
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

        fun updateData(newNoteList: List<Note>) {
            val diffResult = DiffUtil.calculateDiff(NotesDiffCallback(this.noteList, newNoteList))
            diffResult.dispatchUpdatesTo(this)
            this.noteList = newNoteList;
        }

    }

    fun addButtonClicked(view: View) {
        startActivity(Intent(this, CreateNoteActivity::class.java))
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    companion object {
        private const val TAG = "MainActivity"
    }

    class NotesDiffCallback(var oldList: List<Note>, var newList: List<Note>) :
        DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val b = oldList[oldItemPosition].id.equals(newList[newItemPosition].id)
            return b
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val b = oldList[oldItemPosition] == newList[newItemPosition]
            return b;
        }

    }
}
