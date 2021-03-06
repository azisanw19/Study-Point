package id.canwar.studypoint.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.canwar.studypoint.R
import id.canwar.studypoint.dialogs.LoadingDialog
import id.canwar.studypoint.firebase.Authentication
import id.canwar.studypoint.firebase.Database
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private val authentication = Authentication.getInstance()
    private val database = Database.getInstance()
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initUI()
    }

    private fun initUI() {

        loadingDialog = LoadingDialog(this)

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
                val userData = hashMapOf<String, Any>(
                    "email" to email,
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "role" to role!!,
                    "birthday" to birthday,
                    "point" to 0,
                    "totalItem" to 0,
                    "tugasDikerjakan" to 0,
                    "image" to ""
                )

                loadingDialog.show()

                authentication
                    .createNewUserWithEmailAndPassword(email, password) {
                        if (it.isSuccessful) {
                            userData["userId"] = authentication.getUID()!!

                            database.crateUser(userData) { task ->

                                if (task.isSuccessful) {
                                    loadingDialog.dismiss()

                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, "Register failed", Toast.LENGTH_SHORT).show()
                                    loadingDialog.dismiss()
                                }

                            }
                        } else {
                            loadingDialog.dismiss()
                        }
                    }

            }

        }


    }

    private fun onRadioButtonClicked(): String? = when (register_radio_group.checkedRadioButtonId) {
        R.id.register_radio_student -> "student"
        R.id.register_radio_teacher -> "teacher"
        else -> null
    }

}