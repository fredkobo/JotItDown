package za.co.fredkobo.jotitdown

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_note.*


class CreateNoteActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

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

        val userId = auth.currentUser?.uid
        val date = System.currentTimeMillis();
        val note = Note("", title, body, date)
        writeNewPost(note)

        finish()
    }

    private fun writeNewPost(note: Note) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            var ref = database.ref.child("users").child(userId).child("notes")
            var key = database.ref.child("users").child(userId).child("notes").push().key
            if (key != null) {
                note.id = key
                ref.child(key).setValue(note).addOnSuccessListener {
                    Log.d(TAG, "new note add");
                }
            }
        }


    }

    companion object {

        private const val TAG = "NewPostActivity"
        private const val REQUIRED = "Required"
    }
}
