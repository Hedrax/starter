package com.udacity.project4

import android.app.Application
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.util.DataBindingIdlingResource
import com.udacity.project4.util.monitorActivity
import com.udacity.project4.utils.EspressoIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get

@RunWith(AndroidJUnit4::class)
@LargeTest
//END TO END test to black box test the app
class RemindersActivityTest :
    AutoCloseKoinTest() {// Extended Koin Test - embed autoclose @after method to close Koin after every test

    private lateinit var repository: ReminderDataSource
    private lateinit var appContext: Application
    private val dataBindingIdlingResource = DataBindingIdlingResource()
    /**
     * As we use Koin as a Service Locator Library to develop our code, we'll also use Koin to test our code.
     * at this step we will initialize Koin related code to be able to use it in out testing.
     */
    @Before
    fun init() {
        stopKoin()//stop the original app koin
        appContext = getApplicationContext()
        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single {
                SaveReminderViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single { RemindersLocalRepository(get()) as ReminderDataSource }
            single { LocalDB.createRemindersDao(appContext) }
        }
        //declare a new koin module
        startKoin {
            modules(listOf(myModule))
        }
        //Get our real repository
        repository = get()

        //clear the data to start fresh
        runBlocking {
            repository.deleteAllReminders()
        }
    }


    @Before
    fun registerResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }
    @ExperimentalCoroutinesApi
    @Test
    fun snackBarTest_whenSavingWithoutInfo() = runBlocking{
        // GIVEN Starting fresh activity
        val scenario = ActivityScenario.launch(RemindersActivity::class.java)
        dataBindingIdlingResource.monitorActivity(scenario)
        // WHEN - We begin entering information for the reminder.
        onView(withId(R.id.addReminderFAB)).perform(click())
        onView(withId(R.id.save_button)).perform(click())
        //Then it will show a snackBar telling missing title
        onView(withText(R.string.missing_title)).check(matches(isDisplayed()))

        //when adding location title and trying to save
        onView(withId(R.id.reminderTitle)).perform(typeText("Location1"), closeSoftKeyboard())
        onView(withId(R.id.save_button)).perform(click())
        //Then it will display a snackBar telling missing location
        onView(withText(R.string.missing_location)).check(matches(isDisplayed()))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun toastTest_whenSaving() = runBlocking{
        // Given Starting fresh activity
        val scenario = ActivityScenario.launch(RemindersActivity::class.java)
        dataBindingIdlingResource.monitorActivity(scenario)
        onView(withId(R.id.addReminderFAB)).perform(click())
        // When  save reminder frag
        onView(withId(R.id.reminderTitle)).perform(typeText("Location1"), closeSoftKeyboard())
        onView(withId(R.id.selectLocation)).perform(click())
        //When map frag
        onView(withId(R.id.map)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_action)).perform(click())
        onView(withId(R.id.save_button)).perform(click())
        //When Save frag
        onView(withId(R.id.saveReminder)).perform(click())
        //Then we will move back to list fragment and the Toast should've been shown
        onView(withText(R.string.geofence_added)).check(matches(isDisplayed()))
    }
}
