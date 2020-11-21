package id.canwar.studypoint.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import id.canwar.studypoint.R

class CustomBuatSoalDialog(val activitY: Activity, callback: (dialog: AlertDialog, view: View) -> Unit) {

    private var dialog: AlertDialog
    private var view = activitY.layoutInflater.inflate(R.layout.dialog_buat_soal, null) as ViewGroup

    init {
        view.apply {

            dialog = AlertDialog.Builder(activitY, R.style.DialogCustom)
                    .setView(view)
                    .create()
            dialog.show()

            callback(dialog, view)
        }
    }


}