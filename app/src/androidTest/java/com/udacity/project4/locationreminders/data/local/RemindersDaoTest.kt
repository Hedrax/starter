package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*
import kotlin.random.Random

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {
    @get:Rule
    var instantExecutor = InstantTaskExecutorRule()
    private lateinit var db: RemindersDatabase
    //using the database to store values of ReminderDTO then checks for the stored values
    @Before
    fun initDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java).build()
    }
    @After
    fun dataBaseClosure() = db.close()
    @Test
    fun getRemindersTesting() = runBlockingTest {
        //GIVEN a reminder into the database
        val reminder = ReminderDTO(
            "title",
            "description",
            "location",
            (0..360).random().toDouble(),
            (0..360).random().toDouble())
        db.reminderDao().saveReminder(reminder)
        //When extracting the reminder of the db
        val remindersLst = db.reminderDao().getReminders()
        //THEN the lst that represents database has only one item in it
        Assert.assertThat(remindersLst.size, `is`(1))
        Assert.assertThat(remindersLst[0].id, `is`(reminder.id))
        Assert.assertThat(remindersLst[0].location, `is`(reminder.location))
        Assert.assertThat(remindersLst[0].latitude, `is`(reminder.latitude))
        Assert.assertThat(remindersLst[0].longitude, `is`(reminder.longitude))
        Assert.assertThat(remindersLst[0].title, `is`(reminder.title))
        Assert.assertThat(remindersLst[0].description, `is`(reminder.description))
    }
}