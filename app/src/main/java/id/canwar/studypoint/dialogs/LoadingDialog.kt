package id.canwar.studypoint.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.view.ViewGroup
import id.canwar.studypoint.R

class LoadingDialog (private val activity: Activity) {

    private var dialog: AlertDialog
    private val view = activity.layoutInflater.inflate(R.layout.dialog_loading, null) as ViewGroup

    init {

        dialog = AlertDialog.Builder(activity)
            .setView(view)
            .setCancelable(false)
            .create()
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }

}