package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.fake.FakeReminderDAO
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.rules.MainCoroutineRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val reminderDTOS =  listOf<ReminderDTO>(
        ReminderDTO("title1", "description","location",(-360..360).random().toDouble(),(-360..360).random().toDouble()),
        ReminderDTO("title2", "description","location",(-360..360).random().toDouble(),(-360..360).random().toDouble()),
        ReminderDTO("title3", "description","location",(-360..360).random().toDouble(),(-360..360).random().toDouble())
    )
    private val reminderDTO1 = reminderDTOS[0]
    private val reminderDTO2 = reminderDTOS[1]
    private val newReminderDTO = reminderDTOS[2]
    private lateinit var remindersDao: FakeReminderDAO
    private lateinit var localRepository: RemindersLocalRepository


}