package id.canwar.studypoint.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import id.canwar.studypoint.R
import id.canwar.studypoint.firebase.Database
import kotlinx.android.synthetic.main.fragment_soal.view.*

class SoalFragment(val soal: Map<String, Any>, val idDikerjakan: String) : Fragment() {

    private lateinit var view: ViewGroup
    private val database = Database.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view = inflater.inflate(R.layout.fragment_soal, container, false) as ViewGroup

        initUISoal()

        return view

    }

    private fun cekJawaban() {

        val kunci = soal["kunci"].toString()
        val jawaban = onRadioButtonClicked()
        val key = soal["key"].toString()
        val jwb = mapOf<String, Any>(
            "jawab" to jawaban!!
        )
        database.pushJawaban(idDikerjakan, key, jwb)

    }

    override fun onDestroy() {
        super.onDestroy()

        cekJawaban()
    }

    private fun initUISoal() {

        view.soal_kerjakan.text = soal["soal"]?.toString()

        view.pilihanB_kerjakan.visibility = View.GONE
        view.pilihanA_kerjakan.visibility = View.GONE
        view.pilihanC_kerjakan.visibility = View.GONE
        view.pilihanD_kerjakan.visibility = View.GONE
        view.pilihanE_kerjakan.visibility = View.GONE

        val pilihanA = soal["a"]?.toString()
        val pilihanB = soal["b"]?.toString()
        val pilihanC = soal["c"]?.toString()
        val pilihanD = soal["d"]?.toString()
        val pilihanE = soal["e"]?.toString()

        if (pilihanA != null) {
            view.pilihanA_kerjakan.visibility = View.VISIBLE
            view.pilihanA_kerjakan.text = pilihanA
        }

        if (pilihanB != null) {
            view.pilihanB_kerjakan.visibility = View.VISIBLE
            view.pilihanB_kerjakan.text = pilihanB
        }

        if (pilihanC != null) {
            view.pilihanC_kerjakan.visibility = View.VISIBLE
            view.pilihanC_kerjakan.text = pilihanC
        }

        if (pilihanD != null) {
            view.pilihanD_kerjakan.visibility = View.VISIBLE
            view.pilihanD_kerjakan.text = pilihanD
        }

        if (pilihanE != null) {
            view.pilihanE_kerjakan.visibility = View.VISIBLE
            view.pilihanE_kerjakan.text = pilihanE
        }

        // Handle image soal
        val image = soal["image"]?.toString()
        if (image != null) {
            try {
                Picasso.get().load(image).into(view.image_soal_kerjakan)
            } catch (e: Exception) {
                Log.e("Picasso failed", image)
            }
        } else
            view.image_soal_kerjakan.visibility = View.GONE


    }

    private fun onRadioButtonClicked(): String? = when(view.kerjakan_pilih_jawaban.checkedRadioButtonId) {
        R.id.pilihanA_kerjakan -> "a"
        R.id.pilihanB_kerjakan -> "b"
        R.id.pilihanC_kerjakan -> "c"
        R.id.pilihanD_kerjakan -> "d"
        R.id.pilihanE_kerjakan -> "e"
        else -> null
    }
}