package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    @get:Rule
    var instantExecutor = InstantTaskExecutorRule()

    val exampleReminder = ReminderDTO("Title","Description","Location",(0..100).random().toDouble(),(0..100).random().toDouble())
    lateinit var dataSource: FakeDataSource
    @Before
    fun setup(){
        dataSource = FakeDataSource()
    }
    @Test
    fun liveData_testingSaveFn() = runBlockingTest {
        //Given a newly generated viewModel
        val remindersListViewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(),dataSource)
        //When adding new reminder
        dataSource.saveReminder(exampleReminder)
        remindersListViewModel.loadReminders()
        val reminders = remindersListViewModel.remindersList.getOrAwaitValue()
        //Then the newReminder is triggered
        assertThat(reminders.isEmpty(),`is`(false))
    }
    @Test
    fun errorMessage_WhenNullDataSource() = runBlockingTest {
        //Given a newly generated viewModel and an empty dataSource
        dataSource = FakeDataSource(null)
        val remindersListViewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(),dataSource)
        //when updating reminders
        remindersListViewModel.loadReminders()
        //Then returns error message in SnackBar
        assertThat(remindersListViewModel.showSnackBar.getOrAwaitValue(),`is`("No reminders found"))
    }
}