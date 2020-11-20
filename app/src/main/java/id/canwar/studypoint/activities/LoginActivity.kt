package id.canwar.studypoint.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import id.canwar.studypoint.R
import id.canwar.studypoint.firebase.Authentication
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val authentication = Authentication.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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

            if (!isError) {

                authentication
                        .signInWithEmailAndPassword(email, password) {

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()

                        }

            }
        }

        register_activity.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}