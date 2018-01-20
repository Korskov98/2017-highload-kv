#!/bin/bash
K=100;
SUM=0;
i=0;
until [ $i -eq $K ];
do 
TIME=`curl -o /dev/null -d value$i -w %{time_total} -s -X PUT http://localhost:8080/v0/entity?id=key$i`;
TIME="${TIME/,/.}";
SUM=$(echo "scale=3;$SUM + $TIME" | bc);
let "i=i + 1";
done;
REQUESTS=$(echo "scale=1;$K / $SUM" | bc);
SUM=$(echo "scale=1;$SUM * 1000" | bc);
AVERAGE=$(echo "scale=3;$SUM / $K" | bc);
echo "Average delay: $AVERAGE ms";
echo "Requests/sec: $REQUESTS"