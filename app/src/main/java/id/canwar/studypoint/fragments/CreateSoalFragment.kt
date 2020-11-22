package id.canwar.studypoint.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import id.canwar.studypoint.R
import id.canwar.studypoint.dialogs.CustomBuatSoalDialog
import id.canwar.studypoint.firebase.Authentication
import id.canwar.studypoint.firebase.Database
import id.canwar.studypoint.firebase.Storage
import id.canwar.studypoint.helpers.REQUEST_CODE_CAMERA
import id.canwar.studypoint.helpers.REQUEST_CODE_GALERI
import kotlinx.android.synthetic.main.dialog_buat_soal.view.*
import kotlinx.android.synthetic.main.fragment_create_soal.view.*

class CreateSoalFragment : Fragment() {

    private lateinit var viewGroup: ViewGroup
    private val database = Database.getInstance()
    private val authentication = Authentication.getInstance()
    private val storage = Storage.getInstance()

    private var storageTask: StorageTask<UploadTask.TaskSnapshot>? = null

    private var soalId: String? = null
    private var countSoal = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewGroup = inflater.inflate(R.layout.fragment_create_soal, container, false) as ViewGroup

        var countSoal = 1

        viewGroup.button_add_choice_create_soal.setOnClickListener {

            if (viewGroup.layout_pilihanD_create_soal.visibility == View.VISIBLE) {
                viewGroup.layout_pilihanE_create_soal.visibility = View.VISIBLE
                viewGroup.button_add_choice_create_soal.visibility = View.GONE
            }

            if (viewGroup.layout_pilihanC_create_soal.visibility == View.VISIBLE) {
                viewGroup.layout_pilihanD_create_soal.visibility = View.VISIBLE
            }

            if (viewGroup.layout_pilihanB_create_soal.visibility == View.VISIBLE) {
                viewGroup.layout_pilihanC_create_soal.visibility = View.VISIBLE
            }

            if (viewGroup.layout_pilihanA_create_soal.visibility == View.VISIBLE) {
                Log.d("visibility a", "new visibility b")
                viewGroup.layout_pilihanB_create_soal.visibility = View.VISIBLE
            }

        }

        viewGroup.upload_gambar_create_soal.setOnClickListener {

            /** Image from kamera gagal **/
            /*
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CODE_CAMERA
                )
            }
             */

            val isInProgress = storageTask?.isInProgress ?: false

            if (storageTask != null && isInProgress) {
                Toast.makeText(context, "Uploading in progress", Toast.LENGTH_SHORT).show()
            } else {
                getDialogImage()
            }
        }

        viewGroup.button_submit_create_soal.setOnClickListener {

            if (soalId == null) {

                val judul = viewGroup.judul_create_soal.text.toString()
                val deskripsi = viewGroup.deskripsi_create_soal.text.toString()
                val waktu = viewGroup.waktu_create_soal.text.toString().toInt()
                val mataPelajaran = viewGroup.mata_pelajaran_create_soal.text.toString()
                val tingkat = viewGroup.jenjang_create_soal.text.toString()
                val pointMax = viewGroup.point_max_create_soal.text.toString().toInt()

                val uid = authentication.getUID()

                val dataSoal = hashMapOf<String, Any>(
                    "judul" to judul,
                    "deskripsi" to deskripsi,
                    "waktu" to waktu,
                    "mataPelajaran" to mataPelajaran,
                    "tingkat" to tingkat,
                    "point" to pointMax,
                    "pembuatId" to uid!!
                )

                val soal = viewGroup.soal_create_soal.text.toString()
                val pilihanA = viewGroup.pilihanA_create_soal.text.toString()
                val pilihanB = viewGroup.pilihanB_create_soal.text.toString()
                val pilihanC = viewGroup.pilihanC_create_soal.text.toString()
                val pilihanD = viewGroup.pilihanD_create_soal.text.toString()
                val pilihanE = viewGroup.pilihanE_create_soal.text.toString()
                val kunci = viewGroup.kunci_create_soal.text.toString()

                val soalSoal = hashMapOf<String, Any>()

                if (soal != "")
                    soalSoal["soal"] = soal
                if (pilihanA != "")
                    soalSoal["a"] = pilihanA
                if (pilihanB != "")
                    soalSoal["b"] = pilihanB
                if (pilihanC != "")
                    soalSoal["c"] = pilihanC
                if (pilihanD != "")
                    soalSoal["d"] = pilihanD
                if (pilihanE != "")
                    soalSoal["e"] = pilihanE

                if (kunci == "")
                    Toast.makeText(context, "Kunci jawaban belum terisi", Toast.LENGTH_SHORT).show()
                else {
                    soalSoal["kunci"] = kunci
                    database.creaetDeskripsiSoal(dataSoal) {
                        soalId = it.id
                        if (storageTask != null) {
                            val uriTask = storageTask?.snapshot?.metadata?.reference?.downloadUrl
                            uriTask?.addOnSuccessListener { uri ->
                                soalSoal["image"] = uri.toString()
                                Log.d("upload image", "$uri")

                                uploadSoal(soalSoal)
                            }
                        } else {
                            uploadSoal(soalSoal)
                        }
                    }
                }
            } else {
                val soal = viewGroup.soal_create_soal.text.toString()
                val pilihanA = viewGroup.pilihanA_create_soal.text.toString()
                val pilihanB = viewGroup.pilihanB_create_soal.text.toString()
                val pilihanC = viewGroup.pilihanC_create_soal.text.toString()
                val pilihanD = viewGroup.pilihanD_create_soal.text.toString()
                val pilihanE = viewGroup.pilihanE_create_soal.text.toString()
                val kunci = viewGroup.kunci_create_soal.text.toString()

                val soalSoal = hashMapOf<String, Any>()

                if (soal != "")
                    soalSoal["soal"] = soal
                if (pilihanA != "")
                    soalSoal["a"] = pilihanA
                if (pilihanB != "")
                    soalSoal["b"] = pilihanB
                if (pilihanC != "")
                    soalSoal["c"] = pilihanC
                if (pilihanD != "")
                    soalSoal["d"] = pilihanD
                if (pilihanE != "")
                    soalSoal["e"] = pilihanE

                if (kunci == "")
                    Toast.makeText(context, "Kunci jawaban belum terisi", Toast.LENGTH_SHORT).show()
                else {
                    soalSoal["kunci"] = kunci
                    if (storageTask != null) {
                        val uriTask = storageTask?.snapshot?.metadata?.reference?.downloadUrl
                        uriTask?.addOnSuccessListener { uri ->
                            soalSoal["image"] = uri.toString()
                            Log.d("upload image", "$uri")

                            uploadSoal(soalSoal)

                        }
                    } else {
                        uploadSoal(soalSoal)
                    }

                }
            }
        }

        return viewGroup

    }

    private fun uploadSoal(soalSoal: Map<String, Any>) {

        database.createSoal(soalId!!, soalSoal) {
            CustomBuatSoalDialog(activity!!) { dialog, view ->
                view.dialog_selesaikan_soal.setOnClickListener {
                    Toast.makeText(
                        context,
                        "Soal anda berhasil disimpan",
                        Toast.LENGTH_SHORT
                    ).show()
                    storageTask = null
                    dialog.dismiss()
                    clearEditText()
                }
                view.dialog_tambah_soal.setOnClickListener {
                    Toast.makeText(
                        context,
                        "soal no $countSoal berhasil disimpan",
                        Toast.LENGTH_SHORT
                    ).show()
                    countSoal++
                    storageTask = null
                    viewGroup.information_soal_create_soal.visibility =
                        View.GONE
                    dialog.dismiss()
                    clearEditText()
                }
            }
        }

    }

    private fun clearEditText() {

        viewGroup.soal_create_soal.setText("")
        viewGroup.pilihanA_create_soal.setText("")
        viewGroup.pilihanB_create_soal.setText("")
        viewGroup.pilihanC_create_soal.setText("")
        viewGroup.pilihanD_create_soal.setText("")
        viewGroup.pilihanE_create_soal.setText("")
        viewGroup.kunci_create_soal.setText("")

    }

    private fun imageChooserGaleri() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_GALERI)

    }

    /** Image from kamera gagal **/
/*
    private fun imageChooserCamera() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = File(context?.filesDir, "Pic.jpg")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }
*/
    private fun getFileExtension(uri: Uri): String {

        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(activity!!.contentResolver.getType(uri))
            .toString()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("upload intent result", "${data?.data}")

        if (requestCode == REQUEST_CODE_GALERI && data != null && data.data != null) {

            val imageUri = data?.data!!

            val extension = getFileExtension(imageUri)!!

            storageTask = storage.uploadSoal(imageUri, extension, context!!)

        }
        /** Image from kamera gagal **/
/*
        if (requestCode == REQUEST_CODE_CAMERA) {

            Log.d("upload from camera", "upload")

            val uri = data?.extras?.get(MediaStore.EXTRA_OUTPUT) as Uri

            val extension = getFileExtension(uri)

            storageTask = storage.uploadSoal(uri, extension, context!!)

        }

 */
    }


}