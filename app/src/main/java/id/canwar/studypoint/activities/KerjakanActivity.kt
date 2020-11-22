package id.canwar.studypoint.activities

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import id.canwar.studypoint.R
import id.canwar.studypoint.firebase.Database
import kotlinx.android.synthetic.main.activity_kerjakan.*

class KerjakanActivity : AppCompatActivity() {

    private var timeLeftInMilliSecond: Long? = null
    private val database = Database.getInstance()
    private var countDownTimer: CountDownTimer? = null

    private var soal: ArrayList<Map<String, Any>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kerjakan)

        val bundle = intent.extras

        timeLeftInMilliSecond = bundle?.getString("waktu")?.toLong()!! * 1000 * 60
        updateText()

        val soalId = bundle.getString("soalId")

        database.getSoalSoal(soalId!!) {

            soal = it

            startTimer()

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