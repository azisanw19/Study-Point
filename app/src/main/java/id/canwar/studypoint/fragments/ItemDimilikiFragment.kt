package id.canwar.studypoint.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.canwar.studypoint.R
import id.canwar.studypoint.adapters.ItemDimilikiItemHolder
import id.canwar.studypoint.firebase.Authentication
import id.canwar.studypoint.firebase.Database
import kotlinx.android.synthetic.main.fragment_item_dimiliki.view.*

class ItemDimilikiFragment : Fragment() {

    private lateinit var view: ViewGroup
    private val database = Database.getInstance()
    private val authentication = Authentication.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view = inflater.inflate(R.layout.fragment_item_dimiliki, container, false) as ViewGroup

        initKategori()

        return view
    }

    private fun initKategori() {

        val uid = authentication.getUID()

        database.getItemDimiliki(uid!!) { data ->

            val itemDimilikiItemHolder = ItemDimilikiItemHolder(context!!, data)
            view.item_dimiliki_recycler_view.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = itemDimilikiItemHolder
            }

        }


    }

}