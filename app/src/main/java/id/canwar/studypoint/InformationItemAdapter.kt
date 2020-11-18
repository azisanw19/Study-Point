package id.canwar.studypoint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.information_item_holder.view.*

class InformationItemAdapter(val informations: ArrayList<Map<String, Any>?>) : RecyclerView.Adapter<InformationItemAdapter.ViewHolder>() {


    open inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(information: Map<String, Any>?) {

            view.information_item_text.text = information?.get("content").toString() ?: ""

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.information_item_holder, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(informations[position])
    }

    override fun getItemCount(): Int = informations.size

}