package id.canwar.studypoint.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.canwar.studypoint.R
import kotlinx.android.synthetic.main.soal_item_holder.view.*

class SoalItemHolder(val dataSoal: ArrayList<Map<String, Any>?>, val dataUser: ArrayList<Map<String, Any>?>) : RecyclerView.Adapter<SoalItemHolder.ViewHolder>() {

    open inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(soal: Map<String, Any>?, user: Map<String, Any>?) {

            view.soal_item_holder_judul.text = soal?.get("judul").toString()

            val firstName = user?.get("firstName").toString()
            val lastName = user?.get("lastName").toString()

            view.soal_item_holder_guru.text = "$firstName $lastName"

            view.soal_item_holder_deskripsi.text = soal?.get("deskripsi").toString()

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