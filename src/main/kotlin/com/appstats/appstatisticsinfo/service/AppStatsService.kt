package com.appstats.appstatisticsinfo.service

import com.appstats.appstatisticsinfo.model.AppStatisticsListResponse
import java.util.*

interface AppStatsService {
    fun getStats(startDate: Date, endDate: Date, type: Int): AppStatisticsListResponse
}