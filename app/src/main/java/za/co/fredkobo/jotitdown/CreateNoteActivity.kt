package za.co.fredkobo.jotitdown

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class CreateNoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)
    }

    fun discardButtonClicked(view: View) {
        finish()
    }

    fun saveButtonClicked(view: View) {
        finish()
    }
}
