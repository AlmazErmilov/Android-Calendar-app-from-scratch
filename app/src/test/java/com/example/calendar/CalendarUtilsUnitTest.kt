package com.example.calendar

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DateTimeException

class CalendarUtilsUnitTest {
    @Test
    fun calculateWorkingDaysOfMonth_january2024_returns23() {
        val expectedWorkingDays = 23
        val actualWorkingDays = calculateWorkingDaysOfMonth(1, 2024)
        assertEquals(expectedWorkingDays, actualWorkingDays)
    }

    @Test
    fun calculateDaysSinceYearStart_firstOfFebruary2024_returns31() {
        val expectedDaysSinceYearStart = 31
        val actualDaysSinceYearStart = calculateDaysSinceYearStart(1, 2, 2024)
        assertEquals(expectedDaysSinceYearStart, actualDaysSinceYearStart)
    }

    @Test
    fun calculateWorkingDaysOfMonth_february2024_returns21() {
        val expectedWorkingDays = 21
        val actualWorkingDays = calculateWorkingDaysOfMonth(2, 2024)
        assertEquals(expectedWorkingDays, actualWorkingDays)
    }

    @Test
    fun calculateWorkingDaysOfMonth_april2024_returns22() {
        val expectedWorkingDays = 22
        val actualWorkingDays = calculateWorkingDaysOfMonth(4, 2024)
        assertEquals(expectedWorkingDays, actualWorkingDays)
    }

    @Test
    fun calculateDaysSinceYearStart_december31_2024_returns365() {
        val expectedDaysSinceYearStart = 365
        val actualDaysSinceYearStart = calculateDaysSinceYearStart(31, 12, 2024)
        assertEquals(expectedDaysSinceYearStart, actualDaysSinceYearStart)
    }

    @Test(expected = DateTimeException::class)
    fun calculateDaysSinceYearStart_invalidDate_throwsException() {
        // Testing with an invalid date, expecting an exception.
        calculateDaysSinceYearStart(31, 2, 2024) // February 31st is invalid.
    }
}
