# app-statistics-info
Applications statistical information project using Spring Boot, Kotlin and MongoDB.

In this project, we are employing some stored data which contain a date-time field and multiple numeric information.
We are going to map the stored data to an object which represents our desired output. The date-time field, which we will 
refer to it as "report time," is simply a time of a report.
The query will get the stored data in a range of report times based on a specific "type" included in the query.
The fetched data will be extracted into a week number, and a year to which reports time belongs.
Finally, the output will be sorted and represents the accumulative statistics information categorized weekly during 
the year.

### sample stored data
    {
        "reportTime" : ISODate("2017-03-15T23:35:20.201Z"),
        "type" : 1,
        "videoRequests" : 1,
        "webViewRequests" : 1,
        "videoClicks" : 1,
        "webViewClicks" : 1,
        "videoInstalls" : 1,
        "webViewInstalls" : 1
    }
    
    {
        "reportTime" : ISODate("2017-03-16T23:36:20.201Z"),
        "type" : 1,
        "videoRequests" : 2,
        "webViewRequests" : 2,
        "videoClicks" : 2,
        "webViewClicks" : 2,
        "videoInstalls" : 2,
        "webViewInstalls" : 2
    }
    
    {
        "reportTime" : ISODate("2017-03-17T23:37:20.201Z"),
        "type" : 1,
        "videoRequests" : 3,
        "webViewRequests" : 3,
        "videoClicks" : 3,
        "webViewClicks" : 3,
        "videoInstalls" : 3,
        "webViewInstalls" : 3
    }
    
    {
        "reportTime" : ISODate("2017-03-18T23:38:20.201Z"),
        "type" : 1,
        "videoRequests" : 4,
        "webViewRequests" : 4,
        "videoClicks" : 4,
        "webViewClicks" : 4,
        "videoInstalls" : 4,
        "webViewInstalls" : 4
    }
    
    {
        "reportTime" : ISODate("2017-03-19T23:39:20.201Z"),
        "type" : 1,
        "videoRequests" : 5,
        "webViewRequests" : 5,
        "videoClicks" : 5,
        "webViewClicks" : 5,
        "videoInstalls" : 5,
        "webViewInstalls" : 5
    }
    
    {
        "reportTime" : ISODate("2017-03-20T23:40:20.201Z"),
        "type" : 1,
        "videoRequests" : 6,
        "webViewRequests" : 6,
        "videoClicks" : 6,
        "webViewClicks" : 6,
        "videoInstalls" : 6,
        "webViewInstalls" : 6
    }
    
    {
        "reportTime" : ISODate("2017-03-21T23:41:20.201Z"),
        "type" : 1,
        "videoRequests" : 7,
        "webViewRequests" : 7,
        "videoClicks" : 7,
        "webViewClicks" : 7,
        "videoInstalls" : 7,
        "webViewInstalls" : 7
    }
    
    {
        "reportTime" : ISODate("2017-03-22T23:42:20.201Z"),
        "type" : 1,
        "videoRequests" : 8,
        "webViewRequests" : 8,
        "videoClicks" : 8,
        "webViewClicks" : 8,
        "videoInstalls" : 8,
        "webViewInstalls" : 8
    }
    
    {
        "reportTime" : ISODate("2017-03-23T23:43:20.201Z"),
        "type" : 1,
        "videoRequests" : 9,
        "webViewRequests" : 9,
        "videoClicks" : 9,
        "webViewClicks" : 9,
        "videoInstalls" : 9,
        "webViewInstalls" : 9
    }
    
    {
        "reportTime" : ISODate("2018-03-19T23:39:20.201Z"),
        "type" : 1,
        "videoRequests" : 10,
        "webViewRequests" : 10,
        "videoClicks" : 10,
        "webViewClicks" : 10,
        "videoInstalls" : 10,
        "webViewInstalls" : 10
    }
    
    {
        "reportTime" : ISODate("2018-03-20T23:40:20.201Z"),
        "type" : 1,
        "videoRequests" : 11,
        "webViewRequests" : 11,
        "videoClicks" : 11,
        "webViewClicks" : 11,
        "videoInstalls" : 11,
        "webViewInstalls" : 11
    }
    
    {
        "reportTime" : ISODate("2018-03-21T23:41:20.201Z"),
        "type" : 1,
        "videoRequests" : 12,
        "webViewRequests" : 12,
        "videoClicks" : 12,
        "webViewClicks" : 12,
        "videoInstalls" : 12,
        "webViewInstalls" : 12
    }
    
    {
        "reportTime" : ISODate("2018-03-22T23:42:20.201Z"),
        "type" : 1,
        "videoRequests" : 13,
        "webViewRequests" : 13,
        "videoClicks" : 13,
        "webViewClicks" : 13,
        "videoInstalls" : 13,
        "webViewInstalls" : 13
    }
    
    {
        "reportTime" : ISODate("2018-03-23T23:43:20.201Z"),
        "type" : 1,
        "videoRequests" : 14,
        "webViewRequests" : 14,
        "videoClicks" : 14,
        "webViewClicks" : 14,
        "videoInstalls" : 14,
        "webViewInstalls" : 14
    }
    
    {
        "reportTime" : ISODate("2018-03-24T23:44:20.201Z"),
        "type" : 1,
        "videoRequests" : 15,
        "webViewRequests" : 15,
        "videoClicks" : 15,
        "webViewClicks" : 15,
        "videoInstalls" : 15,
        "webViewInstalls" : 15
    }
    
    {
        "reportTime" : ISODate("2018-03-25T23:45:20.201Z"),
        "type" : 1,
        "videoRequests" : 16,
        "webViewRequests" : 16,
        "videoClicks" : 16,
        "webViewClicks" : 16,
        "videoInstalls" : 16,
        "webViewInstalls" : 16
    }
    
    {
        "reportTime" : ISODate("2018-03-26T23:46:20.201Z"),
        "type" : 1,
        "videoRequests" : 17,
        "webViewRequests" : 17,
        "videoClicks" : 17,
        "webViewClicks" : 17,
        "videoInstalls" : 17,
        "webViewInstalls" : 17
    }
    
    {
        "reportTime" : ISODate("2018-09-22T05:41:20.201Z"),
        "type" : 1,
        "videoRequests" : 18,
        "webViewRequests" : 18,
        "videoClicks" : 18,
        "webViewClicks" : 18,
        "videoInstalls" : 18,
        "webViewInstalls" : 18
    }
    
    {
        "reportTime" : ISODate("2018-09-23T05:42:20.201Z"),
        "type" : 1,
        "videoRequests" : 19,
        "webViewRequests" : 19,
        "videoClicks" : 19,
        "webViewClicks" : 19,
        "videoInstalls" : 19,
        "webViewInstalls" : 19
    }
    
    {
        "reportTime" : ISODate("2018-09-24T05:43:20.201Z"),
        "type" : 1,
        "videoRequests" : 20,
        "webViewRequests" : 20,
        "videoClicks" : 20,
        "webViewClicks" : 20,
        "videoInstalls" : 20,
        "webViewInstalls" : 20
    }
    
    {
        "reportTime" : ISODate("2018-09-25T05:44:20.201Z"),
        "type" : 1,
        "videoRequests" : 21,
        "webViewRequests" : 21,
        "videoClicks" : 21,
        "webViewClicks" : 21,
        "videoInstalls" : 21,
        "webViewInstalls" : 21
    }
    
    {
        "reportTime" : ISODate("2018-09-26T05:45:20.201Z"),
        "type" : 1,
        "videoRequests" : 22,
        "webViewRequests" : 22,
        "videoClicks" : 22,
        "webViewClicks" : 22,
        "videoInstalls" : 22,
        "webViewInstalls" : 22
    }
    
    {
        "reportTime" : ISODate("2018-09-27T05:46:20.201Z"),
        "type" : 1,
        "videoRequests" : 23,
        "webViewRequests" : 23,
        "videoClicks" : 23,
        "webViewClicks" : 23,
        "videoInstalls" : 23,
        "webViewInstalls" : 23
    }
    
    {
        "reportTime" : ISODate("2018-09-28T05:47:20.201Z"),
        "type" : 1,
        "videoRequests" : 24,
        "webViewRequests" : 24,
        "videoClicks" : 24,
        "webViewClicks" : 24,
        "videoInstalls" : 24,
        "webViewInstalls" : 24
    }

### sample output 
    http://localhost:8080/app-statistics-info?startDate=2017-03-10&endDate=2018-09-30&type=1    
    
    {
        "stats": [
            {
                "weekNum": 52,
                "year": 1395,
                "requests": 30,
                "clicks": 30,
                "installs": 30
            },
            {
                "weekNum": 53,
                "year": 1395,
                "requests": 12,
                "clicks": 12,
                "installs": 12
            },
            {
                "weekNum": 1,
                "year": 1396,
                "requests": 48,
                "clicks": 48,
                "installs": 48
            },
            {
                "weekNum": 52,
                "year": 1396,
                "requests": 20,
                "clicks": 20,
                "installs": 20
            },
            {
                "weekNum": 53,
                "year": 1396,
                "requests": 22,
                "clicks": 22,
                "installs": 22
            },
            {
                "weekNum": 1,
                "year": 1397,
                "requests": 174,
                "clicks": 174,
                "installs": 174
            },
            {
                "weekNum": 27,
                "year": 1397,
                "requests": 156,
                "clicks": 156,
                "installs": 156
            },
            {
                "weekNum": 28,
                "year": 1397,
                "requests": 138,
                "clicks": 138,
                "installs": 138
            }
        ]
    }
    
    
