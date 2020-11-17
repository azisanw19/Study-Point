package id.canwar.studypoint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        navigationSetup()
    }

    private fun navigationSetup() {

        nav_view.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_profile -> TODO("klik nav profile")
                R.id.nav_item -> TODO("klik nav item")
                R.id.nav_logout -> TODO("klik nav logout")
            }

            drawer_layout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true

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

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }
}