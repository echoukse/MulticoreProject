#ifndef cs_hpp
#define cs_hpp cs_hpp

typedef struct NODE{
int value;
NODE* next;
} stack_node, *stack_node_ptr;

typedef struct RETVAL{
int value;
int isempty;
} retval;
void cs_init();
retval cs(int push, int val, int *isElim, int *elimVal);
void cs_finish();

#endif // cs_hpp
