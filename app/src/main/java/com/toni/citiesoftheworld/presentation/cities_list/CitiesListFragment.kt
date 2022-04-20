package com.toni.citiesoftheworld.presentation.cities_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.toni.citiesoftheworld.databinding.FragmentCitiesListBinding
import com.toni.citiesoftheworld.presentation.CitiesViewModel
import com.toni.citiesoftheworld.presentation.adapter.CitiesAdapter
import com.toni.citiesoftheworld.presentation.adapter.CitiesLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CitiesListFragment : Fragment() {
    private lateinit var binding: FragmentCitiesListBinding

    private val viewModel: CitiesViewModel by viewModels()
    private lateinit var citiesAdapter: CitiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCitiesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    Toast.makeText(requireActivity(), "${it.error}", Toast.LENGTH_LONG).show()
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