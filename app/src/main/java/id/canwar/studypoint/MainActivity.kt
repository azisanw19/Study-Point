package id.canwar.studypoint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nav_view.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_dashboard -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, DashboardFragment()).commit()
                    nav_view.setCheckedItem(R.id.nav_dashboard)
                    databaseGetName()
                }
                R.id.nav_profile -> this // TODO("klok nav profile")
                R.id.nav_item -> this// TODO("klik nav item")
                R.id.nav_logout -> signOut()
            }

            drawer_layout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true

        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, DashboardFragment()).commit()
            nav_view.setCheckedItem(R.id.nav_dashboard)
            databaseGetName()
        }

        navigation_icon.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    private fun databaseGetName() {

        val uid = firebaseAuth.uid
        if (uid != null) {

            db.collection("users")
                    .document(uid)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            Log.d("data", "${document.data}")
                            header_text_main.text = document.data?.get("firstName")?.toString() ?: ""
                        }
                    }
        }
    }

    override fun onStart() {
        super.onStart()
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser == null) {
            // Saat user tidak ada maka belum login pindah ke Loginactivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signOut() {

        firebaseAuth.signOut()
        if (firebaseAuth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }
}