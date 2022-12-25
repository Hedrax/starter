package com.udacity.project4.fake

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import net.bytebuddy.asm.Advice.Return

class FakeDataSource(private var reminderDTOS: MutableList<ReminderDTO> = mutableListOf()) :
    ReminderDataSource {
    private var shouldReturnError = false
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if(shouldReturnError)return Result.Error("test Exception")
        reminderDTOS.let { return Result.Success(it) }
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminderDTOS.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if(shouldReturnError)return Result.Error("test Exception")
        reminderDTOS.firstOrNull(){it.id == id}?.let{return Result.Success(it) }
        return Result.Error("No reminders found")
    }

    override suspend fun deleteAllReminders() {
        reminderDTOS = mutableListOf()
    }
    fun shouldReturnError(value:Boolean){
        shouldReturnError = value
    }

}