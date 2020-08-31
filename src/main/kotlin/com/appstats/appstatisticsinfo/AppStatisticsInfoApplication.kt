package com.appstats.appstatisticsinfo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class AppStatisticsInfoApplication

fun main(args: Array<String>) {
	runApplication<AppStatisticsInfoApplication>(*args)
}
