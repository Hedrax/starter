package com.udacity.project4.locationreminders.savereminder

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.PendingIntent
import android.bluetooth.BluetoothClass.Device
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.databinding.FragmentSaveReminderBinding
import com.udacity.project4.locationreminders.geofence.GeofenceUtils
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import com.udacity.project4.locationreminders.savereminder.selectreminderlocation.SelectLocationFragment
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.util.*

class SaveReminderFragment : BaseFragment() {
    //Get the view model this time as a single to be shared with the another fragment
    override val _viewModel: SaveReminderViewModel by inject()
    private lateinit var geoBuilder: GeofenceUtils
    private lateinit var binding: FragmentSaveReminderBinding
    private var locationEnabled = false
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
            if(it) {
                //that when the flag is enabled
                _viewModel.getLocation()?.let { it1 ->
                    addGeofence(it1)
                }
            }
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
                .addOnSuccessListener{_viewModel.showToast.value = "Geofence Added"}
                //Toast and Todo navigation
                .addOnFailureListener{
                    Log.i(TAG, "Geofence Failed: $it")
                    _viewModel.showToast.value = "Give Location permission"
                }
        }
        catch (e:Exception){
            Log.i(TAG, "geofence")
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
                permissionHandling()
            }
            else{
                //showing error message when permission denied
                _viewModel.showSnackBarInt.value = R.string.permission_denied_explanation
            }
        }
        if (requestCode == REQUEST_BACKGROUND_LOCATION){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                permissionHandling()
            }
            else{
                //showing error message when permission denied
                _viewModel.showSnackBarInt.value = R.string.background_Error
            }
        }
    }
    //the fn checks for location if granted or not
    private fun isPermissionGranted(permission: String):Boolean {
        return (ActivityCompat.checkSelfPermission(
            requireContext(),
            permission) == PackageManager.PERMISSION_GRANTED)
    }
    //The function checks for the current location accessibility of the device if there is a failure tries to
    //asks user to open the location
    private fun checkDeviceLocationSettings() {
        Log.i(SelectLocationFragment.TAG,"Checking device location ")
        val locationRequest =
            LocationRequest.create()
                .apply {priority = LocationRequest.PRIORITY_LOW_POWER}
        val requestBuilder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val settingsResponse =
            settingsClient.checkLocationSettings(requestBuilder.build())
        //request handling to recheck permissions
        settingsResponse.addOnCompleteListener{
            locationEnabled = true
            permissionHandling()
        }
        settingsResponse.addOnFailureListener {
            _viewModel.showSnackBarInt.value = R.string.location_required_error
        }
    }
    //The main fn to handle the permissions requests before saving
    private fun permissionHandling():Boolean{
        var permission: String
        var requestCode: Int
        try{
            //checks for ForeGround permission
            permission = Manifest.permission.ACCESS_FINE_LOCATION
            requestCode = REQUEST_LOCATION_PERMISSION
            if (!isPermissionGranted(permission)) {
                this.requestPermissions(arrayOf(permission), requestCode)
                return false
            }
        }catch (e:Exception){
            Log.i(SelectLocationFragment.TAG,"${e.message}")
        }
        if(Build.VERSION.SDK_INT > 28) {
            try {
                permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION
                requestCode = REQUEST_BACKGROUND_LOCATION
                if (!isPermissionGranted(permission)) {
                    this.requestPermissions(arrayOf(permission), requestCode)
                    return false
                }
            } catch (e: Exception) {
                Log.i(SelectLocationFragment.TAG, "${e.message}")
            }
        }
        try {
            if (!locationEnabled)
            {
                checkDeviceLocationSettings()
                return false
            }
        }catch (e:Exception){
            Log.i(SelectLocationFragment.TAG,"${e.message}")
        }
        return true
    }

    companion object {
        const val TAG = "SaveReminderFrag"
        const val REQUEST_BACKGROUND_LOCATION = 113
        const val REQUEST_LOCATION_PERMISSION = 112
    }
}
