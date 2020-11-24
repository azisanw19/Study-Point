package id.canwar.studypoint.activities

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
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

    private var countDownTimer: CountDownTimer? = null
    private var timeMilliSecond: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kerjakan)

        val soalId = intent.extras?.getString("soalId")
        var indexSoal = 0
        val pointMax = intent.extras?.getString("pointMax")?.toInt()!!
        val uid = authentication.getUID()!!
        timeMilliSecond = intent.extras?.getString("waktu")?.toLong()!! * 1000 * 60

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
                val warnaNomor: ArrayList<Map<String, Any>> = ArrayList()
                for (soalData in it) {
                    val dataSoal = mapOf<String, Any>(
                        "key" to soalData["key"].toString(),
                        "idDikerjakan" to id
                    )
                    warnaNomor.add(dataSoal)
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

                    // setup nomor
                    setupPilihNomor(warnaNomor)
                }

                kerjakan_sesudahnya.setOnClickListener { view ->

                    indexSoal++
                    val temp = it[indexSoal]
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_fragment, SoalFragment(temp, id)).commit()

                    // setup header
                    hiddenNextOrBack(indexSoal, jumlahSoal, id, it, point)
                    kerjakan_nomor.text = "No. ${indexSoal + 1}"

                    // setup pilih nomor
                    setupPilihNomor(warnaNomor)

                }

                startTimer(id, it, point)
            }
        }
    }

    private fun startTimer(idDikerjakan: String, soal: ArrayList<Map<String, Any>>, point: Int) {
        countDownTimer = object : CountDownTimer(timeMilliSecond!!, 1000) {
            override fun onTick(p0: Long) {
                timeMilliSecond = p0
                updateKerjakanwaktu()
            }

            override fun onFinish() {
                selesaiMengerjakan(idDikerjakan, soal, point)
            }

        }.start()
    }

    private fun updateKerjakanwaktu() {

        Log.d("time update", "update time")

        val minutes = ((timeMilliSecond!! / 1000) / 60).toInt()
        val seconds = ((timeMilliSecond!! / 1000) % 60).toInt()

        var timeText = "$minutes:"
        if (seconds < 10)
            timeText += "0"
        timeText += "$seconds"

        kerjakan_waktu_tersisa.text = timeText

    }

    private fun setupPilihNomor(warnaNomor: ArrayList<Map<String, Any>>) {

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
                    if (jawaban["key"] == soal1["key"]) {
                        val kunciSoal = soal1["kunci"].toString()
                        val jawab = jawaban["jawab"].toString()
                        if (kunciSoal == jawab) {
                            count += point
                        }
                    }
                }
            }

            val uid = authentication.getUID()!!

            database.setPoint(idDikerjakan, count)
            database.updatePointProfile(uid, count)
            countDownTimer?.cancel()

            Toast.makeText(this, "Score anda $count", Toast.LENGTH_LONG).show()

            finish()
        }

    }

}