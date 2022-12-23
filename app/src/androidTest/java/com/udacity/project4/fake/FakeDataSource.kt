package com.udacity.project4.fake

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

class FakeDataSource(private var reminderDTOS: MutableList<ReminderDTO>? = mutableListOf()) :
    ReminderDataSource {

}