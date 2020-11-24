package id.canwar.studypoint.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.canwar.studypoint.R
import id.canwar.studypoint.adapters.ItemItemHolder
import id.canwar.studypoint.firebase.Database
import kotlinx.android.synthetic.main.fragment_tukar_point.view.*

class SearchItemFragment : Fragment() {

    private lateinit var view: ViewGroup
    private val database = Database.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view = inflater.inflate(R.layout.fragment_tukar_point, container, false) as ViewGroup

        initUISearch()
        initUIPopuler()


        return view
    }

    private fun initUIPopuler() {

        database.getPopulerItem {

            val itemItemHolder = ItemItemHolder(context!!, it)
            view.populer_item_recycler_view.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = itemItemHolder
            }

        }

    }

    private fun initUISearch() {
        view.item_search_item_holder.visibility = View.GONE

        view.button_cari_item_tukar_point.setOnClickListener {
            val search = view.search_item_tukar_point.text?.toString()

            if (search != null && search != "") {

                database.getItemFromSearch(search) {
                    view.item_search_item_holder.visibility = View.VISIBLE

                    val itemItemHolder = ItemItemHolder(context!!, it)
                    view.hasil_pencarian_recycler_view.apply {
                        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        adapter = itemItemHolder
                    }

                }

            }

        }

    }

}