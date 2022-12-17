package com.udacity.project4.locationreminders.savereminder.selectreminderlocation


import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.databinding.FragmentSelectLocationBinding
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import org.koin.android.ext.android.inject

class SelectLocationFragment : BaseFragment(),OnMapReadyCallback {

    //Use Koin to get the view model of the SaveReminder
    override val _viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentSelectLocationBinding

    private var reminderLocation:Marker? = null
    private lateinit var map: GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_select_location, container, false)

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(true)

        //the Setup of the map
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // the one click listener to the save button will check if user selected location or not
        binding.saveButton.setOnClickListener {
            if(reminderLocation != null){
                _viewModel.setPosition(reminderLocation!!)
                findNavController().navigateUp()
            }
            else{
                _viewModel.showToast.value = getString(R.string.missing_location)
            }
        }
//        TODO: zoom to the user location after taking his permission

//        TODO: call this function after the user confirms on the selected location

        return binding.root
    }

    //the fn handles what to be done when adding marker and update the location selected variable to
    //be added to the save reminder fragment
    private fun setPoiClk(map: GoogleMap) {
        try {
            map.setOnPoiClickListener { poi ->
                val poiMarker =
                    map.addMarker(MarkerOptions().position(poi.latLng).title(poi.name))
                poiMarker.showInfoWindow()
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(poi.latLng, 15F))
                reminderLocation?.remove()
                reminderLocation = poiMarker
            }
        }catch (e:Exception){
            Log.i(TAG, "set POI click")
        }
    }

    //responsible for styling the current map with styling in the JSON file Map_styling.json
    private fun mapStyling(map: GoogleMap) {
        try {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context,R.raw.map_styling))
        }
        catch (e:Exception){
            Log.i(TAG, "set map style = ${e.message}")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }


    //The function checks for the current location accessibility of the device if there is a failure tries to
    //asks user to open the location
    private fun checkDeviceLocationSettings() {
        Log.i(TAG,"Checking device location ")
        val locationRequest =
            LocationRequest.create()
                .apply {priority = LocationRequest.PRIORITY_LOW_POWER}
        val requestBuilder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val settingsResponse =
            settingsClient.checkLocationSettings(requestBuilder.build())
        settingsResponse.addOnFailureListener {
               _viewModel.showSnackBarInt.value = R.string.location_required_error
        }
    }
 //permissions
 private fun isPermissionGranted() : Boolean {
     return ContextCompat.checkSelfPermission(
         requireContext(),
         Manifest.permission.ACCESS_FINE_LOCATION
     ) == PackageManager.PERMISSION_GRANTED
 }
//requests location ForeGround Permission
    private fun requestForeGroundLocationPermission(){
        try {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }catch (e:Exception){
            Log.i(TAG,"RequestForeground permission ")
        }
    }
    //checks the API level and  based on that checks for background permissions for geofence
    @TargetApi(29)
    private fun requestBackgroundLocationPermissions() {
        Log.i(TAG,"Request ")
        ActivityCompat.requestPermissions(
            this.activity!!,
            arrayOf<String>(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
            REQUEST_BACKGROUND_LOCATION
        )
        Log.d(TAG, "BackGround access")
    }
    companion object{
        const val TAG = "Select_Fragment"
        const val REQUEST_LOCATION_PERMISSION = 112
        const val REQUEST_BACKGROUND_LOCATION = 113
    }

}
