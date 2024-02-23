package com.shivamgupta.newsapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    private val destinationChangedListener = NavController.OnDestinationChangedListener { _, destination, _ ->
        handleSearchIconVisibility(destinationId = destination.id)

        // Update bottom nav visibility based on destination
        binding.bottomNavView.isVisible = !(destination.id == R.id.searchNewsFragment || destination.id == R.id.newsDetailFragment)

        // Update `back-arrow` icon visibility for Saved News Screen
        if(destination.id == R.id.bookmarkNewsFragment || destination.id == R.id.searchNewsFragment){
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

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

        // Setup bottom navigation with nav graph
        binding.bottomNavView.setupWithNavController(navController)
    }

    private fun handleSearchIconVisibility(destinationId: Int) {
        binding.searchIcon.apply {
            isVisible = destinationId == R.id.newsFeedFragment
            setOnClickListener {
                navController.navigate(R.id.searchNewsFragment)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onStart() {
        super.onStart()
        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

    override fun onStop() {
        super.onStop()
        navController.removeOnDestinationChangedListener(destinationChangedListener)
    }
}