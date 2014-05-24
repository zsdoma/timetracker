timetracker [![Build Status](https://travis-ci.org/github/maven-plugins.png)](https://travis-ci.org/zsdoma/timetracker)
===========

TimeTracker application and library for ProgKörny course in University of Debrecen.

TimeTracke CLI usage
--------------------
Examples:
```
timetracker -s "start log"
timetracker -e -endDate "2014.03.13. 22.22" -message "start log updated with end log"
timetracker -l -date "2014.03.13."
timetracker -r -id 1394749320000
```

TimeTracker Core and API
----------------------------
Example:
```java
TimeTracker timeTracker = new DefaultTimeTracker(new XmlDateSource(new File("timetracker.xml")));
timeTracker.start("start log");
```
