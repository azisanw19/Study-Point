package id.canwar.studypoint.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import id.canwar.studypoint.R
import id.canwar.studypoint.firebase.Authentication
import id.canwar.studypoint.firebase.Database
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.lang.Exception

class ProfileFragment : Fragment() {

    private lateinit var view: ViewGroup
    private val database = Database.getInstance()
    private val authentication = Authentication.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view = inflater.inflate(R.layout.fragment_profile, container, false) as ViewGroup

        val uid = authentication.getUID()
        database.getUser(uid!!) {

            initUI(it)

        }

        return view


    }

    private fun initUI(dataUser: Map<String, Any>?) {

        val image = dataUser?.get("image")?.toString()
        val tugasDikerjakan = dataUser?.get("tugasDikerjakan")?.toString() ?: "0"
        val point = dataUser?.get("point")?.toString() ?: "0"
        val email = dataUser?.get("email").toString()
        val firstName = dataUser?.get("firstName").toString()
        val lastName = dataUser?.get("lastName").toString()
        val birthDay = dataUser?.get("birthday").toString()

        try {
            Picasso.get().load(image).into(view.image_profile)
        } catch (e: Exception) {
            Log.e("Picasso failed", e.toString())
        }

        view.profile_tugas_dikerjakan.text = tugasDikerjakan
        view.profile_point.text = point
        view.profile_email.apply {
            setText(email)
            isClickable = false
            isEnabled = false
        }
        view.profile_first_name.setText(firstName)
        view.profile_last_name.setText(lastName)
        view.profile_birthday.setText(birthDay)

        view.profile_ubah_data.setOnClickListener {

            val uid = authentication.getUID()

            val firstName = view.profile_first_name.text.toString()
            val lastName = view.profile_last_name.text.toString()
            val birthDay = view.profile_birthday.text.toString()

            val dataUser = mapOf<String, Any>(
                "firstName" to firstName,
                "lastName" to lastName,
                "birthday" to birthDay
            )

            database.updateUser(uid!!, dataUser) {
                Toast.makeText(context, "Berhasil update data profile", Toast.LENGTH_SHORT).show()
            }
        }

    }

}