package za.co.fredkobo.jotitdown

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_note.*
import za.co.fredkobo.jotitdown.NoteDetailActivity.Companion.NOTE_KEY


class CreateNoteActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var noteToEdit: Note
    var isEditActivity = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayShowHomeEnabled(true)
        if (intent.hasExtra(NOTE_KEY)) {
            noteToEdit = intent.getParcelableExtra<Note>(NOTE_KEY)
            title_et.setText(noteToEdit.title)
            body_et.setText(noteToEdit.body)
            title = "Edit a note"
            isEditActivity = true
        } else {
            title = "Compose a new note"
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
    }

    fun discardButtonClicked(view: View) {
        finish()
    }

    fun saveButtonClicked(view: View) {
        val title = title_et.text.toString()
        val body = body_et.text.toString()

        if (TextUtils.isEmpty(title)) {
            title_et.error = REQUIRED
            return
        }

        if (TextUtils.isEmpty(body)) {
            body_et.error = REQUIRED
            return
        }

        val date = System.currentTimeMillis();
        val note = Note("", title, body, date)

        if (isEditActivity) {
            noteToEdit.title = title
            noteToEdit.body = body
            noteToEdit.date = date
            editPost(noteToEdit)
        } else {
            writeNewNote(note)
        }


        finish()
    }

    private fun editPost(note: Note) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val ref = database.ref.child("users").child(userId).child("notes").child(note.id)
            ref.setValue(note).addOnSuccessListener {
                Log.d(TAG, "new note edited")
            }
        }
    }

    private fun writeNewNote(note: Note) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val ref = database.ref.child("users").child(userId).child("notes")
            val key = database.ref.child("users").child(userId).child("notes").push().key
            if (key != null) {
                note.id = key
                ref.child(key).setValue(note).addOnSuccessListener {
                    Log.d(TAG, "new note add");
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    companion object {

        private const val TAG = "NewPostActivity"
        private const val REQUIRED = "Required"
    }
}
