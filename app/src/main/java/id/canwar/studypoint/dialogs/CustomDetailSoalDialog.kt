package id.canwar.studypoint.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import id.canwar.studypoint.R

class CustomDetailSoalDialog(val activity: Activity, callback: (dialog: AlertDialog, view: View) -> Unit) {

    private var dialog: AlertDialog
    private var view = activity.layoutInflater.inflate(R.layout.dialog_details_soal, null) as ViewGroup

    init {
        view.apply {

            dialog = AlertDialog.Builder(activity, R.style.DialogCustom)
                .setView(view)
                .create()
            dialog.show()

            callback(dialog, view)
        }
    }

}