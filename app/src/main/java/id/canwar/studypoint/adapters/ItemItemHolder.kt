package id.canwar.studypoint.adapters

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.canwar.studypoint.R
import id.canwar.studypoint.firebase.Authentication
import id.canwar.studypoint.firebase.Database
import kotlinx.android.synthetic.main.item_item_holder.view.*

class ItemItemHolder(val context: Context, val items: ArrayList<Map<String, Any>?>) :
    RecyclerView.Adapter<ItemItemHolder.ViewHolder>() {

    private val database = Database.getInstance()
    private val authentication = Authentication.getInstance()

    open inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Map<String, Any>?) {

            val image = item?.get("image")?.toString()
            try {
                Picasso.get().load(image).placeholder(R.drawable.ic_image_none)
                    .into(view.image_item_holder)
            } catch (e: Exception) {
                Log.e("error picasso", "$image")
            }

            view.point_item_holder.text = item?.get("point")?.toString()
            view.title_item_holder.text = item?.get("title")?.toString()

            view.card_view_item_item_holder.setOnClickListener {
                Log.d("click item", "item")

                AlertDialog.Builder(context)
                    .setTitle("Penukaran")
                    .setMessage("Apakah anda yakin?")
                    .setPositiveButton("Ya") { dialog, which ->

                        val uid = authentication.getUID()
                        val itemId = item?.get("key").toString()
                        val penukaran = mapOf<String, Any>(
                            "userId" to uid!!,
                            "itemId" to itemId,
                            "waktu" to System.currentTimeMillis()
                        )

                        val point = item?.get("point").toString().toInt()

                        database.getUser(uid) {

                            val userPoint = it?.get("point").toString().toInt()

                            if (userPoint < point) {
                                Toast.makeText(
                                    context,
                                    "Maaf poin anda tidak mencukupi untuk melakukan penukaran!",
                                    Toast.LENGTH_LONG
                                ).show()
                                dialog.dismiss()
                            } else {
                                database.savePenukaran(penukaran) {
                                    Toast.makeText(context, "Penukaran berhasil", Toast.LENGTH_LONG)
                                        .show()

                                    database.updatePointProfile(uid, point)
                                    dialog.dismiss()
                                }
                            }

                        }

                    }
                    .setNegativeButton("Cancel") { dialog, which ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_item_holder, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

}