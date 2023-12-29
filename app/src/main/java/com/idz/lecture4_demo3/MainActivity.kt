package com.idz.lecture4_demo3

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {
    var navCtrl: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHost = supportFragmentManager.findFragmentById(R.id.mainNavHost) as NavHost?
        navCtrl = navHost!!.navController
        setupActionBarWithNavController(this, navCtrl!!)

        //if (Model.instance().isSignedIn()) {
//        if (true) {
//                navCtrl.navigate(R.id.action_loginFragment_to_moviesFragment2);
//            }
//        }
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