package id.canwar.studypoint.firebase

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
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

                            soal["soalId"] = document.id

                            Log.d("data soal id", "${document.id}")

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

    fun creaetDeskripsiSoal(data: Map<String, Any>, callback: (documentReference: DocumentReference) -> Unit) {

        db.collection("soal")
                .add(data)
                .addOnSuccessListener(callback)

    }

    fun createSoal(soalId: String, data: Map<String, Any>, callback: (documentReference: DocumentReference) -> Unit) {

        db.collection("soal")
                .document(soalId)
                .collection("soalSoal")
                .add(data)
                .addOnSuccessListener (callback)

    }

    fun getSoalSoal(soalId: String, callback: (data: ArrayList<Map<String, Any>>) -> Unit) {

        db.collection("soal")
            .document(soalId)
            .collection("soalSoal")
            .get()
            .addOnSuccessListener {

                val data: ArrayList<Map<String, Any>> = ArrayList()

                for (documentSnapshot in it.documents) {

                    val soal = documentSnapshot.data
                    soal?.set("key", documentSnapshot.id)

                    if (soal != null) {
                        data.add(soal)
                    }

                }

                Log.d("data soal soal", "$data")
                callback(data)

            }

    }

    fun pushKerjakan(dikerjakan: Map<String, Any>, callback: (id: String) -> Unit) {

        db.collection("dikerjakan")
            .add(dikerjakan)
            .addOnSuccessListener {
                Log.d("puskKerjakan", "${it.id}")
                callback(it.id)
            }
    }

    fun setPoint(idDikerjakan: String, point: Int) {

        db.collection("dikerjakan")
            .document(idDikerjakan)
            .update("point", point)
            .addOnSuccessListener {
                Log.d("update point", "$point")
            }

    }

    fun pushJawaban(idDikerjakan: String, key: String, jawaban: Map<String, Any>) {

        db.collection("dikerjakan")
            .document(idDikerjakan)
            .collection("jawaban")
            .document(key)
            .set(jawaban)
            .addOnSuccessListener {
                Log.d("Save jawaban", "success")
            }

    }

    fun getJawaban(idDikerjakan: String, callback: (jawaban: ArrayList<Map<String, Any>>) -> Unit) {

        db.collection("dikerjakan")
            .document(idDikerjakan)
            .collection("jawaban")
            .get()
            .addOnSuccessListener {

                val jawaban = ArrayList<Map<String, Any>>()

                for (documentSnapshot in it.documents) {
                    val jwb = documentSnapshot.data
                    jwb?.set("key", documentSnapshot.id)

                    jawaban.add(jwb!!)
                }

                callback(jawaban)
            }

    }

    fun getJawaban(idDikerjakan: String, key: String, callback: (documentSnapshot: DocumentSnapshot) -> Unit) {

        db.collection("dikerjakan")
            .document(idDikerjakan)
            .collection("jawaban")
            .document(key)
            .get()
            .addOnSuccessListener {
                callback(it)
            }

    }

    fun updatePointProfile(uid:String, point: Int) {

        getUser(uid) {
            val pointSebelumnya = it?.get("point").toString().toInt()
            val totalPoint = point + pointSebelumnya
            db.collection("users")
                .document(uid)
                .update("point", totalPoint)
                .addOnSuccessListener {
                    Log.d("update point user", "$totalPoint")
                }
        }

    }

    fun updateTotalTugas(uid: String) {

        getUser(uid) {

            val tugasDikerjakan = it?.get("tugasDikerjakan")?.toString()?.toInt() ?: 0
            val totalTugasDikerjakan = tugasDikerjakan + 1
            db.collection("users")
                .document(uid)
                .update("tugasDikerjakan", totalTugasDikerjakan)
                .addOnSuccessListener {
                    Log.d("tambah dikerjakan", "$totalTugasDikerjakan")
                }

        }

    }

    fun updateUser(uid: String, update: Map<String, Any>, callback: () -> Unit) {

        db.collection("users")
            .document(uid)
            .update(update)
            .addOnSuccessListener {
                Log.d("update user", "image update successfully")
                callback()
            }

    }

    fun updateProfileImage(uid: String, image: String) {

        db.collection("users")
            .document(uid)
            .update("image", image)
            .addOnSuccessListener {
                Log.d("update image", "image update successfully")
            }
    }

    fun getItemFromSearch(search: String, callback: (items: ArrayList<Map<String, Any>?>) -> Unit) {

        db.collection("item")
            .orderBy("title")
            .startAt(search)
            .endAt(search + "\uf8ff")
            .get()
            .addOnSuccessListener {
                val items: ArrayList<Map<String, Any>?> = ArrayList()

                for (documentSnapshot in it.documents) {
                    val item = documentSnapshot.data
                    item?.set("key", documentSnapshot.id)

                    items.add(item)
                }

                callback(items)
            }

    }

    fun getPopulerItem(callback: (items: ArrayList<Map<String, Any>?>) -> Unit) {

        db.collection("item")
            .orderBy("penukaran")
            .get()
            .addOnSuccessListener {

                val items: ArrayList<Map<String, Any>?> = ArrayList()

                for (documentSnapshot in it.documents) {
                    val item = documentSnapshot.data
                    item?.set("key", documentSnapshot.id)

                    items.add(item)
                }

                callback(items)

            }
    }

    fun savePenukaran(data: Map<String, Any>, callback: () -> Unit) {

        db.collection("penukaranItem")
            .add(data)
            .addOnSuccessListener {
                callback()
            }

    }


/**
    fun pushPoint(idDikerjakan: String, point: Int) {

        db.collection("dikerjakan")
            .document(idDikerjakan)
            .get()
            .addOnSuccessListener {
                val pointSebelumnya = it.data?.get("point")?.toString()?.toInt()!!

                val totalPoint = pointSebelumnya + point
                db.collection("dikerjakan")
                    .document(idDikerjakan)
                    .update("point", totalPoint)
                    .addOnSuccessListener {
                        Log.d("update point", "$totalPoint")
                    }
            }

    }
**/
}