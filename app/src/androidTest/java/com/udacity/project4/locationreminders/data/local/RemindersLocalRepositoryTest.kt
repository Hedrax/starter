package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth
import com.udacity.project4.fake.FakeReminderDAO
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.rules.MainCoroutineRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val reminderDTOS =  listOf<ReminderDTO>(
        ReminderDTO("title1", "description","location",(-360..360).random().toDouble(),(-360..360).random().toDouble()),
        ReminderDTO("title2", "description","location",(-360..360).random().toDouble(),(-360..360).random().toDouble()),
        ReminderDTO("title3", "description","location",(-360..360).random().toDouble(),(-360..360).random().toDouble())
    )
    private val reminderDTO1 = reminderDTOS[0]
    private val reminderDTO2 = reminderDTOS[1]
    private val newReminderDTO = reminderDTOS[2]
    private lateinit var remindersDao: FakeReminderDAO
    private lateinit var localRepository: RemindersLocalRepository

    @Before
    fun setup() {
        remindersDao = FakeReminderDAO()
        localRepository = RemindersLocalRepository(remindersDao, Dispatchers.Unconfined)
    }
    @Test
    fun savesToCache() = runBlockingTest {
        //GIVEN an empty Reminder DAO and new item for the test
        val reminderDTOList = mutableListOf<ReminderDTO>()

        //WHEN adding the new item to local repo
        localRepository.saveReminder(newReminderDTO)
        reminderDTOList.addAll(remindersDao.serviceData.values)
        //THEN remindersDAO and local repo should contain new added item
        Truth.assertThat(reminderDTOList).contains(newReminderDTO)
        val result = localRepository.getReminders() as? Result.Success
        Truth.assertThat(result?.data).contains(newReminderDTO)
    }
    @Test
    fun emptyListFetchedFromLocalCache() =
        runBlockingTest {
            //GIVEN an empty local repo and adding some items
            Truth.assertThat((localRepository.getReminders() as? Result.Success)?.data).isEmpty()
            remindersDao.serviceData[reminderDTO1.id] = reminderDTO1
            remindersDao.serviceData[reminderDTO2.id] = reminderDTO2
            Truth.assertThat((localRepository.getReminders() as? Result.Success)?.data).isNotEmpty()
            //WHEN Deleting all items
            localRepository.deleteAllReminders()
            //THEN it should return empty list
            Truth.assertThat((localRepository.getReminders() as? Result.Success)?.data).isEmpty()
        }
    @Test
    fun byIdThatDoesNotExistInLocalCache() =
        runBlockingTest {
            val msg = (localRepository.getReminder(reminderDTO1.id) as? Result.Error)?.message
            Assert.assertThat<String>(msg, CoreMatchers.notNullValue())
            Truth.assertThat(msg).isEqualTo("Reminder does not exist !")
        }
    @Test
    fun byIdThatExistsInLocalCache() = runBlockingTest {
        //GIVEN a local repo that doesn't contain the new item
        Truth.assertThat((localRepository.getReminder(reminderDTO1.id) as? Result.Error)?.message).isEqualTo(
            "Reminder not found!")
        //WHEN Modifying the map list items to equal something
        remindersDao.serviceData[reminderDTO1.id] = reminderDTO1
        //THEN the local repository should include that item we added
        val reminder = (localRepository.getReminder(reminderDTO1.id) as? Result.Success)?.data
        Assert.assertThat<ReminderDTO>(reminder as ReminderDTO, CoreMatchers.notNullValue())
        Assert.assertThat(reminder.id, CoreMatchers.`is`(reminderDTO1.id))
        Assert.assertThat(reminder.title, CoreMatchers.`is`(reminderDTO1.title))
        Assert.assertThat(reminder.location, CoreMatchers.`is`(reminderDTO1.location))
        Assert.assertThat(reminder.latitude, CoreMatchers.`is`(reminderDTO1.latitude))
        Assert.assertThat(reminder.longitude, CoreMatchers.`is`(reminderDTO1.longitude))
        Assert.assertThat(reminder.description, CoreMatchers.`is`(reminderDTO1.description))
    }
}