package com.udacity.project4.fake

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.RemindersDao

class FakeReminderDAO : RemindersDao {
    val serviceData: LinkedHashMap<String, ReminderDTO> = LinkedHashMap()

    override suspend fun getReminders(): List<ReminderDTO> {
        val lst = mutableListOf<ReminderDTO>()
        lst.addAll(serviceData.values)
        return lst
    }

    override suspend fun getReminderById(reminderId: String): ReminderDTO? {
        serviceData[reminderId]?.let {return it}
        return null
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        serviceData[reminder.id] = reminder
    }

    override suspend fun deleteAllReminders() {
        TODO("Not yet implemented")
    }

}
