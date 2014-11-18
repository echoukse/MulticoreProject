CXX=clang++
CXX=clang++
CXXFLAGS=-O2 -std=c++11 -I../../
LDFLAGS=-O2 -std=c++11 -pthreads -latomic

qd_comparison: counter_pthreads5 counter_std5 counter_qd1 counter_qd2 counter_qd5 counter_pthreads50 counter_std50 counter_qd50 counter_pthreads500 counter_std500 counter_qd500

atomic: counter_atomic5 counter_atomic50 counter_atomic500

all: qd_comparison atomic


%_1_128.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=1 -DDELEGATIONQLEN=128	

%_2_128.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=2 -DDELEGATIONQLEN=128

%_4_128.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=4 -DDELEGATIONQLEN=128

%_6_128.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=6 -DDELEGATIONQLEN=128

%_8_128.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=8 -DDELEGATIONQLEN=128


%_1_256.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=1 -DDELEGATIONQLEN=256	

%_2_256.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=2 -DDELEGATIONQLEN=256

%_4_256.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=4 -DDELEGATIONQLEN=256

%_6_256.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=6 -DDELEGATIONQLEN=256

%_8_256.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=8 -DDELEGATIONQLEN=256


%_1_512.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=1 -DDELEGATIONQLEN=512	

%_2_512.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=2 -DDELEGATIONQLEN=512

%_4_512.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=4 -DDELEGATIONQLEN=512

%_6_512.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=6 -DDELEGATIONQLEN=512

%_8_512.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=8 -DDELEGATIONQLEN=512


%_1_1024.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=1 -DDELEGATIONQLEN=1024	

%_2_1024.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=2 -DDELEGATIONQLEN=1024

%_4_1024.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=4 -DDELEGATIONQLEN=1024

%_6_1024.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=6 -DDELEGATIONQLEN=1024

%_8_1024.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=8 -DDELEGATIONQLEN=1024
	

%_1_2048.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=1 -DDELEGATIONQLEN=2048	

%_2_2048.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=2 -DDELEGATIONQLEN=2048

%_4_2048.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=4 -DDELEGATIONQLEN=2048

%_6_2048.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=6 -DDELEGATIONQLEN=2048

%_8_2048.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS) -DTHREADS=8 -DDELEGATIONQLEN=2048
	
	
%.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS)

counter_qd1_128: counter_qd_1_128.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd2_128: counter_qd_2_128.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd4_128: counter_qd_4_128.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd6_128: counter_qd_6_128.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd8_128: counter_qd_8_128.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)


counter_qd1_256: counter_qd_1_256.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd2_256: counter_qd_2_256.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd4_256: counter_qd_4_256.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd6_256: counter_qd_6_256.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd8_256: counter_qd_8_256.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)


counter_qd1_512: counter_qd_1_512.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd2_512: counter_qd_2_512.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd4_512: counter_qd_4_512.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd6_512: counter_qd_6_512.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd8_512: counter_qd_8_512.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)


counter_qd1_1024: counter_qd_1_1024.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd2_1024: counter_qd_2_1024.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd4_1024: counter_qd_4_1024.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd6_1024: counter_qd_6_1024.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd8_1024: counter_qd_8_1024.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)


counter_qd1_2048: counter_qd_1_2048.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd2_2048: counter_qd_2_2048.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd4_2048: counter_qd_4_2048.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd6_2048: counter_qd_6_2048.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

counter_qd8_2048: counter_qd_8_2048.o cs.o
	$(CXX) -o $@ $^ $(LDFLAGS)

clean:
	-@rm counter_qd1_128 counter_qd2_128 counter_qd4_128 counter_qd6_128 counter_qd8_128 \
	counter_qd1_256 counter_qd2_256 counter_qd4_256 counter_qd6_256 counter_qd8_256 \
	counter_qd1_512 counter_qd2_512 counter_qd4_512 counter_qd6_512 counter_qd8_512 \
	counter_qd1_1024 counter_qd2_1024 counter_qd4_1024 counter_qd6_1024 counter_qd8_1024 \
	counter_qd1_2048 counter_qd2_2048 counter_qd4_2048 counter_qd6_2048 counter_qd8_2048 \
	*.o 2>/dev/null || true
