package com.shivamgupta.newsapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.shivamgupta.newsapp.R
import com.shivamgupta.newsapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavGraph()

    }

    private fun setupNavGraph() {
        setSupportActionBar(binding.materialToolbar)
        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupActionBarWithNavController(
            this,
            navController,
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            handleSearchIconVisibility(destinationId = destination.id)
            binding.bottomNavView.isVisible = destination.id != R.id.newsSearchFragment
        }

        // Setup bottom navigation with nav graph
        binding.bottomNavView.setupWithNavController(navController)
    }

    private fun handleSearchIconVisibility(destinationId: Int) {
        binding.searchButton.apply {
            isVisible = destinationId == R.id.newsFeedFragment
            setOnClickListener {
                navController.navigate(R.id.newsSearchFragment)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}