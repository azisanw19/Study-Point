package id.canwar.studypoint.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.canwar.studypoint.R
import id.canwar.studypoint.adapters.SoalItemHolder
import id.canwar.studypoint.firebase.Authentication
import id.canwar.studypoint.firebase.Database
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {

    private lateinit var viewGroup: ViewGroup
    private val database = Database.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewGroup = inflater.inflate(R.layout.fragment_search, container, false) as ViewGroup


        viewGroup.search_button_cari_soal.setOnClickListener {
            val judul = viewGroup.search_cari_judul_soal.text.toString()
            val kategori = viewGroup.search_kategori_soal.text.toString()

            database.getSoal(judul, kategori) { dataSoal, dataUser ->

                val soalItemHolder = SoalItemHolder(activity!!, dataSoal, dataUser)
                viewGroup.soal_recycler_view.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = soalItemHolder
                }

            }
        }

        return viewGroup
    }


}