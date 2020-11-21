package id.canwar.studypoint.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.canwar.studypoint.adapters.InformationItemAdapter
import id.canwar.studypoint.R
import id.canwar.studypoint.adapters.TaskDoneItemHolder
import id.canwar.studypoint.firebase.Authentication
import id.canwar.studypoint.firebase.Database
    import kotlinx.android.synthetic.main.fragment_dashboard.view.*

class DashboardFragment(val userData: Map<String, Any>?) : Fragment() {

    private lateinit var viewGroup: ViewGroup
    private val database = Database.getInstance()
    private val authentication = Authentication.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewGroup = inflater.inflate(R.layout.fragment_dashboard, container, false) as ViewGroup

        getInformation(viewGroup)
        getTaskDone(viewGroup)

        if (userData?.get("role")?.toString() == "student") {
            viewGroup.dashboard_search_or_create_soal.text = "Cari Soal"
            viewGroup.dashboard_search_or_create_soal.setOnClickListener {
                (activity as AppCompatActivity).supportActionBar?.title = "Cari Soal"
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container, SearchFragment())?.commit()
            }
        } else if (userData?.get("role")?.toString() == "teacher") {
            viewGroup.dashboard_search_or_create_soal.text = "Buat Soal"
            viewGroup.dashboard_search_or_create_soal.setOnClickListener {
                (activity as AppCompatActivity).supportActionBar?.title = "Buat Soal"
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container, CreateSoalFragment())?.commit()
            }
        }


        return viewGroup
    }

    private fun getInformation(view: View) {

        database.getInformation {

            val informationItemAdapter = InformationItemAdapter(it)
            view.info_dashboard_recycler_view.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = informationItemAdapter
            }
        }

    }

    private fun getTaskDone(view: View) {

        val uid = authentication.getUID()

        database.getTaskDone(uid!!) { dataTask, dataSoal ->

            val taskDoneItemHolder = TaskDoneItemHolder(dataTask, dataSoal)
            view.task_done_recycler_view.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = taskDoneItemHolder
            }

        }

    }


}