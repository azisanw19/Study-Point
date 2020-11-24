package id.canwar.studypoint.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.canwar.studypoint.R
import id.canwar.studypoint.firebase.Database
import kotlinx.android.synthetic.main.nomor_soal_item_holder.view.*

class NomorSoalItemHolder(val context: Context, val warnaNomor: ArrayList<Map<String, Any>>) :
    RecyclerView.Adapter<NomorSoalItemHolder.ViewHolder>() {

    private val database = Database.getInstance()

    open inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(warna: Map<String, Any>, position: Int) {
            Log.d("nomor soal", "$warna")
            view.nomor_soal_text_item_holder.text = "${position + 1}"
            val idDikerjakan = warna["idDikerjakan"].toString()
            val key = warna["key"].toString()
            database.getJawaban(idDikerjakan, key) {
                val dataJawab = it.data
                if (dataJawab != null) {
                    val jawab = dataJawab["jawab"]?.toString()
                    val tandai = dataJawab["tandai"]?.toString()?.toBoolean()
                    if (jawab != null) {
                        if (tandai != null) {
                            if (tandai) {
                                view.card_view_nomor_soal.background = context.getDrawable(R.drawable.rounded_red)
                                view.nomor_soal_text_item_holder.setTextColor(Color.parseColor("#ffffff"))
                            }
                            else {
                                view.card_view_nomor_soal.background = context.getDrawable(R.drawable.rounded_gray)
                                view.nomor_soal_text_item_holder.setTextColor(Color.parseColor("#ffffff"))
                            }
                        } else {
                            view.card_view_nomor_soal.background = context.getDrawable(R.drawable.rounded_gray)
                            view.nomor_soal_text_item_holder.setTextColor(Color.parseColor("#ffffff"))
                        }
                    } else {
                        if (tandai != null) {
                            if (tandai) {
                                view.card_view_nomor_soal.background = context.getDrawable(R.drawable.rounded_red)
                                view.nomor_soal_text_item_holder.setTextColor(Color.parseColor("#ffffff"))
                            } else {
                                view.card_view_nomor_soal.background = context.getDrawable(R.drawable.rounded)
                                view.nomor_soal_text_item_holder.setTextColor(Color.parseColor("#000000"))
                            }
                        } else {
                            view.card_view_nomor_soal.background = context.getDrawable(R.drawable.rounded)
                            view.nomor_soal_text_item_holder.setTextColor(Color.parseColor("#000000"))
                        }

                    }
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.nomor_soal_item_holder, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(warnaNomor[position], position)
    }

    override fun getItemCount(): Int = warnaNomor.size

}