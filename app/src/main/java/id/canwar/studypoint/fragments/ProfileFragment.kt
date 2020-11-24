package id.canwar.studypoint.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import id.canwar.studypoint.R
import id.canwar.studypoint.firebase.Authentication
import id.canwar.studypoint.firebase.Database
import id.canwar.studypoint.firebase.Storage
import id.canwar.studypoint.helpers.REQUEST_CODE_GALERI
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.lang.Exception

class ProfileFragment : Fragment() {

    private lateinit var view: ViewGroup
    private val database = Database.getInstance()
    private val authentication = Authentication.getInstance()

    private val storage = Storage.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view = inflater.inflate(R.layout.fragment_profile, container, false) as ViewGroup

        val uid = authentication.getUID()
        database.getUser(uid!!) {

            initUI(it)

        }

        return view


    }

    private fun initUI(dataUser: Map<String, Any>?) {

        val image = dataUser?.get("image")?.toString()
        val tugasDikerjakan = dataUser?.get("tugasDikerjakan")?.toString() ?: "0"
        val point = dataUser?.get("point")?.toString() ?: "0"
        val email = dataUser?.get("email").toString()
        val firstName = dataUser?.get("firstName").toString()
        val lastName = dataUser?.get("lastName").toString()
        val birthDay = dataUser?.get("birthday").toString()

        try {
            Picasso.get().load(image).placeholder(R.drawable.ic_person).into(view.image_profile)
        } catch (e: Exception) {
            Log.e("Picasso failed", e.toString())
        }

        view.profile_tugas_dikerjakan.text = tugasDikerjakan
        view.profile_point.text = point
        view.profile_email.apply {
            setText(email)
            isClickable = false
            isEnabled = false
        }
        view.profile_first_name.setText(firstName)
        view.profile_last_name.setText(lastName)
        view.profile_birthday.setText(birthDay)

        view.profile_ubah_data.setOnClickListener {

            val uid = authentication.getUID()

            val firstName = view.profile_first_name.text.toString()
            val lastName = view.profile_last_name.text.toString()
            val birthDay = view.profile_birthday.text.toString()

            val dataUser = mapOf<String, Any>(
                "firstName" to firstName,
                "lastName" to lastName,
                "birthday" to birthDay
            )

            database.updateUser(uid!!, dataUser) {
                Toast.makeText(context, "Berhasil update data profile", Toast.LENGTH_SHORT).show()
            }
        }

        // click handle profile photo
        view.image_profile.setOnClickListener {

            getDialogImage()

        }

    }

    private fun getDialogImage() {

        val charSequence = arrayOf("Galeri")

        val dialog = AlertDialog.Builder(context)
            .setTitle("Upload image")
            .setItems(charSequence) { _, which ->

                when (which) {
                    0 -> imageChooserGaleri()
                }

            }

        dialog.create()
        dialog.show()

    }

    private fun imageChooserGaleri() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_GALERI)

    }

    private fun getFileExtension(uri: Uri): String {

        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(activity!!.contentResolver.getType(uri))
            .toString()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("upload intent result", "${data?.data}")

        if (requestCode == REQUEST_CODE_GALERI && data != null && data.data != null) {

            val imageUri = data?.data!!

            val extension = getFileExtension(imageUri)!!

            storage.uploadProfile(imageUri, extension) {

                Toast.makeText(context, "Upload image profile berhasil", Toast.LENGTH_LONG).show()
                val imageTask = it.metadata?.reference?.downloadUrl
                imageTask?.addOnSuccessListener { uri ->
                    try {
                        Picasso.get().load(uri).placeholder(R.drawable.ic_person).into(view.image_profile)
                    } catch (e: Exception) {
                        Log.e("Picasso failed", e.toString())
                    }

                    updateDbImageProfile(uri.toString())
                }

            }

        }
    }

    private fun updateDbImageProfile(image: String) {

        val uid = authentication.getUID()

        database.updateProfileImage(uid!!, image)

    }

}