timetracker [![Build Status](https://travis-ci.org/zsdoma/timetracker.svg?branch=master)](https://travis-ci.org/zsdoma/timetracker)
===========

TimeTracker application and library for ProgKörny course in University of Debrecen.

TimeTracke CLI usage
--------------------
Examples:
```
timetracker -s -message "start log"
timetracker -s "start log" -startDate "2014.03.03 12:00" -message "message"
timetracker -e -endDate "2014.03.13. 22.22" -message "start log updated with end log"
timetracker -l -date "2014.03.13."
timetracker -r -id 1394749320000
```

List all worklog.
```
timetracker --list -date "*"
```
Result:
```
+---------------+-------------------+-------------------+---------------------------+
| Id            | Start Date        | End Date          | Message                   |
+---------------+-------------------+-------------------+---------------------------+
| 1401004800000 | 2014.05.25. 10:00 | 2014.05.25. 12:10 | tomorrow worklog 1        |
| 1401055080000 | 2014.05.25. 23:58 | 2014.05.26. 00:10 | midnight test             |
| 1401122700795 | 2014.05.26. 18:45 | 2014.05.26. 18:46 | work finished 2           |
| 1401123600000 | 2014.05.26. 19:00 | 2014.05.26. 20:46 | test worklog              |
| 1401139560947 | 2014.05.26. 23:26 | 2014.05.27. 03:00 | hello                     |
+---------------+-------------------+-------------------+---------------------------+
```


TimeTracker Core and API
----------------------------
Example:
```java
TimeTracker timeTracker = new DefaultTimeTracker(new XmlDateSource(new File("timetracker.xml")));
timeTracker.start("start log");
timeTracker.end();
timeTracker.list();
```
