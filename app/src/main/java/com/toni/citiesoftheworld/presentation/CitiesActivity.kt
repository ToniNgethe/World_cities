package com.toni.citiesoftheworld.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.toni.citiesoftheworld.databinding.ActivityCitiesBinding
import com.toni.citiesoftheworld.presentation.adapter.CitiesAdapter
import com.toni.citiesoftheworld.presentation.adapter.CitiesLoadStateAdapter
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
        initSearch()
    }

    private fun initSearch() {
        binding.etSearch.doOnTextChanged { text, start, before, count ->
            text?.let {
                if (text.length >= 2) {
                    viewModel.submitQuery(text.trim().toString())
                }
            } ?: kotlin.run {
                viewModel.submitQuery("")
            }
        }
    }

    private fun setUpAdapter() {
        citiesAdapter = CitiesAdapter()
        binding.rvCities.adapter = citiesAdapter.withLoadStateHeaderAndFooter(
            header = CitiesLoadStateAdapter(citiesAdapter),
            footer = CitiesLoadStateAdapter(citiesAdapter)
        )

        lifecycleScope.launchWhenResumed {
            citiesAdapter.loadStateFlow.collect { loadState ->
                // display progress bar
                binding.pbCities.apply {
                    visibility = if (loadState.mediator?.refresh is LoadState.Loading) View.VISIBLE
                    else View.GONE
                }

                // in case of an error
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error

                errorState?.let {
                    Toast.makeText(this@CitiesActivity, "${it.error}", Toast.LENGTH_LONG).show()
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