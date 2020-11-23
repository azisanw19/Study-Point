package id.canwar.studypoint.adapters

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.canwar.studypoint.R
import id.canwar.studypoint.activities.KerjakanActivity
import id.canwar.studypoint.dialogs.CustomDetailSoal
import kotlinx.android.synthetic.main.dialog_details_soal.view.*
import kotlinx.android.synthetic.main.soal_item_holder.view.*

class SoalItemHolder(val activity: Activity, val dataSoal: ArrayList<Map<String, Any>?>, val dataUser: ArrayList<Map<String, Any>?>) : RecyclerView.Adapter<SoalItemHolder.ViewHolder>() {

    open inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(soal: Map<String, Any>?, user: Map<String, Any>?) {

            view.soal_item_holder_judul.text = soal?.get("judul").toString()

            val firstName = user?.get("firstName").toString()
            val lastName = user?.get("lastName").toString()

            view.soal_item_holder_guru.text = "$firstName $lastName"

            view.soal_item_holder_deskripsi.text = soal?.get("deskripsi").toString()

            view.soal_item_holder_details.setOnClickListener {
                CustomDetailSoal(activity) { dialog, viewDialog ->

                    viewDialog.dialog_judul.text = soal?.get("judul").toString()
                    viewDialog.dialog_guru.text = "$firstName $lastName"
                    viewDialog.dialog_deskripsi.text = soal?.get("deskripsi").toString()

                    val waktu = soal?.get("waktu").toString()

                    viewDialog.dialog_waktu.text = "$waktu menit"

                    val mataPelajaran = soal?.get("mataPelajaran").toString()
                    val tingkat = soal?.get("tingkat").toString()
                    viewDialog.dialog_kategori.text = "$mataPelajaran, $tingkat"

                    Log.d("soal-soal", "${soal?.get("soalSoal")}")

                    viewDialog.dialog_kerjakan_soal.setOnClickListener {

                        val intent = Intent(activity, KerjakanActivity::class.java).apply {

                            val bundle = Bundle().apply {
                                putExtra("soalId", soal?.get("soalId").toString())
                                putExtra("waktu", waktu)
                                putExtra("pointMax", soal?.get("point").toString())
                            }

                            putExtras(bundle)

                        }

                        activity.startActivity(intent)
                        dialog.dismiss()

                    }

                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.soal_item_holder, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSoal[position], dataUser[position])
    }

    override fun getItemCount(): Int = dataSoal.size


}