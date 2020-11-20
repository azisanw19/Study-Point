package id.canwar.studypoint.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import id.canwar.studypoint.DashboardFragment
import id.canwar.studypoint.R
import id.canwar.studypoint.firebase.Authentication
import id.canwar.studypoint.firebase.Database
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

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

        val toggle = ActionBarDrawerToggle(
                this,
                drawer_layout,
                toolbar,
                R.string.navigation_open,
                R.string.navigation_close,
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun databaseGetName() {

        val uid = Authentication.getInstance().getUID()
        if (uid != null) {

            Log.d("data user uid", uid)
            Database.getInstance().getUser(uid) {
                Log.d("data user", "$it")
                supportActionBar?.title = it?.get("firstName")?.toString()
            }

        }
    }

    override fun onStart() {
        super.onStart()
        val firebaseUser = Authentication.getInstance().getCurrentUser()

        if (firebaseUser == null) {
            // Saat user tidak ada maka belum login pindah ke Loginactivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signOut() {

        Authentication.getInstance().signOut {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }
}