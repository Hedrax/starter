package com.udacity.project4.locationreminders.data.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.fake.FakeDataSource
import com.udacity.project4.fake.FakeReminderDAO
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    private val reminder1 = ReminderDTO("Title1", "Description1","Location 1", (0..360).random().toDouble(),(0..360).random().toDouble())
    private val reminder2 = ReminderDTO("Title2", "Description2","Location 2", (0..360).random().toDouble(),(0..360).random().toDouble())
    private val reminder3 = ReminderDTO("Title3", "Description3","Location 3", (0..360).random().toDouble(),(0..360).random().toDouble())
    private val reminderNew = ReminderDTO("TitleNew", "DescriptionNew","Location New", (0..360).random().toDouble(),(0..360).random().toDouble())
    private val localReminders = listOf(reminder1,reminder2,reminder3).sortedBy { it.id }
    private val newReminders = listOf(reminder1,reminder2,reminder3,reminderNew).sortedBy { it.id }

    private lateinit var remindersLocalDataSource: FakeDataSource
    private lateinit var remindersRepository: RemindersLocalRepository
    private lateinit var reminderDao: FakeReminderDAO
    @Before
    fun repoCreation(){
        remindersLocalDataSource = FakeDataSource(localReminders.toMutableList())
        reminderDao = FakeReminderDAO()
        remindersRepository = RemindersLocalRepository(
            reminderDao,
            Dispatchers.Unconfined)
    }
    @Test
    fun getReminder_requestsAllRemindersFromLocalDataSource() = runBlockingTest {
        // When we get the reminders from the Repo
        val reminders = remindersRepository.getReminders() as Result.Success
        // Then tasks are loaded from the ReminderDAO
        assertThat(reminders.data, IsEqual(reminderDao.getReminders()))
    }

    @Test
    fun saveReminders_savingNewReminder() = runBlockingTest {
        //When save new reminder to Repo
        remindersRepository.saveReminder(reminderNew)
        val reminders = remindersRepository.getReminders() as Result.Success
        //Then it should equals the original + the new added reminder
        assertThat(reminders.data, IsEqual(newReminders))
    }

    @Test
    fun getReminderByID() = runBlockingTest {
        //When passing the Id of the first element
        val reminder = remindersRepository.getReminder(localReminders[0].id) as Result.Success
        //Then it should equals the element that we passed in ID
        assertThat(reminder.data, IsEqual(localReminders[0]))
    }
    @Test
    fun deleteReminders_deletingAllReminders() = runBlockingTest {
        //When getting all reminders
        val reminders = remindersRepository.getReminders() as Result.Success
        //Given that the list isn't empty
        assertThat(reminders.data.isEmpty(), IsEqual(false))
        remindersRepository.deleteAllReminders()
        val remindersUpdated = remindersRepository.getReminders() as Result.Success
        //Then the size equals 0
        assertThat(remindersUpdated.data.size, IsEqual(0))
    }
}
