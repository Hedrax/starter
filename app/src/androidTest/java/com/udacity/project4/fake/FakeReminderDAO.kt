package com.udacity.project4.fake

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.RemindersDao

class FakeReminderDAO : RemindersDao {
    val serviceData: LinkedHashMap<String, ReminderDTO> = LinkedHashMap()

    override suspend fun getReminders(): List<ReminderDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getReminderById(reminderId: String): ReminderDTO? {
        TODO("Not yet implemented")
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllReminders() {
        TODO("Not yet implemented")
    }

}
