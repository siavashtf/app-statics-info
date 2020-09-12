package com.appstats.appstatisticsinfo.service

import com.appstats.appstatisticsinfo.domain.AppStatistics
import com.appstats.appstatisticsinfo.model.AppStatisticsListResponse
import com.appstats.appstatisticsinfo.model.AppStatisticsModel
import com.appstats.appstatisticsinfo.repository.AppStatisticsRepository
import com.appstats.appstatisticsinfo.utils.ConstantProperties
import com.appstats.appstatisticsinfo.utils.DateConverterUtil
import com.appstats.appstatisticsinfo.utils.Log
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*
import kotlin.math.ceil

@Service
class AppStatsServiceImpl(private val appStatisticsRepository: AppStatisticsRepository) : AppStatsService {
    companion object : Log()

    /**
     * This method will be used to get an AppStatisticListResponse which includes a list of models.
     * Also it will cache the data using a combined key for a period of time which has been set in properties file.
     * @param: query filters
     * @return: A response object which contains desired data based on filters
     */
    @Cacheable(value = ["stats-cache"], key = "{#startDate, #endDate, #type}")
    override fun getStats(startDate: Date, endDate: Date, type: Int): AppStatisticsListResponse {
        logger.info("=================> service: getStats called <=================")

        val stats = getAppStatisticsModelList(startDate, endDate, type)
        if (stats != null && stats.isNotEmpty()) {
            val categorizedStatsByYear = groupStatsByYear(stats.toMutableList())
            logger.info("=================> service: stats have been categorized by the year number <=================")

            val categorizedAccumulativeStats: MutableList<AppStatisticsModel> = ArrayList()
            for (statsInYear in categorizedStatsByYear.values) {
                logger.info("=================> service: categorizing stats of year: ${categorizedStatsByYear.filterValues { it == statsInYear }.keys}<=================")
                categorizedAccumulativeStats.addAll(groupStatsByWeekNumAndAccumulativeInfo(statsInYear.toMutableList()).values)
            }
            logger.info("=================> service: categorized accumulated stats are going to be sorted... <=================")

            val sortedStats = sortBasedOnYearAndWeekNum(categorizedAccumulativeStats)

            logger.info("=================> service: instantiating response object with sorted list... <=================")

            return AppStatisticsListResponse(sortedStats)
        }
        logger.info("=================> service: Due to no data has been fetched, response object does not contain any list <=================")

        return AppStatisticsListResponse(null)
    }

    /**
     * This method will group the model list by the week number, and will accumulate the information of each week
     * @param: a model list
     * @return: grouped model list
     */
    private fun groupStatsByWeekNumAndAccumulativeInfo(sortedList: MutableList<AppStatisticsModel>): Map<Int, AppStatisticsModel> {
        logger.info("=================> service: grouping each year stats by week number, and accumulating each week info... <=================")
        return sortedList.groupingBy(AppStatisticsModel::weekNum).fold(AppStatisticsModel(0, 0, 0, 0, 0),
                { acc, elem ->
                    AppStatisticsModel(elem.weekNum, elem.year,
                            acc.requests + elem.requests,
                            acc.clicks + elem.clicks,
                            acc.installs + elem.installs)
                })
    }

    /**
     * This method will group the model list by the year number
     * @param: a model list
     * @return: grouped model list
     */
    private fun groupStatsByYear(stats: MutableList<AppStatisticsModel>): Map<Int, List<AppStatisticsModel>> {
        logger.info("=================> service: grouping model list by year number... <=================")
        return stats.groupBy { it.year }
    }

    /**
     * This method will sort the mapped models list based on their year and weekNum
     * @param: The list which is going to be sorted
     * @return: The sorted list
     */
    private fun sortBasedOnYearAndWeekNum(stats: MutableList<AppStatisticsModel>): List<AppStatisticsModel> {
        logger.info("=================> service: sorting categorized model list... <=================")

        //*** Sorting models list based on their year and then their week number
        return stats.sortedWith(compareBy<AppStatisticsModel> { it.year }.thenBy { it.weekNum })
    }

    /**
     *  This method will be used get the mapped list of models which has been obtained from app statistics domains
     *  @param: query filters
     *  @return: A list of mapped objects
     */
    private fun getAppStatisticsModelList(startDate: Date,
                                          endDate: Date,
                                          type: Int): MutableList<AppStatisticsModel>? {
        logger.info("=================> service: calling repository method to fetch data... <=================")
        logger.info("=================> query filters: $startDate, $endDate, $type <=================")

        //*** Retrieving data based on filters
        val appStatisticList = appStatisticsRepository.findAllByReportTimeBetweenAndType(startDate, endDate, type)

        if (appStatisticList.isNotEmpty())
            logger.info("=================> service: data fetched <=================")
        else {
            logger.info("=================> service: no data fetched based on these filters <=================")
            return null
        }

        //*** Using another list to store mapped objects
        val appStatisticsModelList = mutableListOf<AppStatisticsModel>()

        logger.info("=================> service: mapping domains onto models... <=================")
        //*** Mapping each domain object onto a model
        for (appStatistic in appStatisticList) {
            appStatisticsModelList.add(mapAppStatisticToAppStatisticModel(appStatistic))
        }

        return appStatisticsModelList
    }

    /**
     * This method will map a retrieved domain onto a model
     * @param: An object of domain
     * @return: An object of model
     */
    private fun mapAppStatisticToAppStatisticModel(appStatistic: AppStatistics): AppStatisticsModel {
        logger.info("=================> service: domain with id ${appStatistic.id} is going to be mapped... <=================")

        //*** Converting date object onto local date object to use its methods
        val localDate: LocalDate = DateConverterUtil.convertDateToLocalDate(appStatistic.reportTime)

        //*** Which year and day in solar calender does the report belong to?
        val (year, day) = calculateSolarYearAndDay(localDate)

        //*** Which week of the year?
        val weekNum: Int = calculateWeekNum(day)

        logger.info("================================= year: $year, day: $day, weekNum: $weekNum ==================================")

        //*** requests = videoRequests + webViewRequest
        val requests: Int = appStatistic.videoRequests.plus(appStatistic.webViewRequests)

        //*** clicks = videoClicks + webViewClicks
        val clicks: Int = appStatistic.videoClicks.plus(appStatistic.webViewClicks)

        //*** installs = videoInstalls + webViewInstalls
        val installs: Int = appStatistic.videoInstalls.plus(appStatistic.webViewInstalls)

        //*** Instantiating a model by resulted data
        return AppStatisticsModel(weekNum, year, requests, clicks, installs)
    }

    /**
     * This method will extract the Solar year and day out of Georgian date by using some constant numbers to map
     * Georgian year and day onto Solar year and day.
     * March is a special month because the new solar year happens in this month. Also, the 20th and 21st of this
     * month are the days in which the new year happens. We will use these special cases to determine solar date.
     * @param: a Georgian date
     * @return: equivalent Solar year and day
     */
    private fun calculateSolarYearAndDay(localDate: LocalDate): Pair<Int, Int> {
        val day: Int
        val year: Int

        //*** months greater than March
        //*** formula: solarDay = georgianDay - 79
        //*** formula: solarYear = georgianYear - 621
        if (localDate.monthValue > ConstantProperties.MARCH_MONTH_NUM) {
            day = localDate.dayOfYear - ConstantProperties.DIFFERENCE_DAYS_BETWEEN_GEORGIAN_AND_SOLAR_AT_BEGINNING_OF_SOLAR_YEAR
            year = localDate.year - ConstantProperties.DIFFERENCE_YEARS_BETWEEN_GEORGIAN_AND_SOLAR_AT_BEGINNING_OF_SOLAR_YEAR
        } else {
            //*** months lower than March
            if (localDate.monthValue < ConstantProperties.MARCH_MONTH_NUM) {
                //*** the year before the current year was leap year
                //*** formula: solarDay = georgianDay + 286 ~> Jan 1st and Feb 29th are special cases
                //*** formula: solarYear = georgianYear - 622
                day = if (isLeapYear(localDate.year - 1) && (localDate.dayOfYear == 1 || localDate.dayOfYear == 59)) {
                    localDate.dayOfYear + ConstantProperties.DIFFERENCE_DAYS_BETWEEN_GEORGIAN_AND_SOLAR_AT_END_OF_SOLAR_YEAR + 1
                } else {
                    localDate.dayOfYear + ConstantProperties.DIFFERENCE_DAYS_BETWEEN_GEORGIAN_AND_SOLAR_AT_END_OF_SOLAR_YEAR
                }
                year = localDate.year - ConstantProperties.DIFFERENCE_YEARS_BETWEEN_GEORGIAN_AND_SOLAR_AT_END_OF_SOLAR_YEAR
            }
            //*** March
            else {
                //*** leap year
                if (isLeapYear(localDate.year)) {
                    //*** anyGeorgianLeapYear/3/20 ~ anySolarLeapYear/1/1
                    if (localDate.dayOfMonth >= ConstantProperties.DAY_NUM_OF_MARCH_MONTH_AT_BEGINNING_OF_SOLAR_LEAP_YEAR) {
                        day = localDate.dayOfYear - ConstantProperties.DIFFERENCE_DAYS_BETWEEN_GEORGIAN_AND_SOLAR_AT_BEGINNING_OF_SOLAR_YEAR
                        year = localDate.year - ConstantProperties.DIFFERENCE_YEARS_BETWEEN_GEORGIAN_AND_SOLAR_AT_BEGINNING_OF_SOLAR_YEAR
                    } else {
                        day = localDate.dayOfYear + ConstantProperties.DIFFERENCE_DAYS_BETWEEN_GEORGIAN_AND_SOLAR_AT_END_OF_SOLAR_YEAR
                        year = localDate.year - ConstantProperties.DIFFERENCE_YEARS_BETWEEN_GEORGIAN_AND_SOLAR_AT_END_OF_SOLAR_YEAR
                    }
                } else {
                    //*** anyGeorgianNormalYear/3/21 ~ anySolarNormalYear/1/1
                    if (localDate.dayOfMonth >= ConstantProperties.DAY_NUM_OF_MARCH_MONTH_AT_BEGINNING_OF_SOLAR_LEAP_YEAR + 1) {
                        day = localDate.dayOfYear - ConstantProperties.DIFFERENCE_DAYS_BETWEEN_GEORGIAN_AND_SOLAR_AT_BEGINNING_OF_SOLAR_YEAR
                        year = localDate.year - ConstantProperties.DIFFERENCE_YEARS_BETWEEN_GEORGIAN_AND_SOLAR_AT_BEGINNING_OF_SOLAR_YEAR
                    } else {
                        day = localDate.dayOfYear + ConstantProperties.DIFFERENCE_DAYS_BETWEEN_GEORGIAN_AND_SOLAR_AT_END_OF_SOLAR_YEAR
                        year = localDate.year - ConstantProperties.DIFFERENCE_YEARS_BETWEEN_GEORGIAN_AND_SOLAR_AT_END_OF_SOLAR_YEAR
                    }
                }
            }
        }

        return Pair(year, day)
    }

    /**
     * To determine the year is a leap year or not
     * @param: a year
     * @return: is a leap year or not
     */
    private fun isLeapYear(year: Int): Boolean {
        return year.rem(ConstantProperties.LEAP_YEAR_PERIOD) == 0
    }

    /**
     * This method calculates the week number of the year based on the day of report time
     * @param: days number of the year
     * @return: number of the week
     */
    private fun calculateWeekNum(dayOfYear: Int): Int {
        //*** Dividing days number of the year by days number of the week then rounding it up to the nearest integer
        return ceil(dayOfYear.toDouble() / ConstantProperties.WEEK_DAYS_NUM).toInt()
    }
}