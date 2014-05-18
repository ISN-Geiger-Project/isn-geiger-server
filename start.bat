@echo off
java -Xms16m -Xmx256m -XX:MinHeapFreeRatio=5 -XX:MaxHeapFreeRatio=25 -XX:+UseParNewGC -XX:GCTimeRatio=32 -jar ./ISNGeigerChecker.jar
pause