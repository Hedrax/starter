package com.udacity.project4.fake

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

class FakeDataSource(private var reminderDTOS: MutableList<ReminderDTO>? = mutableListOf()) :
    ReminderDataSource {
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        reminderDTOS?.let { return Result.Success(it) }
        return Result.Error("No reminders found")
    }
    override suspend fun saveReminder(reminder: ReminderDTO){
        reminderDTOS?.add(reminder)
    }
    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        reminderDTOS?.firstOrNull { it.id == id }?.let { return Result.Success(it) }
        return Result.Error("No reminders found")
    }
    override suspend fun deleteAllReminders() {
        reminderDTOS = mutableListOf()
    }
}