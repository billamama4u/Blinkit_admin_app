package com.tarun.admin_blinkit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tarun.admin_blinkit.ViewModel.Dataupload
import com.tarun.admin_blinkit.databinding.ActivityAdminPageMainBinding

class AdminPageMainActivity : AppCompatActivity() {
    private val viewmodel: Dataupload by viewModels()
    lateinit var binding: ActivityAdminPageMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAdminPageMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController = navHostFragment.navController

        // Setup bottom navigation view with nav controller
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bootombar)
        bottomNavigationView.setupWithNavController(navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView2)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}
