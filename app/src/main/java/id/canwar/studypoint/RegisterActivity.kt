package id.canwar.studypoint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
    }

    private fun createNewUser(email: String, password: String, dataRegister: HashMap<String, String>) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {

                    if (it.isSuccessful) {

                        val uid = firebaseAuth.uid
                        if (uid != null) {
                            db.collection("users")
                                    .document(uid)
                                    .set(dataRegister)
                        }
                    }

                }

    }

}