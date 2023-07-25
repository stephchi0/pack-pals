package com.example.packpals.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.packpals.R
import com.example.packpals.databinding.ActivityNavigationDrawerViewBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NavigationDrawerViewActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavigationDrawerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationDrawerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarNavigationDrawerView.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf( R.id.mapFragment, R.id.itineraryPageFragment,
            R.id.expensesFragment, R.id.profilePageFragment, R.id.findAPalFragment, R.id.incomingPalRequestsFragment, R.id.albumFragment), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setNavigationViewListener(navView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation_drawer_view, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer_view)
        // Handle item selection
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer_view)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun setNavigationViewListener(navView: NavigationView) {
        navView.setNavigationItemSelectedListener(this)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer_view)
        Log.i("NavActivity", item.title as String)
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        if (item.title == "Return to Trip Menu") {
            finish()
        }
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}