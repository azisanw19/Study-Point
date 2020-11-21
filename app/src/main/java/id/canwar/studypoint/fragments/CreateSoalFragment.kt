package id.canwar.studypoint.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import id.canwar.studypoint.R
import id.canwar.studypoint.dialogs.CustomBuatSoalDialog
import id.canwar.studypoint.firebase.Authentication
import id.canwar.studypoint.firebase.Database
import kotlinx.android.synthetic.main.dialog_buat_soal.view.*
import kotlinx.android.synthetic.main.fragment_create_soal.view.*

class CreateSoalFragment() : Fragment() {

    private lateinit var viewGroup: ViewGroup
    private val database = Database.getInstance()
    private val authentication = Authentication.getInstance()

    private var soalId: String? = null

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
                    database.creaetDeskripsiSoal(dataSoal) {
                        soalId = it.id
                        database.createSoal(soalId!!, soalSoal) {documentCreateSoal ->
                            CustomBuatSoalDialog(activity!!) { dialog, view ->
                                view.dialog_selesaikan_soal.setOnClickListener {
                                    Toast.makeText(context, "Soal anda berhasil disimpan", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()
                                    clearEditText()
                                }
                                view.dialog_tambah_soal.setOnClickListener {
                                    Toast.makeText(context, "soal no $countSoal berhasil disimpan", Toast.LENGTH_SHORT).show()
                                    countSoal++
                                    viewGroup.information_soal_create_soal.visibility = View.GONE
                                    dialog.dismiss()
                                    clearEditText()
                                }
                            }
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

                    database.createSoal(soalId!!, soalSoal) {
                        CustomBuatSoalDialog(activity!!) { dialog, view ->
                            view.dialog_selesaikan_soal.setOnClickListener {
                                Toast.makeText(context, "Soal anda berhasil disimpan", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                                clearEditText()
                            }
                            view.dialog_tambah_soal.setOnClickListener {
                                Toast.makeText(context, "soal no $countSoal berhasil disimpan", Toast.LENGTH_SHORT).show()
                                countSoal++
                                viewGroup.information_soal_create_soal.visibility = View.GONE
                                dialog.dismiss()
                                clearEditText()
                            }
                        }
                    }

                }
            }
        }

        return viewGroup

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


}