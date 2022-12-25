package com.udacity.project4.locationreminders.reminderslist

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Database
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutor = InstantTaskExecutorRule()

    private val exampleReminder = ReminderDTO("Title","Description","Location",(0..100).random().toDouble(),(0..100).random().toDouble())
    private lateinit var dataSource: FakeDataSource
    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        dataSource = FakeDataSource()
            Dispatchers.setMain(testDispatcher)
    }
    @ExperimentalCoroutinesApi
    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
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
        dataSource = FakeDataSource()
        val remindersListViewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(),dataSource)
        //when updating reminders
        remindersListViewModel.loadReminders()
        //Then returns error message in SnackBar
        assertThat(remindersListViewModel.showSnackBar.getOrAwaitValue(),`is`("No reminders found"))
    }
    @Test
    fun checkingLoading_whenSavingReminder() = runBlockingTest{
        val reminderListViewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(), dataSource)
        //Given a dataSource with some elements in it
        dataSource.saveReminder(exampleReminder)
        //when we add them and pause dispatcher in order to check for loading
        testDispatcher.pauseDispatcher()
        reminderListViewModel.loadReminders()
        assertThat(reminderListViewModel.showLoading.getOrAwaitValue(), `is`(true))
    }

    @Test
    fun returningError() = runBlockingTest {
        //Given a newly generated viewModel and a data source having an element
        val remindersListViewModel =
            RemindersListViewModel(ApplicationProvider.getApplicationContext(), dataSource)
        remindersListViewModel.loadReminders()
        //When data source is returning error
        dataSource.saveReminder(exampleReminder)
        dataSource.shouldReturnError(true)
        //Then the Livedata is still sized 0 with no update on the newly added element
        remindersListViewModel.loadReminders()
        assertThat(remindersListViewModel.empty.getOrAwaitValue(), `is`(true))
  }
}