package com.example.calendar

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import org.junit.Test

class ComposeInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testDialogDisplay1() {
        composeTestRule.onNodeWithText("15", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithText("This day is 45 day(s) since the start of 2024.").assertIsDisplayed()
    }
    @Test
    fun testDialogDisplay2() {
        composeTestRule.onNodeWithText("1", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithText("This day is 31 day(s) since the start of 2024.").assertIsDisplayed()
    }

    @Test
    fun testDialogDisplay3() {
        composeTestRule.onNodeWithText("29", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithText("This day is 59 day(s) since the start of 2024.").assertIsDisplayed()
    }

}
