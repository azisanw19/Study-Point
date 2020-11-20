package id.canwar.studypoint.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Authentication {

    companion object {
        var authentication: Authentication? = null

        fun getInstance(): Authentication {

            if (authentication == null)
                authentication = Authentication()

            return authentication!!

        }
    }

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun signInWithEmailAndPassword(email: String, password: String, callback: (task: Task<AuthResult>) -> Unit) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    callback(it)
                }

    }

    fun createNewUserWithEmailAndPassword(email: String, password: String, callback: (task: Task<AuthResult>) -> Unit) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    callback(it)
                }

    }

    fun getUID(): String? = firebaseAuth.uid

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    fun signOut(callback: () -> Unit) {

        firebaseAuth.signOut()

        if (getCurrentUser() == null) {
            callback()
        }

    }



}