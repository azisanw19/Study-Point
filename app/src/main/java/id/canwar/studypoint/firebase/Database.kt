package id.canwar.studypoint.firebase

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
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

    fun getInformation(callback: (data: ArrayList<Map<String, Any>?>) -> Unit) {

        db.collection("information")
                .get()
                .addOnSuccessListener {
                    Log.d("data information", "${it.documents}")

                    val data: ArrayList<Map<String, Any>?> = ArrayList()

                    for (document in it.documents) {
                        data.add(document.data)
                    }

                    callback(data)

                }

    }

    fun getTaskDone(uid: String, callback: (dataTask: ArrayList<Map<String, Any>?>, dataSoal: ArrayList<Map<String, Any>?>) -> Unit) {

        db.collection("dikerjakan")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener {
                    Log.d("data dikerjakan", "${it.documents}")

                    val taskDone: ArrayList<Map<String, Any>?> = ArrayList()
                    val dataSoal: ArrayList<Map<String, Any>?> = ArrayList()
                    val soals: ArrayList<Task<DocumentSnapshot>> = ArrayList()

                    for (document in it.documents) {
                        val dataTask = document.data

                        val soal = db.collection("soal").document(dataTask?.get("soalId").toString()).get()
                        soals.add(soal)

                        taskDone.add(dataTask)
                    }

                    Tasks.whenAllSuccess<DocumentSnapshot>(soals)
                            .addOnSuccessListener { snapshotList ->
                                for (document in snapshotList) {
                                    val soal = document.data

                                    dataSoal.add(soal)
                                }
                                Log.d("data dikerjakan", "$taskDone")
                                Log.d("data soal", "$dataSoal")
                                callback(taskDone, dataSoal)
                            }


                }

    }

}