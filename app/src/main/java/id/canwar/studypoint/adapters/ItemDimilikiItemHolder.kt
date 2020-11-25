package id.canwar.studypoint.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.canwar.studypoint.R
import kotlinx.android.synthetic.main.list_dimiliki_item_holder.view.*
import java.lang.Exception

class ItemDimilikiItemHolder(val context: Context, val items: ArrayList<Map<String, Any>?>) :
    RecyclerView.Adapter<ItemDimilikiItemHolder.ViewHolder>() {

    open inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Map<String, Any>?) {

            val image = item?.get("image").toString()
            try {
                Picasso.get().load(image).placeholder(R.drawable.ic_image_none)
                    .into(view.image_item_dimiliki_holder)
            } catch (e: Exception) {
                Log.e("Error picasso", "$image")
            }

            view.title_item_dimiliki_holder.text = item?.get("title").toString()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_dimiliki_item_holder, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

}