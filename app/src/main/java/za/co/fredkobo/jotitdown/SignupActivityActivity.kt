package za.co.fredkobo.jotitdown

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signup_activity.*

class SignupActivityActivity : AppCompatActivity() {

    private val TAG = SignupActivityActivity::class.java.simpleName
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_activity)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayShowHomeEnabled(true)
        title = getString(R.string.create_account)

        auth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun cancelButtonClicked(view: View) {
        finish()
    }

    fun submitButtonClicked(view: View) {
        val email_addr = email_et.text.toString()
        val password = password_et.text.toString();

        createAccount(email_addr, password);
    }

    fun validateInputs(): Boolean {
        var isValid = false

        val email_addr = email_et.text.toString()
        val password = password_et.text.toString();
        val verify_password = verify_password_et.text.toString()

        if (TextUtils.isEmpty(email_addr)) {
            email_et.error = REQUIRED
            return false
        }

        if (TextUtils.isEmpty(password)) {
            password_et.error = REQUIRED
            return false
        }

        if (TextUtils.isEmpty(password)) {
            verify_password_et.error = REQUIRED
            return false
        }

        if(!isValidEmailAddress(email_addr)) {
            email_et.error = ENTER_VALID_EMAIL
            return false
        }

        if(password.length < 6) {
            password_et.error = ENTER_VALID_PASSWORD
            return false
        }

        if(!verify_password.equals(password)) {
            verify_password_et.error = PASSWORDS_MUST_MATCH
            return false
        }

        if (email_addr.length > 3 && password.length >= 6 && password.equals(verify_password)) {
            isValid = true
        }

        return isValid
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateInputs()) {
            return
        }

        //showProgressBar()

        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }

                // [START_EXCLUDE]
                //hideProgressBar()
                // [END_EXCLUDE]
            }
        // [END create_user_with_email]
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
        private const val REQUIRED = "Required"
        private const val ENTER_VALID_EMAIL = "Enter a valid email address"
        private const val ENTER_VALID_PASSWORD = "Password must be 6 or more characters"
        private const val PASSWORDS_MUST_MATCH = "This must match your password"
    }
}
