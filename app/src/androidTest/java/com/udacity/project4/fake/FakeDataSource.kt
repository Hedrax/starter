package com.udacity.project4.fake

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

class FakeDataSource(private var reminderDTOS: MutableList<ReminderDTO>? = mutableListOf()) :
    ReminderDataSource {
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllReminders() {
        TODO("Not yet implemented")
    }

}