package com.udacity.project4.locationreminders.reminderslist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.fake.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.Koin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.KoinTest
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest :AutoCloseKoinTest(){

//    TODO: test the navigation of the fragments.
//    TODO: test the displayed data on the UI.
//    TODO: add testing for the error messages.

    private lateinit var dataSource: FakeDataSource
    private lateinit var ViewModel: RemindersListViewModel
    private val reminder1 = ReminderDTO("Title1", "Description1","Location 1", (0..360).random().toDouble(),(0..360).random().toDouble())
    private val reminder2 = ReminderDTO("Title2", "Description2","Location 2", (0..360).random().toDouble(),(0..360).random().toDouble())
    private val reminder3 = ReminderDTO("Title3", "Description3","Location 3", (0..360).random().toDouble(),(0..360).random().toDouble())


}