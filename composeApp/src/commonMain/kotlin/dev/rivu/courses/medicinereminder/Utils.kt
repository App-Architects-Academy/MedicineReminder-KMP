package dev.rivu.courses.medicinereminder

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.TimeZone.Companion
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.format
import kotlinx.datetime.toLocalDateTime

fun getTodayDateFormatted(): String {
    val now = Clock.System.now()
    return DateTimeComponents.Formats.RFC_1123.format {
        setDateTime(now.toLocalDateTime(TimeZone.currentSystemDefault()))
    }
}

fun parseFormattedDate(formattedDate: String): Instant {
    return Instant.parse(formattedDate, DateTimeComponents.Formats.RFC_1123)
}