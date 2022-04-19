package com.toni.citiesoftheworld.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.toni.citiesoftheworld.databinding.ActivityCitiesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CitiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCitiesBinding

    private val viewModel: CitiesViewModel by viewModels()
    private lateinit var citiesAdapter: CitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAdapter()
    }

    private fun setUpAdapter() {
        citiesAdapter = CitiesAdapter()
        binding.rvCities.adapter = citiesAdapter.withLoadStateHeaderAndFooter(
            header = CitiesLoadStateAdapter(citiesAdapter),
            footer = CitiesLoadStateAdapter(citiesAdapter)
        )
        lifecycleScope.launchWhenResumed {
            citiesAdapter.loadStateFlow.collect { loadStatus ->
                binding.pbCities.apply {
                    visibility = if (loadStatus.mediator?.refresh is LoadState.Loading) View.VISIBLE
                    else View.GONE
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.fetchCities().collect {
                citiesAdapter.submitData(it)
            }
        }
    }
}