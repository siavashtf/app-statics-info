package com.appstats.appstatisticsinfo.controller

import com.appstats.appstatisticsinfo.model.AppStatisticsListResponse
import com.appstats.appstatisticsinfo.service.AppStatsService
import com.appstats.appstatisticsinfo.utils.Log
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping(value = ["/app-statistics-info"])
class AppStatisticsController(private val appStatsService: AppStatsService) {
    companion object: Log()
    /**
     * Retrieving filters to fetch data based on them
     * @param: a date range and type
     * @return: An object containing a list
     */
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getStats(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: Date,
                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: Date,
                 @RequestParam type: Int): AppStatisticsListResponse {
        logger.info("=================> controller: getStats called <=================")

        return appStatsService.getStats(startDate, endDate, type)
    }
}