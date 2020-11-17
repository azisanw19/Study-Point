package id.canwar.studypoint

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.register_activity.*

class RegisterActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        initUI()
    }

    private fun initUI() {

        register_button.setOnClickListener {

            val email = register_email.text.toString()
            val firstName = register_first_name.text.toString()
            val lastName = register_last_name.text.toString()
            val role = onRadioButtonClicked()
            val birthday = register_birthday.text.toString()
            val password = register_password.text.toString()
            val confirmPassword = register_confirm_password.text.toString()

            val registerSK = register_sk.isChecked

            var isError = false

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                register_email.error = "Invalid email"
                register_email.isFocusable = true
                isError = true
            }

            if (password.length < 8) {
                register_password.error = "Password must be least of 8 character"
                register_password.isFocusable = true
                isError = true
            }

            if (password != confirmPassword) {
                register_confirm_password.error = "Password not matched"
                register_confirm_password.isFocusable = true
                isError = true
            }

            if (!registerSK) {
                Toast.makeText(this, "You must accept SK", Toast.LENGTH_SHORT).show()
                isError = true
            }

            if (role == null) {
                Toast.makeText(this, "Role is empty", Toast.LENGTH_SHORT).show()
                isError = true
            }


            if (!isError) {
                val userData = hashMapOf<String, String>(
                        "email" to email,
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "role" to role!!,
                        "birthday" to birthday
                )

                createNewUser(email, password, userData)
            }

        }


    }

    private fun onRadioButtonClicked(): String? = when(register_radio_group.checkedRadioButtonId) {
        R.id.register_radio_student -> "student"
        R.id.register_radio_teacher -> "teacher"
        else -> null
    }

    private fun createNewUser(email: String, password: String, dataRegister: HashMap<String, String>) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { it ->

                    if (it.isSuccessful) {

                        val uid = firebaseAuth.uid
                        if (uid != null) {
                            dataRegister["uid"] = uid
                            db.collection("users")
                                    .document(uid)
                                    .set(dataRegister)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                    }
                        }
                    }

                }

    }

}