package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import org.junit.runner.RunWith;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.*
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get:Rule
    var instantExecutor = InstantTaskExecutorRule()

    private val reminder1 = ReminderDTO("Title1", "Description1","Location 1", (0..360).random().toDouble(),(0..360).random().toDouble())
    private val reminder2 = ReminderDTO("Title2", "Description2","Location 2", (0..360).random().toDouble(),(0..360).random().toDouble())
    private val reminder3 = ReminderDTO("Title3", "Description3","Location 3", (0..360).random().toDouble(),(0..360).random().toDouble())
    private val reminderNew = ReminderDTO("TitleNew", "DescriptionNew","Location New", (0..360).random().toDouble(),(0..360).random().toDouble())
    private val localReminders = listOf(reminder1,reminder2,reminder3).sortedBy { it.id }
    private val newReminders = listOf(reminder1,reminder2,reminder3,reminderNew).sortedBy { it.id }

    @Before
    fun initDB() {
        val dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

}