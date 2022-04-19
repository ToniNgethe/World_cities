package com.toni.citiesoftheworld.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.toni.citiesoftheworld.R
import com.toni.citiesoftheworld.databinding.ItemCityBinding
import com.toni.citiesoftheworld.databinding.NetworkStateItemBinding
import com.toni.citiesoftheworld.domain.model.City

class CitiesAdapter : PagingDataAdapter<City, CitiesVH>(CitiesDiffUtil()) {

    class CitiesDiffUtil : DiffUtil.ItemCallback<City>() {
        override fun areItemsTheSame(
            oldItem: City, newItem: City
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: City, newItem: City
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesVH =
        CitiesVH(ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CitiesVH, position: Int) {
        holder.bind(getItem(position))
    }
}

class CitiesVH(var binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(city: City?) {
        binding.apply {
            tvCityName.text = city?.name
            tvCountryName.text = city?.countryName
        }
    }
}

class CitiesLoadStateAdapter(
    private val adapter: CitiesAdapter
) : LoadStateAdapter<NetworkStateItemViewHolder>() {
    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) {
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ): NetworkStateItemViewHolder {
        return NetworkStateItemViewHolder(parent) { adapter.retry() }
    }
}


class NetworkStateItemViewHolder(
    parent: ViewGroup, private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.network_state_item, parent, false)
) {
    private val binding = NetworkStateItemBinding.bind(itemView)
    private val progressBar = binding.progressBar
    private val errorMsg = binding.errorMsg
    private val retry = binding.retryButton.also {
            it.setOnClickListener { retryCallback() }
        }

    fun bindTo(loadState: LoadState) {
        progressBar.isVisible = loadState is LoadState.Loading
        retry.isVisible = loadState is LoadState.Error
        errorMsg.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
        errorMsg.text = (loadState as? LoadState.Error)?.error?.message
    }
}
