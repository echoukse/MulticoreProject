ant test
cd build
java -cp ../dist/lib/ActiveMonitor-20141116.jar:. examples/Stack/WarmUpRandomTest 1000000 5 50 50 128
cd ..
