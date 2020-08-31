package com.appstats.appstatisticsinfo.model

class AppStatisticsModel (val weekNum: Int,
                          val year: Int,
                          val requests: Int = 0,
                          val clicks: Int = 0,
                          val installs: Int = 0)