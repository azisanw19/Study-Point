package id.canwar.studypoint

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.dashboard.view.*

class DashboardFragment : Fragment() {

    private lateinit var viewGroup: ViewGroup
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewGroup = inflater.inflate(R.layout.dashboard, container, false) as ViewGroup

        getInformation(viewGroup)

        return viewGroup
    }

    private fun getInformation(view: View) {

        Log.d("data", "getInformation")

        db.collection("information")
                .get()
                .addOnSuccessListener {result ->

                    Log.d("data", "${result.documents}")

                    val data: ArrayList<Map<String, Any>?> = ArrayList()

                    for (document in result.documents) {
                        Log.d("data collection", "${document.id} => ${document.data}")
                        data.add(document.data)
                    }

                    val informationItemAdapter = InformationItemAdapter(data)
                    view.info_dashboard_recycler_view.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = informationItemAdapter
                    }


                }

    }


}