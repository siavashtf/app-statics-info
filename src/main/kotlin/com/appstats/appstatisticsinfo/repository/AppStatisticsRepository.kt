package com.appstats.appstatisticsinfo.repository

import com.appstats.appstatisticsinfo.domain.AppStatistics
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface AppStatisticsRepository: MongoRepository<AppStatistics, String> {
    fun findAllByReportTimeBetweenAndType(startDate: Date, endDate: Date, type: Int): List<AppStatistics>
}