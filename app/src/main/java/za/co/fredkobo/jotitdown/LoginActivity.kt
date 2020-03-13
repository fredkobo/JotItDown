package za.co.fredkobo.jotitdown

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup_activity.*
import kotlinx.android.synthetic.main.activity_signup_activity.email_et
import kotlinx.android.synthetic.main.activity_signup_activity.password_et

class LoginActivity : AppCompatActivity() {

    private val TAG = LoginActivity::class.java.simpleName
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish();
        }
    }

    fun loginButtonClicked(view: View) {
        if (validateLoginCredentials()) {

            val email = email_et.text.toString()
            val password = password_et.text.toString()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()

                        email_et.error = INCORRECT_EMAIL_OR_PASSWORD
                        password_et.error = INCORRECT_EMAIL_OR_PASSWORD
                        updateUI(null)
                    }

                    // ...
                }
        }
    }

    fun validateLoginCredentials(): Boolean {
        var isvalid = false

        val email_addr = email_et.text.toString()
        val password = password_et.text.toString()

        if (email_addr.length > 3 && password.length >= 6) {
            isvalid = true
        }

        return isvalid
    }

    fun newAccountButtonClicked(view: View) {
        startActivity(Intent(this, SignupActivityActivity::class.java))
    }

    companion object {
        private const val INCORRECT_EMAIL_OR_PASSWORD = "Incorrect email or password"
    }
}
