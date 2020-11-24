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

        val keySoal = soal["key"].toString()
        var jawab: Map<String, Any>?
        database.getJawaban(idDikerjakan, keySoal) {
            jawab = it.data
            setCheckedFromDb(jawab)
        }
        initUISoal()

        return view

    }

    private fun cekJawaban() {

        val jawaban = onRadioButtonClicked()
        val tandai = view.tandai_soal_kerjakan.isChecked
        val key = soal["key"].toString()
        val jwb = mutableMapOf<String, Any>()
        if (jawaban != null) {
            jwb["jawab"] = jawaban
        }
        jwb["tandai"] = tandai

        // bug karna async jawaban soal nomor terakhir rawan tidak tersimpan di database
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

    private fun setCheckedFromDb(jawab: Map<String, Any>?) {

        Log.d("hasil jawaban", "jawab")

        if (jawab != null) {
            val pilihan = jawab["jawab"]?.toString()
            val tandai = jawab["tandai"]?.toString()?.toBoolean()
            if (pilihan != null) {
                when (pilihan) {
                    "a" -> view.pilihanA_kerjakan.isChecked = true
                    "b" -> view.pilihanB_kerjakan.isChecked = true
                    "c" -> view.pilihanC_kerjakan.isChecked = true
                    "d" -> view.pilihanD_kerjakan.isChecked = true
                    "e" -> view.pilihanE_kerjakan.isChecked = true
                }
            }
            if (tandai != null) {
                view.tandai_soal_kerjakan.isChecked = tandai
            }

        }

    }

    private fun onRadioButtonClicked(): String? =
        when (view.kerjakan_pilih_jawaban.checkedRadioButtonId) {
            R.id.pilihanA_kerjakan -> "a"
            R.id.pilihanB_kerjakan -> "b"
            R.id.pilihanC_kerjakan -> "c"
            R.id.pilihanD_kerjakan -> "d"
            R.id.pilihanE_kerjakan -> "e"
            else -> null
        }
}