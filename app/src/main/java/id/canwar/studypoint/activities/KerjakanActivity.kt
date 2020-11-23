package id.canwar.studypoint.activities

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import id.canwar.studypoint.R
import id.canwar.studypoint.firebase.Database
import kotlinx.android.synthetic.main.activity_kerjakan.*

class KerjakanActivity : AppCompatActivity() {

    private var timeLeftInMilliSecond: Long? = null
    private val database = Database.getInstance()
    private var countDownTimer: CountDownTimer? = null

    private var nomorSoal: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kerjakan)

        val bundle = intent.extras

        timeLeftInMilliSecond = bundle?.getString("waktu")?.toLong()!! * 1000 * 60
        updateText()

        val soalId = bundle.getString("soalId")

        database.getSoalSoal(soalId!!) { soalSoal ->

            tampilkanSoal(soalSoal[0])

            val size = soalSoal.size
            checkNomor(size)

            kerjakan_sebelumnya.setOnClickListener {
                Log.d("kerjakan klik", "sebelumnya")
                checkNomor(size)
                nomorSoal -= 1
                tampilkanSoal(soalSoal[nomorSoal])
            }
            kerjakan_sesudahnya.setOnClickListener {
                nomorSoal += 1
                checkNomor(size)
                tampilkanSoal(soalSoal[nomorSoal])
                Log.d("kerjakan klik", "sesudahnya")

            }

            startTimer()

        }

    }

    private fun checkNomor(size: Int) {
        if (nomorSoal == 1)
            kerjakan_sebelumnya.visibility = View.INVISIBLE
        else
            kerjakan_sebelumnya.visibility = View.VISIBLE

        if (nomorSoal == size - 1)
            kerjakan_sesudahnya.visibility = View.INVISIBLE
        else
            kerjakan_sesudahnya.visibility = View.VISIBLE
    }

    private fun tampilkanSoal(soal: Map<String, Any>) {

        kerjakan_nomor.text = "No ${nomorSoal + 1}"

        soal_kerjakan.text = soal["soal"]?.toString()
        val image = soal["image"]?.toString()
        if (image != null) {
            try {
                Picasso.get().load(image).into(image_soal_kerjakan)
            } catch (e: Exception) {
                Log.e("Picasso failed", image)
            }
        } else
            image_soal_kerjakan.visibility = View.GONE

        pilihanA_kerjakan.visibility = View.GONE
        pilihanB_kerjakan.visibility = View.GONE
        pilihanC_kerjakan.visibility = View.GONE
        pilihanD_kerjakan.visibility = View.GONE
        pilihanE_kerjakan.visibility = View.GONE

        val pilihanA = soal["a"]?.toString()
        val pilihanB = soal["b"]?.toString()
        val pilihanC = soal["c"]?.toString()
        val pilihanD = soal["d"]?.toString()
        val pilihanE = soal["e"]?.toString()

        if (pilihanA != null) {
            pilihanA_kerjakan.visibility = View.VISIBLE
            pilihanA_kerjakan.text = pilihanA
        }

        if (pilihanB != null) {
            pilihanB_kerjakan.visibility = View.VISIBLE
            pilihanB_kerjakan.text = pilihanB
        }

        if (pilihanC != null) {
            pilihanC_kerjakan.visibility = View.VISIBLE
            pilihanC_kerjakan.text = pilihanC
        }

        if (pilihanD != null) {
            pilihanD_kerjakan.visibility = View.VISIBLE
            pilihanD_kerjakan.text = pilihanD
        }

        if (pilihanE != null) {
            pilihanE_kerjakan.visibility = View.VISIBLE
            pilihanE_kerjakan.text = pilihanE
        }

    }

    private fun startTimer() {

        countDownTimer = object : CountDownTimer(timeLeftInMilliSecond!!, 1000) {
            override fun onTick(tick: Long) {
                timeLeftInMilliSecond = tick
                updateText()
            }

            override fun onFinish() {
                TODO("Not yet implemented")
            }

        }.start()

    }

    private fun updateText() {

        Log.d("time update", "update time")

        val minutes = ((timeLeftInMilliSecond!! / 1000) / 60).toInt()
        val seconds = ((timeLeftInMilliSecond!! / 1000) % 60).toInt()

        var timeLeftText = "$minutes:"
        if (seconds < 10)
            timeLeftText += "0"
        timeLeftText += "$seconds"

        kerjakan_waktu_tersisa.text = timeLeftText


    }

}