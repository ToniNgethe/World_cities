package com.toni.citiesoftheworld.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.toni.citiesoftheworld.databinding.ItemCityBinding
import com.toni.citiesoftheworld.domain.model.City

class CitiesAdapter : PagingDataAdapter<City, CitiesAdapter.CitiesVH>(CitiesDiffUtil()) {

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

    class CitiesVH(var binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(city: City?) {
            binding.apply {
                tvCityName.text = city?.name
                tvCountryName.text = city?.countryName
            }
        }
    }
}