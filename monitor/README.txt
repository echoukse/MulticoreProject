// To build, go to monitor directory and run:
ant test

// To run the Stack test:
cd build
java -cp ../dist/lib/ActiveMonitor-20141116.jar:. examples/Stack/WarmUpRandomTest <arguments per ActiveSortedList.java>
// E.g.:
java -cp ../dist/lib/ActiveMonitor-20141116.jar:. examples/Stack/WarmUpRandomTest 0 0 100000 a 4 50 50 1024
