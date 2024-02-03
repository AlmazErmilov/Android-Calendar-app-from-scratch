package com.example.calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.calendar.ui.theme.CalendarTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import java.time.LocalDate
import java.time.temporal.ChronoField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalendarScreen(month = 9, year = 2024, backgroundImage = painterResource(id = R.drawable.ic_launcher_foreground))
                }
            }
        }
    }
}

@Composable
fun CalendarScreen(month: Int, year: Int, backgroundImage: Painter) {
    // This should be your ViewModel or some similar state holder
    val calendarState = remember { CalendarState() }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            BackgroundImage(image = backgroundImage)
            Column {
                CalendarHeader(monthName = month.toMonthName(), year = year)
                WeekDaysHeader()
                CalendarGrid(
                    month = month,
                    year = year,
                    calendarState = calendarState,
                    onDayClicked = { day ->
                        calendarState.showDialogForDay(day, month, year)
                    }
                )
            }
        }
    }

    if (calendarState.showDialog) {
        DayDialog(
            day = calendarState.selectedDay,
            month = month,
            year = year,
            onCloseRequest = { calendarState.showDialog = false }
        )
    }
}

class CalendarState {
    var showDialog by mutableStateOf(false)
    var selectedDay by mutableStateOf(1)
    var selectedMonth by mutableStateOf(1) // Assuming you want to show the month in the dialog
    var selectedYear by mutableStateOf(2024) // Assuming you want to show the year in the dialog

    fun showDialogForDay(day: Int, month: Int, year: Int) {
        selectedDay = day
        selectedMonth = month
        selectedYear = year
        showDialog = true
    }
}

@Composable
fun BackgroundImage(image: Painter) {
    Image(
        painter = image,
        contentDescription = null, // Decorative image does not require content description
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit // Cover ensures the image fills the bounds of the container
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalendarTheme {
        CalendarScreen(month = 9, year = 2024, backgroundImage = painterResource(id = R.drawable.ic_launcher_foreground))
    }
}

@Composable
fun CalendarHeader(monthName: String, year: Int) {
    Text(
        text = "$monthName $year",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun WeekDaysHeader() {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Absolute.SpaceBetween) {
        listOf("", "M", "T", "O", "T", "F", "L", "S").forEach { day ->
            //Spacer(modifier = Modifier.width(20.dp)) // Add some spacing
            Text(
                text = day,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold,
                    fontSize = 20.sp),
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 7.dp)
                    .width(25.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun DayDialog(day: Int, month: Int, year: Int, onCloseRequest: () -> Unit) {
    val daysSinceYearStart = calculateDaysSinceYearStart(day, month, year)
    val monthName = month.toMonthName()

    AlertDialog(
        onDismissRequest = onCloseRequest,
        title = { Text("Selected Date") },
        text = { Text("Day $day of $monthName is $daysSinceYearStart days since the start of $year.") },
        confirmButton = {
            Button(onClick = onCloseRequest) {
                Text("OK")
            }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    )
}

// Extension function to get month name from its number
fun Int.toMonthName(): String {
    return when (this) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        12 -> "December"
        else -> "Invalid month"
    }
}

fun getNumberOfDaysInMonth(month: Int, year: Int): Int {
    return LocalDate.of(year, month, 1).lengthOfMonth()
}

// Returns the day of the week for the first day of the month
fun getFirstDayOfWeekOfMonth(month: Int, year: Int): Int {
    return LocalDate.of(year, month, 1).get(ChronoField.DAY_OF_WEEK)
}

@Composable
fun CalendarGrid(month: Int, year: Int, calendarState: CalendarState, onDayClicked: (Int) -> Unit) {
    val daysInMonth = getNumberOfDaysInMonth(month, year)
    val firstDayOfWeek = getFirstDayOfWeekOfMonth(month, year) - 1 // Adjust for Monday as the first day
    val firstDateOfMonth = LocalDate.of(year, month, 1)

    // Calculate the number of rows needed for the grid
    val numberOfRows = (daysInMonth + firstDayOfWeek - 1) / 7 + 1

    Row {
        // Week numbers column
        Column {
            for (week in 0 until numberOfRows) {
                val weekStartDate = firstDateOfMonth.plusDays((week * 7).toLong() - firstDayOfWeek)
                val weekNumber = getWeekOfYear(weekStartDate)
                WeekNumber(weekNumber)
            }
        }

        // Days grid column
        Column {
            for (week in 0 until numberOfRows) {
                WeekRow(week, firstDayOfWeek, daysInMonth, month, year, calendarState, onDayClicked)
            }
        }
    }
}


@Composable
fun WeekRow(week: Int, firstDayOfWeek: Int, daysInMonth: Int, month: Int, year: Int, calendarState: CalendarState, onDayClicked: (Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        for (dayOfWeek in 1..7) {
            val dayNumber = week * 7 + dayOfWeek - firstDayOfWeek
            if (dayNumber in 1..daysInMonth) {
                DayCell(dayNumber, calendarState, month, year)
            } else {
                Spacer(modifier = Modifier.width(40.dp)) // Use Spacer for alignment
            }
        }
    }
}

@Composable
fun DayCell(dayNumber: Int?, calendarState: CalendarState, month: Int, year: Int) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(25.dp)
            .clickable(enabled = dayNumber != null) {
                dayNumber?.let {
                    calendarState.showDialogForDay(it, month, year)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (dayNumber != null) {
            Text(text = dayNumber.toString())
        }
    }
}

@Composable
fun WeekNumber(weekNumber: Int) {
    Text(
        text = weekNumber.toString(),
        modifier = Modifier
            .padding(8.dp),
            //.align(Alignment.CenterHorizontally),
        textAlign = TextAlign.Center
    )
}

fun calculateDaysSinceYearStart(day: Int, month: Int, year: Int): Int {
    val date = LocalDate.of(year, month, day)
    val yearStart = LocalDate.of(year, 1, 1)
    return date.dayOfYear - yearStart.dayOfYear
}

fun getWeekOfYear(date: LocalDate): Int {
    return date.get(WeekFields.of(Locale.getDefault()).weekOfYear())
}

