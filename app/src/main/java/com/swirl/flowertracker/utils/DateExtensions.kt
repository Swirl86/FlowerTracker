package com.swirl.flowertracker.utils

import android.annotation.SuppressLint
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

fun LocalDateTime.dateTimeToString(): String {
    val pattern = "dd MMM yyyy HH:mm"
    return this.format(DateTimeFormatter.ofPattern(pattern))
}

fun LocalDateTime.toMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun LocalDateTime.yearRange(nr: Int = 10) = (this.minusYears(nr.toLong()).year..this.plusYears(nr.toLong()).year)

@SuppressLint("ConstantLocale")
private val defaultDateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy", Locale.getDefault())

fun Long.convertMillisToLocalDate() : LocalDate {
    return Instant
        .ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

fun LocalDate.convertLocalDateToMillis(dateTimeFormatter: DateTimeFormatter = defaultDateFormatter) : Long {
    return LocalDate.parse(this.format(dateTimeFormatter), dateTimeFormatter)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

fun convertMillisToLocalDateWithFormatter(date: LocalDate) : LocalDate {
    val dateInMillis = date.convertLocalDateToMillis()

    return dateInMillis.convertMillisToLocalDate()
}

fun LocalDate.dateToString(): String {
    val dateInMillis = convertMillisToLocalDateWithFormatter(this)
    return defaultDateFormatter.format(dateInMillis)
}

/** Extension function to get ISO week number */
val LocalDate.isoWeekNumber: Int
    get() = this.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())