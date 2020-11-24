package id.canwar.studypoint.firebase

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask

class Storage {

    companion object {
        var storage: Storage? = null
        fun getInstance(): Storage {

            if (storage == null)
                storage = Storage()

            return storage!!
        }

    }

    private val storage = FirebaseStorage.getInstance()


    fun uploadSoal(
        imageUri: Uri,
        extensionImage: String,
        context: Context
    ): StorageTask<UploadTask.TaskSnapshot> {

        return storage.getReference("soal")
            .child("${System.currentTimeMillis()}.$extensionImage")
            .putFile(imageUri)
            .addOnSuccessListener {

                Toast.makeText(context, "Upload image soal berhasil!", Toast.LENGTH_LONG).show()

                Log.d("upload image", "${it.uploadSessionUri}")
//                val uriTask = it.metadata?.reference?.downloadUrl
//                uriTask?.addOnSuccessListener {
//
//                    Log.d("upload image", "$it")
//                }


            }
            .addOnFailureListener {

            }
            .addOnProgressListener {

            }

    }

    fun uploadProfile(imageUri: Uri, extensionImage: String, callback: (task:  UploadTask.TaskSnapshot) -> Unit){

        storage.getReference("profile")
            .child("${System.currentTimeMillis()}.$extensionImage")
            .putFile(imageUri)
            .addOnSuccessListener {

                Log.d("upload profile", "${it.uploadSessionUri}")

                callback(it)
            }
    }

}