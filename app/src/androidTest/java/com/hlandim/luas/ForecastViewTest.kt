package com.hlandim.luas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalFoundationApi
@HiltAndroidTest
class ForecastViewTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun givenForecastViewVisible_shouldSeeInitialViews() {
        activityRule.let {
            // Assert top bar is visible
            it.onNode(hasText("Luas")).assertIsDisplayed()
            it.onNodeWithContentDescription("Refresh").assertIsDisplayed()

            // Assert forecast list is visible
            it.onNodeWithContentDescription("Stop title").assertIsDisplayed()
            it.onNodeWithContentDescription("Forecast List").assertIsDisplayed()
        }
    }

    @Test
    fun whenClickRefresh_thenLoadingShouldAppear() {
        activityRule.let {
            it.onNodeWithContentDescription("Refresh").performClick()
            it.waitForIdle()
            it.onNodeWithContentDescription("Loading").assertDoesNotExist()
        }
    }
}