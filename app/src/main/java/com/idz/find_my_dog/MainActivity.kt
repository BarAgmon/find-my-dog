package com.idz.find_my_dog

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    var navCtrl: NavController? = null
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Make navbar invisible until user logged in
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.GONE
        val navHost = supportFragmentManager.findFragmentById(R.id.navHostMain) as NavHost?
        navCtrl = navHost!!.navController
        setupActionBarWithNavController(this, navCtrl!!)

        navCtrl!!.addOnDestinationChangedListener { _, destination, _ ->
            // Show BottomNavigationView only in certain destinations
            when (destination.id) {
                R.id.loginFragment, R.id.registrationFragment -> bottomNavigationView.visibility = View.GONE
                else -> bottomNavigationView.visibility = View.VISIBLE
            }
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_all_posts -> {
                    val bundle = Bundle().apply {
                        putBoolean("isUserPostsOnly", false)
                    }
                    navCtrl?.navigate(R.id.action_global_postsAction, bundle)
                    true
                }
                R.id.navigation_my_posts -> {
                    val bundle = Bundle().apply {
                        putBoolean("isUserPostsOnly", true)
                    }
                    navCtrl?.navigate(R.id.action_global_postsAction, bundle)
                    true
                }
                R.id.navigation_my_profile -> {
                    navCtrl?.navigate(R.id.action_global_userProfileAction)
                    true
                }

                else -> false
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.bottom_navigation_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!super.onOptionsItemSelected(item)) {
            when (item.itemId) {
                android.R.id.home -> navCtrl!!.navigateUp()
                else -> onNavDestinationSelected(item, navCtrl!!)
            }
        } else {
            return true
        }
        return false
    }
}