package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.CoreMatchers.`is`
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    @get:Rule
    var instantExecutor = InstantTaskExecutorRule()

    private val  exampleReminder = ReminderDataItem("Title","Description","Location",(0..100).random().toDouble(),(0..100).random().toDouble())
    lateinit var saveReminderViewModel: SaveReminderViewModel
    lateinit var dataSource: FakeDataSource

    @Test
    fun liveData_settingLiveDataVarsOfVm(){
        //Given a fresh viewModel
        dataSource = FakeDataSource()
        saveReminderViewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(),dataSource)
        //When setting Title, position LiveData Variables
        saveReminderViewModel.setPositionReminder(exampleReminder)
        //Then title value should've changed
        val title = saveReminderViewModel.title.getOrAwaitValue()
        assertThat(title,`is`(exampleReminder.title))
    }

    @Test
    fun errorTesting_whenSavingWithNullValues(){
        //Given a fresh viewModel
        dataSource = FakeDataSource()
        saveReminderViewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(),dataSource)
        //Given Null values
        assertThat(saveReminderViewModel.title.getOrAwaitValue().isNullOrBlank(),`is`(true))
        //Then It should print out error
        saveReminderViewModel.save()
        assertThat(saveReminderViewModel.showSnackBarInt.getOrAwaitValue() == R.string.missing_title , `is`(true))
    }
    @Test
    fun saving_savingToFakeDataBase() = runBlockingTest{
        //Given a fresh viewModel
        dataSource = FakeDataSource()
        saveReminderViewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(),dataSource)
        //When setting Title, position LiveData Variables
        saveReminderViewModel.setPositionReminder(exampleReminder)
        saveReminderViewModel.appendData()
        //Then dataSource should contain the values
        val reminders = dataSource.getReminders() as Result.Success
        assertThat(saveReminderViewModel.positionStr == null,`is`(false))
        assertThat(reminders.data.isEmpty(),`is`(false))
        assertThat(reminders.data[0].title,`is`(exampleReminder.title))
    }
}