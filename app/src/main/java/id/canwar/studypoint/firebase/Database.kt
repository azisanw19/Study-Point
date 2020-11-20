package id.canwar.studypoint.firebase

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class Database {

    companion object {
        var database: Database? = null

        fun getInstance(): Database {

            if (database == null)
                database = Database()

            return database!!

        }
    }

    private val db = FirebaseFirestore.getInstance()

    fun crateUser(userData: Map<String, Any>, callback: (task: Task<Void>) -> Unit) {

        db.collection("users")
                .document(userData["userId"].toString())
                .set(userData)
                .addOnCompleteListener(callback)

    }

    fun getUser(userId: String, callback: (data: Map<String, Any>?) -> Unit) {

        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener {
                    Log.d("data user", "$it")

                    if (it != null) {
                        callback(it.data)
                    }
                }

    }

    fun getInformation(callback: (query: QuerySnapshot) -> Unit) {

        db.collection("information")
                .get()
                .addOnSuccessListener {
                    Log.d("data information", "${it.documents}")

                    callback(it)

                }

    }

}