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
import org.junit.*
import java.util.*

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
    @Test
    fun deleteRemindersTest() = runBlockingTest {
        //GIVEN Some values to be inserted in db
        val reminderDTOList = listOf<ReminderDTO>(
            ReminderDTO("title1", "description","location",(0..360).random().toDouble(),(0..360).random().toDouble()),
            ReminderDTO("title2", "description","location",(0..360).random().toDouble(),(0..360).random().toDouble()),
            ReminderDTO("title3", "description","location",(0..360).random().toDouble(),(0..360).random().toDouble()))
        reminderDTOList.forEach {db.reminderDao().saveReminder(it)}
        //WHEN deleting all elements
        db.reminderDao().deleteAllReminders()
        //THEN db is empty
        val reminderDTOList1 = db.reminderDao().getReminders()
        Assert.assertThat(reminderDTOList1.isEmpty(), `is`(true))
    }
    @Test
    fun reminderByIdNotFound() =
        runBlockingTest {
            //GIVEN random ID
            val id = UUID.randomUUID().toString()
            //WHEN extracting the item from the data base with that id
            val reminderDTO = db.reminderDao().getReminderById(id)
            //THEN the expected value should be null and a value in P of (1/billion*billion)
            Assert.assertNull(reminderDTO)
        }
    @Test
    fun insertReminderByID() =
        runBlockingTest {
            //GIVEN ReminderDTO into the database
            val reminderDTO = ReminderDTO(
                "title",
                "description",
                "location",(0..360).random().toDouble(),(0..360).random().toDouble())
            db.reminderDao().saveReminder(reminderDTO)
            //WHEN using id to extract reminders
            val reminderDTO1 = db.reminderDao().getReminderById(reminderDTO.id)
            //THEN we got one reminder data and it suppose to equal same as the original inserted value
            Assert.assertThat<ReminderDTO>(reminderDTO1 as ReminderDTO, notNullValue())
            Assert.assertThat(reminderDTO1.id, `is`(reminderDTO.id))
            Assert.assertThat(reminderDTO1.title, `is`(reminderDTO.title))
            Assert.assertThat(reminderDTO1.description, `is`(reminderDTO.description))
            Assert.assertThat(reminderDTO1.location, `is`(reminderDTO.location))
            Assert.assertThat(reminderDTO1.latitude, `is`(reminderDTO.latitude))
            Assert.assertThat(reminderDTO1.longitude, `is`(reminderDTO.longitude))
        }
}