#include<chrono>
#include<iostream>
#include "cs.hpp"

static int shared_counter;
static std::chrono::time_point<std::chrono::high_resolution_clock> start_time;

  
stack_node_ptr head;
void cs_init() {
	start_time = std::chrono::high_resolution_clock::now();
	stack_node_ptr stack_sentinel;
	stack_sentinel = new stack_node;
	stack_sentinel->value = 0;
	stack_sentinel->next = NULL;
	
	head = stack_sentinel;
}

retval cs(int push, int val) {
	retval return_struct;
	if(push){
		stack_node_ptr my_node;
		my_node = new stack_node;
		my_node->value = val;
		my_node->next = head;
		head = my_node;
	}
	else{
		if(head->next == NULL){
			return_struct.isempty = 1;
			return return_struct;	
		}
		return_struct.isempty = 0;
		return_struct.value = head->value;
		stack_node_ptr temp;
		temp = head;
		head = head->next;
		free(temp);				
	}
	return return_struct;
}

void cs_finish() {
	retval myreturn;
	auto end_time = std::chrono::high_resolution_clock::now();
	auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end_time - start_time);

	std::cout << "time needed: " << duration.count() << " ms" << std::endl;
	do{
		myreturn = cs(0,0);
	} while(!myreturn.isempty);
}

