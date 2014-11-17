#include<array>
#include<iostream>
#include<thread>
#include "cs.hpp"
#include "qd.hpp"

static qdlock lock;
static int pushpercent, iterations;

void call_cs() {
	int *push, *val, myrand;
	retval myreturn;
	push = new int;
	val = new int;
	for(int i = 0; i < iterations/THREADS; i++) {
		/* DELEGATE_F waits for a result */
		myrand = rand()%100;
		if(myrand<pushpercent){
			
			*push = 1;
			*val = i;
			lock.DELEGATE_F(cs,*push,*val);			
		}
		else{
			*push = 0;
			*val = 0;
			auto returned_value = lock.DELEGATE_F(cs,*push,*val);			
			myreturn = returned_value.get();
			//if(i<10)
				//std::cout << myreturn.value<<"\n";
			//returned_value.wait();
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

