package id.canwar.studypoint

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)



        initUI()
    }

    private fun initUI() {

        login_button.setOnClickListener {
            val email = login_email.text.toString()
            val password = login_password.text.toString()

            var isError = false

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                login_email.error = "Invalid email"
                login_email.isFocusable = true
                isError = true
            }

            if (password.length < 8) {
                login_password.error = "Password must be least of 8 character"
                login_password.isFocusable = true
                isError = true
            }

            if (!isError)
                loginUser(email, password)
        }

        register_activity.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun loginUser(email: String, password: String) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                .addOnFailureListener {
                    // TODO on failed
                }

    }

}