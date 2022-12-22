package com.udacity.project4.locationreminders.savereminder

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.CoreMatchers.`is`
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matcher
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
}