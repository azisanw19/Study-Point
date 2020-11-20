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

    fun getSoal(judul: String, kategori: String, callback: (dataSoal: ArrayList<Map<String, Any>?>, dataUser: ArrayList<Map<String, Any>?>) -> Unit) {

        if (judul == "" && kategori == "") {
            callback(ArrayList(), ArrayList())
        }

        val filter = ArrayList<Task<QuerySnapshot>>()
        if (kategori != "" && judul != "") {
            val soalRef = db.collection("soal")

            soalRef.orderBy("judul")
                    .startAt(judul)
                    .endAt(judul + "\uf8ff")
            soalRef.orderBy("tingkat")
                    .startAt(kategori)
                    .endAt(kategori + "\uf8ff")

            filter.add(soalRef.get())
        }
        else {
            if (judul != "") {

                val dariJudul = db.collection("soal")
                        .orderBy("judul")
                        .startAt(judul)
                        .endAt(judul + "\uf8ff")
                        .get()

                filter.add(dariJudul)
            } else if (kategori != "") {
                val dariTingkat = db.collection("soal")
                        .orderBy("tingkat")
                        .startAt(kategori)
                        .endAt(kategori + "\uf8ff")
                        .get()

                val dariMataPelajaran = db.collection("soal")
                        .orderBy("mataPelajaran")
                        .startAt(kategori)
                        .endAt(kategori + "\uf8ff")
                        .get()

                filter.add(dariTingkat)
                filter.add(dariMataPelajaran)
            }
        }

        Log.d("data filter", "$filter")

        Tasks.whenAllSuccess<QuerySnapshot>(filter)
                .addOnSuccessListener {

                    val soals: ArrayList<Map<String, Any>?> = ArrayList()
                    val users: ArrayList<Map<String, Any>?> = ArrayList()
                    val userSnaphot: ArrayList<Task<DocumentSnapshot>> = ArrayList()

                    for (querySnapshot in it) {

                        for (document in querySnapshot) {

                            val soal = document.data

                            val pembuatId = soal["pembuatId"].toString()

                            if (!soals.contains(soal)) {
                                soals.add(soal)
                                val userSnap = db.collection("users").document(pembuatId).get()
                                userSnaphot.add(userSnap)
                            }

                        }

                    }

                    Tasks.whenAllSuccess<DocumentSnapshot>(userSnaphot)
                            .addOnSuccessListener {documentSnapshotList ->

                                for (documentSnapshot in documentSnapshotList) {
                                    val user = documentSnapshot.data

                                    users.add(user)
                                }

                                Log.d("data soal", "$soals")
                                Log.d("panjang soal", "${soals.size}")
                                Log.d("data user", "$users")
                                Log.d("panjang user", "${users.size}")

                                callback(soals, users)
                            }

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