package id.canwar.studypoint.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.canwar.studypoint.R
import kotlinx.android.synthetic.main.task_done_item_holder.view.*

class TaskDoneItemHolder(val tasks: ArrayList<Map<String, Any>?>, val soals: ArrayList<Map<String, Any>?>) : RecyclerView.Adapter<TaskDoneItemHolder.ViewHolder>() {

    open inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(task: Map<String, Any>?, soal: Map<String, Any>?) {
            view.task_name_item_holder.text = soal?.get("judul").toString()
            view.task_score_item_holder.text = task?.get("point").toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_done_item_holder, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position], soals[position])
    }

    override fun getItemCount(): Int = tasks.size

}