package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource (private var reminderDTOS: MutableList<ReminderDTO> = mutableListOf()) :
    ReminderDataSource {
    private var shouldReturnError = false
    fun shouldReturnError(value:Boolean){
        shouldReturnError = value
    }
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if(shouldReturnError)return Result.Error("test Exception")
        reminderDTOS.let { return Result.Success(it) }
    }
    override suspend fun saveReminder(reminder: ReminderDTO){
        reminderDTOS.add(reminder)
    }
    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if(shouldReturnError)return Result.Error("test Exception")
        reminderDTOS.firstOrNull { it.id == id }?.let { return Result.Success(it) }
        return Result.Error("No reminders found")
    }
    override suspend fun deleteAllReminders() {
        reminderDTOS = mutableListOf()
    }

}