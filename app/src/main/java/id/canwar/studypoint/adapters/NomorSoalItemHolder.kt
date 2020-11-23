package id.canwar.studypoint.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.canwar.studypoint.R
import kotlinx.android.synthetic.main.nomor_soal_item_holder.view.*

class NomorSoalItemHolder(val context: Context, val warnaNomor: ArrayList<Int>) :
    RecyclerView.Adapter<NomorSoalItemHolder.ViewHolder>() {

    open inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(warna: Int?, position: Int) {
            /**
             * warna 0 -> belum dikerjakan
             * warna 1 -> sudah dikerjakan
             * warna 2 -> Flag
             */

            view.nomor_soal_text_item_holder.text = "${position + 1}"
            if (warna == 0) {
                view.card_view_nomor_soal.background = context.getDrawable(R.drawable.rounded)
                view.nomor_soal_text_item_holder.setTextColor(Color.parseColor("#000000"))
            } else if (warna == 1) {
                view.card_view_nomor_soal.background = context.getDrawable(R.drawable.rounded_gray)
                view.nomor_soal_text_item_holder.setTextColor(Color.parseColor("#ffffff"))
            } else if (warna == 2) {
                view.card_view_nomor_soal.background = context.getDrawable(R.drawable.rounded_red)
                view.nomor_soal_text_item_holder.setTextColor(Color.parseColor("#ffffff"))
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