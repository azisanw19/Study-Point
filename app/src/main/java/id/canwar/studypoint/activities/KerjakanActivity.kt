package id.canwar.studypoint.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.canwar.studypoint.R
import id.canwar.studypoint.adapters.NomorSoalItemHolder
import id.canwar.studypoint.firebase.Authentication
import id.canwar.studypoint.firebase.Database
import id.canwar.studypoint.fragments.SoalFragment
import kotlinx.android.synthetic.main.activity_kerjakan.*

class KerjakanActivity : AppCompatActivity() {

    private val database = Database.getInstance()
    private val authentication = Authentication.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kerjakan)

        val soalId = intent.extras?.getString("soalId")
        var indexSoal = 0
        val pointMax = intent.extras?.getString("pointMax")?.toInt()!!
        val uid = authentication.getUID()!!

        val dikerjakan = mapOf<String, Any>(
            "userId" to uid,
            "soalId" to intent.extras?.getString("soalId")!!,
            "point" to 0,
            "waktuDikerjakan" to System.currentTimeMillis()
        )

        database.getSoalSoal(soalId!!) {
            database.pushKerjakan(dikerjakan) { id ->

                // Initializing soal
                val soal = it[indexSoal]
                val jumlahSoal = it.size - 1
                val point = pointMax / it.size
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_fragment, SoalFragment(soal, id)).commit()
                hiddenNextOrBack(indexSoal, jumlahSoal, id, it, point)

                // initializing nomor
                val warnaNomor = ArrayList<Int>()
                for (i in 0 until jumlahSoal + 1) {
                    warnaNomor.add(0)
                }
                setupPilihNomor(warnaNomor)


                kerjakan_sebelumnya.setOnClickListener { view ->

                    indexSoal--
                    val temp = it[indexSoal]
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_fragment, SoalFragment(temp, id)).commit()

                    // setup header
                    hiddenNextOrBack(indexSoal, jumlahSoal, id, it, point)
                    kerjakan_nomor.text = "No. ${indexSoal + 1}"
                }

                kerjakan_sesudahnya.setOnClickListener { view ->

                    indexSoal++
                    val temp = it[indexSoal]
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_fragment, SoalFragment(temp, id)).commit()

                    // setup header
                    hiddenNextOrBack(indexSoal, jumlahSoal, id, it, point)
                    kerjakan_nomor.text = "No. ${indexSoal + 1}"

                }
            }
        }
    }

    private fun setupPilihNomor(warnaNomor: ArrayList<Int>) {

        val nomorSoalItemHolder = NomorSoalItemHolder(this, warnaNomor)
        kerjakan_pilih_nomor_recycler_view.apply {
            layoutManager =
                LinearLayoutManager(this@KerjakanActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = nomorSoalItemHolder
        }

    }

    private fun hiddenNextOrBack(index: Int, jumlahSoal: Int, id: String, soal: ArrayList<Map<String, Any>>, point: Int) {

        if (index == 0) {
            kerjakan_sebelumnya.visibility = View.INVISIBLE
        } else {
            kerjakan_sebelumnya.visibility = View.VISIBLE
        }

        if (index == jumlahSoal) {
            kerjakan_sesudahnya.visibility = View.INVISIBLE
            kerjakan_selesai.visibility = View.VISIBLE
            kerjakan_selesai.setOnClickListener {
                selesaiMengerjakan(id, soal, point)
            }
        } else {
            kerjakan_sesudahnya.visibility = View.VISIBLE
            kerjakan_selesai.visibility = View.INVISIBLE
        }

    }

    private fun selesaiMengerjakan(idDikerjakan: String, soal: ArrayList<Map<String, Any>>, point: Int) {

        database.getJawaban(idDikerjakan) {
            var count = 0
            for (jawaban in it) {
                for (soal1 in soal) {
                    if (jawaban["key"] == soal1.get("key"))
                        count += point
                }
            }

            val uid = authentication.getUID()!!

            database.setPoint(idDikerjakan, count)
            database.updatePointProfile(uid, count)
            finish()
        }

    }

}