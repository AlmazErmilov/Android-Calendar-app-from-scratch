import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendar.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalendarInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testDialogDisplay() {
        // Perform action to select a day, for example, click on a specific day
        // This depends on your app's UI implementation, for demonstration purposes, let's say we click on day 15
        onView(withText("15")).perform(click())

        // Check if the dialog is displayed
        onView(withText("Selected Date")).check(matches(isDisplayed()))

        // Here you should assert that the correct number of days since January 1st is displayed
        // Replace the text matcher and assertion with the appropriate logic based on your app's behavior
        onView(withText("Number of days since 1 January: 15")).check(matches(isDisplayed()))
    }
}