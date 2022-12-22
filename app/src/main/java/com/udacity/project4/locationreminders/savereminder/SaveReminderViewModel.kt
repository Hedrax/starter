package com.udacity.project4.locationreminders.savereminder

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.udacity.project4.R
import com.udacity.project4.base.BaseViewModel
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.launch

class SaveReminderViewModel(val app: Application, val dataSource: ReminderDataSource) :
    BaseViewModel(app) {
    private val selectedPosition = MutableLiveData<Marker?>(null)
    val title = MutableLiveData<String?>(null)
    val description = MutableLiveData<String>()
    val positionStr: String
        get() = selectedPosition.value?.title.toString()
    val saveFlag = MutableLiveData<Boolean>()
    private val _navigate = MutableLiveData<Boolean>(false)
    val navigate: LiveData<Boolean>
        get() = _navigate

    /**
     * Clear the live data objects to start fresh next time the view model gets called
     */
    fun onClear() {
        selectedPosition.value = null
        title.value = null
        saveFlag.value = false
        _navigate.value = false
    }
    init{
        title.value = null
        description.value = ""
        saveFlag.value = false
        selectedPosition.value = null
    }
    fun save() {
        showLoading.value = true
        if (checkValidity()) {
            appendData()
            saveFlag
            _navigate.value = true
            onClear()
        }
    }
    //fn to receive the incoming selected position from location selection fragment
    fun setPosition(value: Marker) {
        selectedPosition.value = value
    }
    //simple setter fn for easy translation from ReminderDTO to position
    fun setPositionReminder(value: ReminderDataItem) {
        title.value = value.title

    }
    //check if the title is not null and a position has been selected
    private fun checkValidity(): Boolean {
        if ((title.value == null) || (title.value == "")) {
            showSnackBarInt.value = R.string.missing_title
            return false
        }
        if (selectedPosition.value == null) {
            showSnackBarInt.value = R.string.missing_location
            return false
        }
        return true
    }
    //saving to dataBase process
    fun appendData() {
        showLoading.value = true
        viewModelScope.launch {
            dataSource.saveReminder(ReminderDTO(
                title = title.value,
                location = positionStr,
                description = description.value,
                latitude = selectedPosition.value?.position?.latitude,
                longitude = selectedPosition.value?.position?.longitude
            ))
        }
        showLoading.value = false
        navigationCommand.value = NavigationCommand.Back
    }
    //navigation fn
    fun navigateToSelectLocation() {
        navigationCommand.value = NavigationCommand
            .To(SaveReminderFragmentDirections.actionSaveReminderFragmentToSelectLocationFragment())
    }

    fun getLocation() : LatLng? {
        return selectedPosition.value?.position?.let {
            LatLng(
                it.latitude,
                it.longitude)
        }
    }
}