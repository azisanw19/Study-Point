package id.canwar.studypoint.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.canwar.studypoint.adapters.InformationItemAdapter
import id.canwar.studypoint.R
import id.canwar.studypoint.adapters.TaskDoneItemHolder
import id.canwar.studypoint.firebase.Authentication
import id.canwar.studypoint.firebase.Database
import kotlinx.android.synthetic.main.fragment_dashboard.view.*

class DashboardFragment : Fragment() {

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