
```
rrdtool graph output.png -a PNG --title "NetBeans Heap Usage" -s -1h \
 DEF:used=/var/lib/ganglia/rrds/SPL/localhost.localdomain/NetBeans_Memory_Heap_used.rrd:sum:AVERAGE \
 DEF:max=/var/lib/ganglia/rrds/SPL/localhost.localdomain/NetBeans_Memory_Heap_max.rrd:sum:AVERAGE \
 DEF:committed=/var/lib/ganglia/rrds/SPL/localhost.localdomain/NetBeans_Memory_Heap_committed.rrd:sum:AVERAGE \
 LINE:max#ff0000:"Max Heap" \
 LINE:committed#00ff00:"Committed Heap" \
 AREA:used#0000ff:"Used Heap" \
```

![http://www.specialprojectslab.com/images/NetBeans_Composite.png](http://www.specialprojectslab.com/images/NetBeans_Composite.png)