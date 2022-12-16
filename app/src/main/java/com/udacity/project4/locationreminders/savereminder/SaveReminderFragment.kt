package com.udacity.project4.locationreminders.savereminder

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.databinding.FragmentSaveReminderBinding
import com.udacity.project4.locationreminders.geofence.GeofenceUtils
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import org.koin.android.ext.android.inject
import java.util.*

class SaveReminderFragment : BaseFragment() {
    //Get the view model this time as a single to be shared with the another fragment
    override val _viewModel: SaveReminderViewModel by inject()

    private lateinit var geoBuilder: GeofenceUtils
    private lateinit var binding: FragmentSaveReminderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_save_reminder, container, false)

        setDisplayHomeAsUpEnabled(true)

        binding.viewModel = _viewModel

        _viewModel.navigate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) findNavController().navigateUp()
        })
        geoBuilder = GeofenceUtils(context)
        //observer on saveFlag liveData when we're saving the data he will cause adding geofence
        _viewModel.saveFlag.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it) _viewModel.getLocation()?.let { it1 -> addGeofence(it1)}

        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this

    }

    override fun onDestroy() {
        super.onDestroy()
        //make sure to clear the view model after destroy, as it's a single view model.
        _viewModel.onClear()
    }
    //fn that adds the geofence it gives a log and toast in case of failure
    @SuppressLint("MissingPermission")
    private fun addGeofence(latLng: LatLng,radius: Float = 200f) {
        try{
            val geoId: String = UUID.randomUUID().toString()
            val geofence: Geofence = geoBuilder.buildGeofence(geoId,latLng,radius,
                Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            val pendingIntent: PendingIntent? = geoBuilder.getPendingIntent()
            val geofenceRequest: GeofencingRequest = geoBuilder.getGeofencingRequest(geofence)
            LocationServices.getGeofencingClient(context!!)
                .addGeofences(geofenceRequest, pendingIntent)
                .addOnSuccessListener{_viewModel.showToast.value = "GeoFence added"}
                .addOnFailureListener{
                    Log.d(TAG, "Geofence Failed: ${it.message}")
                    _viewModel.showToast.value = "Give Location permission"
                }
        }
        catch (e:Exception){
            Log.i(TAG, "geofence")
        }
    }
    companion object {
        const val TAG = "SaveReminderFrag"
    }
}
