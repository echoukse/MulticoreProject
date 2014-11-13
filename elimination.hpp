#include<array>
#include<iostream>
#include<thread>
#include <atomic>

#define EMPTY 0
#define PUSH_WAITING 1
#define POP_WAITING  2
#define BUSY  3
#define ELMSUCCESS 1
#define ELMFAIL 0

struct slot
{
 int data;
 int status; 
};

class Exchanger
{
 std::atomic<slot> myslot;
 int isPush;
 public:
 
 int mynumber;
 Exchanger *next;
 
 Exchanger(int mydata){
   slot temp;
   temp.data = mydata;
   temp.status = EMPTY;
   myslot.store(temp);
   this->next = NULL;
 } 
 
 int visit(int visitPush, int value, int timeout){
   int loops = 0, retval = ELMFAIL;
   slot readslot, newslot;
   newslot.data = value;
   while(loops<timeout){
     readslot = myslot.load(std::memory_order_relaxed);
     switch(readslot.status){
       case EMPTY:
       //PUSH gets EMPTY
         if(visitPush){
           //Try to set to PUSH_WAITING
           newslot.status = PUSH_WAITING;
           if(myslot.compare_exchange_strong(readslot,newslot)){
           //The myslot is now in PUSH_WAITING
             std::cout<<"Made it waiting, I was Push!"<<mynumber<<" \n";
             while(loops<2*timeout){
               readslot = myslot.load(std::memory_order_relaxed);
               if(readslot.status == BUSY){
                 std::cout<<"Match made, I was Push! "<<mynumber<<"\n";
                 retval = ELMSUCCESS;
                 break;
               }
               loops++;
             }
             newslot.status = EMPTY;
             if(!myslot.compare_exchange_strong(readslot,newslot)){
               //This happens when after timeout we read a BUSY, so now we got ELMSUCCESS
               retval = ELMSUCCESS;
               myslot.compare_exchange_strong(readslot,newslot);
               std::cout<<"Match made, I was Push! "<<mynumber<<"\n";
             }
             return retval;             
           }
         }
        else{
          //Try to set to POP_WAITING
           newslot.status = POP_WAITING;
           if(myslot.compare_exchange_strong(readslot,newslot)){
           //The myslot is now in POP_WAITING
             std::cout<<"Made it waiting, I was Pop! "<<mynumber<<"\n";
             while(loops<2*timeout){
               readslot = myslot.load(std::memory_order_relaxed);
               if(readslot.status == BUSY){
                 std::cout<<"Match made, I was Pop!"<<mynumber<<" \n";
                 retval = readslot.data;
                 break;
               }
               loops++;
             }
             newslot.status = EMPTY;
             if(!myslot.compare_exchange_strong(readslot,newslot)){
               //This happens when after timeout we read a BUSY, so now we got ELMSUCCESS
               retval = readslot.data;
               myslot.compare_exchange_strong(readslot,newslot);
               std::cout<<"Match made, I was Pop! "<<mynumber<<"\n";
             }
             return retval;             
           }          
        }
        break;
      case PUSH_WAITING:
        if(isPush){
          if(loops == 1)
            std::cout<<"Push tried to meet push!"<<mynumber<<" \n";
          continue;
          }
        else{
          newslot.status = BUSY;
          if(myslot.compare_exchange_strong(readslot,newslot)){
            std::cout<<"Set to busy, I was Pop!"<<mynumber<<" \n";
            return readslot.data;
          }
        }
        break;
      case POP_WAITING:
        if(isPush){
          newslot.status = BUSY;
          
          if(myslot.compare_exchange_strong(readslot,newslot)){
            std::cout<<"Set to busy, I was Push! "<<mynumber<<"\n";
            return ELMSUCCESS;
          }
        }
        else{
          if(loops == 1)
            std::cout<<"Pop tried to meet pop! "<<mynumber<<"\n";
        }
        break;
      case BUSY:
        if(loops == 1)
          std::cout<<"Slot was busy!"<<mynumber<<" \n";
        break;
      default: 
        break;      
     }
     loops++;
   }
   return ELMFAIL;
 }
};

class EliminationArray{
  public:
  int capacity;
  Exchanger *ExArray;
  
  
  
  EliminationArray(int mycapacity){
    Exchanger *temp1,*temp2;
    this->capacity = mycapacity;
    ExArray = new Exchanger(0);
    temp1= ExArray;
    temp1->mynumber = 0;
    
    for(int i=1;i<mycapacity;i++){
      temp2 = new Exchanger(0);
      temp2->mynumber = i;
      temp1->next = temp2;
      temp1 = temp1->next;
      
    }
  }
  
  int visit(int isPush, int value, int timeout){
    int slotnumber;
    slotnumber = rand() % capacity;
    Exchanger *temp;
    
    //printf("Reached for statement in ElArray \n");
    temp = ExArray;
    //std::cout<<"Slotnumber: "<<slotnumber<<"\n";
    
    for(int i=1; i<=slotnumber; i++){
      temp = temp->next;
    }
    //printf("Reached return statement in ElArray \n");
    return temp->visit(isPush, value, timeout);
  }
};
