package com.appstats.appstatisticsinfo.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "appStatistics")
data class AppStatistics(@Id val id: String,
                    val reportTime: Date,
                    val type: Int,
                    val videoRequests: Int = 0,
                    val webViewRequests: Int = 0,
                    val videoClicks: Int = 0,
                    val webViewClicks: Int = 0,
                    val videoInstalls: Int = 0,
                    val webViewInstalls: Int = 0)