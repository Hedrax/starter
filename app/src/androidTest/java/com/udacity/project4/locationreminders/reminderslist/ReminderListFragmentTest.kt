package com.udacity.project4.locationreminders.reminderslist

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.fake.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest :AutoCloseKoinTest(){

    private lateinit var dataSource: FakeDataSource
    private lateinit var viewModel: RemindersListViewModel
    private val reminder1 = ReminderDTO("Title1", "Description1","Location 1", (0..360).random().toDouble(),(0..360).random().toDouble())


    @Before
    fun varInit(){
        dataSource = FakeDataSource()
        viewModel = RemindersListViewModel(getApplicationContext(),dataSource)
        stopKoin()
        startKoin { module{single {viewModel}} }
    }

    @Test
    fun listDisplay() = runBlockingTest {
        //Given the reminder at the beginning of the datasource
        dataSource.saveReminder(reminder1)

        //When launching the fragment
        launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        //Then it will be displayed first
        onView(withId(R.id.reminderCardView)).check(matches(isDisplayed()))
        onView(withId(R.id.title)).check(matches(withText( reminder1.title)))
    }

    @Test
    fun errorMessage_RemovingAllReminders() = runBlockingTest {
            //WHEN deleting all reminders
            dataSource.deleteAllReminders()
            //Then a message will show up
            onView(withText("No reminders found")).check(matches(isDisplayed()))
        }

    @Test
    fun navigationAway_navigatingToAddReminder() = runBlockingTest {
        //GIVEN launching the Reminder List Fragment
        val fragmentScenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        fragmentScenario.onFragment {Navigation.setViewNavController(it.view!!, navController)}
        //WHEN clicking on the add Reminder floating button
        onView(withId(R.id.addReminderFAB)).perform(click())
        //THEN it will navigate away to saveReminder
        verify(navController).navigate(ReminderListFragmentDirections.toSaveReminder())
    }
}