package za.co.fredkobo.jotitdown

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_note_detail.*

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        note = intent.getParcelableExtra<Note>(NOTE_KEY)
        date_time_tv.text = formatDate(note.date)
        title_tv.text = note.title
        body_tv.text = note.body

        title = note.title

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.edit_item -> {
                editNote()
                finish()
                true
            }
            R.id.delete_item -> {
                deleteNote()
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_note_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }


    companion object {
        private const val TAG = "NoteDetailActivity"
        const val NOTE_KEY = "note_key"
    }

    private fun editNote() {
        val intent = Intent(this, CreateNoteActivity::class.java);
        intent.putExtra(NOTE_KEY, note)
        startActivity(intent)
        finish()
    }

    private fun deleteNote() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val ref = database.ref.child("users").child(userId).child("notes").child(note.id)
            ref.removeValue()
        }
    }

    fun editButtonClicked(view: View) {
        editNote()
    }
}
