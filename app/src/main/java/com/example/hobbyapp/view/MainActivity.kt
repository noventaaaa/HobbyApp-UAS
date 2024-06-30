package com.example.hobbyapp.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.hobbyapp.R
import com.example.hobbyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = (supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment).navController
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.navView,navController)
        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{_, destination,_ ->
            if (destination.id == R.id.loginFragment || destination.id == R.id.registerFragment) {
                supportActionBar?.setDisplayHomeAsUpEnabled(false) // Hide back button
                supportActionBar?.setDisplayShowHomeEnabled(false) // Hide app icon
                supportActionBar?.setDisplayShowTitleEnabled(false) // Hide title
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                binding.bottomNav.visibility = View.GONE
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(true) // Show back button
                supportActionBar?.setDisplayShowHomeEnabled(true) // Show app icon
                supportActionBar?.setDisplayShowTitleEnabled(true) // Show title
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                binding.bottomNav.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
                || super.onSupportNavigateUp()
    }

}