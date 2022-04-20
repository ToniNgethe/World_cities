package com.toni.citiesoftheworld.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.toni.citiesoftheworld.R
import com.toni.citiesoftheworld.databinding.ActivityCitiesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CitiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCitiesBinding

    private val navController: NavController
        get() = Navigation.findNavController(this, R.id.nav_host_fragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener {
            val fragment = navController.currentDestination?.id
            if (fragment == R.id.citiesListFragment) {
                binding.floatingActionButton.setImageResource(R.drawable.ic_list)
                navController.navigate(R.id.action_citiesListFragment_to_citiesMapFragment)
            } else {
                binding.floatingActionButton.setImageResource(R.drawable.ic_location)
                navController.navigateUp()
            }
        }
    }


}