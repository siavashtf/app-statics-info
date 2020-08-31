package com.appstats.appstatisticsinfo.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class DateConverterUtil {
    companion object {
        private fun convertInstantToLocalDate(instant: Instant): LocalDate {
            return LocalDateTime.ofInstant(instant, ZoneOffset.UTC).toLocalDate()
        }

        fun convertDateToLocalDate(date: Date): LocalDate {
            return convertInstantToLocalDate(date.toInstant())
        }
    }
}