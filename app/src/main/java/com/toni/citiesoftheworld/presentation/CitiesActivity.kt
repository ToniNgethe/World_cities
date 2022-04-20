package com.toni.citiesoftheworld.presentation

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.toni.citiesoftheworld.R
import com.toni.citiesoftheworld.databinding.ActivityCitiesBinding
import com.toni.citiesoftheworld.presentation.adapter.CitiesAdapter
import com.toni.citiesoftheworld.presentation.adapter.CitiesLoadStateAdapter
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
                checkAskPermission {
                    binding.floatingActionButton.setImageResource(R.drawable.ic_list)
                    navController.navigate(R.id.action_citiesListFragment_to_citiesMapFragment)
                }
            } else {
                binding.floatingActionButton.setImageResource(R.drawable.ic_location)
                navController.navigateUp()
            }
        }
    }

    private fun checkAskPermission(permissionCheckCb: () -> Unit) {
        Dexter.withContext(this).withPermissions(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report?.areAllPermissionsGranted() == true) {
                    permissionCheckCb.invoke()
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.permission_error),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?, p1: PermissionToken?
            ) {
            }
        }).check()
    }

}