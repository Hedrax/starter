package com.udacity.project4.locationreminders.savereminder.selectreminderlocation


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.databinding.FragmentSelectLocationBinding
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import org.koin.android.ext.android.inject

class SelectLocationFragment : BaseFragment(),OnMapReadyCallback {

    private val latLngHome = LatLng(30.06485678718907, 31.218080233756442)
    //variable to indicate whether we have access or not
    private var locationPermissionGranted = false
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
        //requesting permission
        permissionHandling()
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

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
        map = googleMap
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngHome,7F))
        setPoiClk(map)
        setOnClick(map)
        mapStyling(map)
        }catch (e:Exception){
            Log.i(TAG,"${e.message}")
        }
    }
    //the fn handles what to be done when adding marker and update the location selected variable to
    //be added to the save reminder fragment
    private fun setPoiClk(map: GoogleMap) {
        try {
            map.setOnPoiClickListener { poi ->
                val poiMarker =
                    map.addMarker(MarkerOptions().position(poi.latLng).title(poi.name))
                poiMarker.showInfoWindow()
                reminderLocation?.remove()
                reminderLocation = poiMarker
            }
        }catch (e:Exception){
            Log.i(TAG, "set POI click")
        }
    }
    private fun setOnClick(map: GoogleMap){
        map.setOnMapClickListener {
            val marker =
                map.addMarker(MarkerOptions().position(it).title("Custom Location"))
            marker.hideInfoWindow()
            reminderLocation?.remove()
            reminderLocation = marker
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
    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        try {
            if (locationPermissionGranted) {
                map.isMyLocationEnabled = true
                map.uiSettings?.isMyLocationButtonEnabled = true
                checkDeviceLocationSettings()
            } else {
                map.isMyLocationEnabled = false
                map.uiSettings?.isMyLocationButtonEnabled = false
                permissionHandling()
            }
        } catch (e: SecurityException) {
            Log.i("Exception: %s", e.message, e)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //handling the location permission when requested
        if (requestCode == REQUEST_LOCATION_PERMISSION){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                locationPermissionGranted = true
                updateLocationUI()
            }
            else{
                //showing error message when permission denied
                _viewModel.showSnackBarInt.value = R.string.permission_denied_explanation
            }
        }

    }

    private fun permissionHandling(permission: String = Manifest.permission.ACCESS_FINE_LOCATION
                                   , requestCode: Int = REQUEST_LOCATION_PERMISSION){
        try{
            //checks for permission, if not request it
            if (isPermissionGranted()) {
                // Update Location UI
                locationPermissionGranted = true
                updateLocationUI()
            }else{
                //Request permission
                this.requestPermissions(arrayOf(permission), requestCode)
            }
        }catch (e:Exception){
            Log.i(TAG,"${e.message}")
        }
    }

    //the fn checks for location if granted or not
 private fun isPermissionGranted(
     permission: String = Manifest.permission.ACCESS_FINE_LOCATION):Boolean {
     return (ActivityCompat.checkSelfPermission(
         requireContext(),
         permission) == PackageManager.PERMISSION_GRANTED)
 }
    companion object{
        const val TAG = "Select_Fragment"
        const val REQUEST_LOCATION_PERMISSION = 112
    }

}
