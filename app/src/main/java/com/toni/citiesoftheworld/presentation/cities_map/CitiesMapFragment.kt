package com.toni.citiesoftheworld.presentation.cities_map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.toni.citiesoftheworld.R
import com.toni.citiesoftheworld.databinding.FragmentCitiesMapBinding

class CitiesMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentCitiesMapBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCitiesMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.setOnMapClickListener {
            moveCameraToLocation(it)
        }
        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLocation = LatLng(location.latitude, location.longitude)
                moveCameraToLocation(currentLocation)
            } ?: kotlin.run {
                Snackbar.make(
                    binding.root,
                    getString(R.string.unable_to_get_your_location_error),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun moveCameraToLocation(latLng: LatLng) {
        googleMap.clear()
        googleMap.addMarker(
            MarkerOptions().position(latLng).title("Current location")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6f))
    }
}