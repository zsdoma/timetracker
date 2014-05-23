timetracker
===========

TimeTracker application and library for ProgKörny course in University of Debrecen.

TimeTracke CLI usage
--------------------

```
timetracker -s "start log"
timetracker -e -endDate "2014.03.13. 22.22" -message "start log updated with end log"
timetracker -l -date "2014.03.13."
timetracker -r -id 200202020
```


TimeTracker Core and API
----------------------------


```java
TimeTracker timeTracker = new DefaultTimeTracker(new XmlDateSource(new File("")));
timeTracker.start("start log");
```
