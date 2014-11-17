#include<array>
#include<iostream>
#include<thread>
#include "cs.hpp"
#include "qd.hpp"
#include <math.h>
#include <time.h>
#include <atomic>

static qdlock lock;
static int pushpercent, iterations;

void call_cs() {
	int *push, *val, **isElim, **elimVal, myrand;
	retval myreturn;
	push = new int;
	val = new int;
	isElim = new int*;
	elimVal = new int*;
	*isElim = new int;
	*elimVal = new int;
	srand (time(NULL));
	//for(int i = 0; i < 100000000/THREADS; i++) {
	for(int i = 0; i < iterations/THREADS; i++) {
		/* DELEGATE_F waits for a result */
		myrand = rand()%100;
		if(myrand<pushpercent){
			
			*push = 1;
			*val = i;
            **isElim = 0;
            **elimVal = 0;
			lock.DELEGATE_F(cs,*push,*val,*isElim,*elimVal);
		}
		else{
			*push = 0;
			*val = 0;
            **isElim = 0;
            **elimVal = 0;
            //std::cout << "HERE\n";
			auto returned_value = lock.DELEGATE_F(cs,*push,*val,*isElim,*elimVal);
			myreturn = returned_value.get();
            //std::cout << "val passed: " << myreturn.value << "\n";
			//if(i<10)
			//	std::cout << myreturn.value<<"\n";
			//returned_value.wait();
		}
		if(i%10000){
			//std::cout << ".";
			//fflush(stdout);
		}
	}
}

/* an empty function can be used to wait for previously delegated sections:
 * when the (empty) function is executed, everything preceding it is also done.
 */
void empty() {
}

int main(int argc, char* argv[]) {
	if(argc!=4){
		std::cout<<"Usage: <out filename> <Elimination array length> <Push percentage> <Number of iterations>";
		exit(0);
	}
	
	int elimarray_len = atoi(argv[1]);
	pushpercent = atoi(argv[2]);
	iterations = atoi(argv[3]);
	
	std::cout << THREADS << " threads / QD locking\n";
	lock.initArray(elimarray_len);
	cs_init();
	std::array<std::thread, THREADS> ts;
	for(auto& t : ts) {
		t = std::thread(&call_cs);
	}
	for(auto& t : ts) {
		t.join();
	}
	/* the empty function is used as a marker:
	 * all preceding sections are executed before it */
	auto b = lock.DELEGATE_F(&empty);
	b.wait(); /* wait for the call to empty() */

	cs_finish();
}

