#include <atomic>
template<typename T>

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
 Exchanger *next;
 public:

 void Exchanger(int mydata){
   this.myslot->data = mydata;
   this.myslot->status = EMPTY; 
   this.next = NULL;
 } 
 
 int Exchange_visit(int visitPush, int value, int timeout){
   int loops = 0, retval =ELMFAIL;
   slot readslot, newslot;
   newslot.data = value;
   while(loops<timeout){
     readslot = myslot.load(std::memory_order_relaxed);
     switch(readslot->status){
       case EMPTY:
       //PUSH gets EMPTY
         if(visitPUSH){
           //Try to set to PUSH_WAITING
           newslot.status = PUSH_WAITING;
           if(myslot.compare_exchange_strong(readslot,newslot)){
           //The myslot is now in PUSH_WAITING
             while(loops<timeout){
               readslot = myslot.load(std::memory_order_relaxed);
               if(readslot.status == BUSY){
                 retval = ELMSUCCESS;
                 break;
               }
               loops++;
             }
             newslot.status = EMPTY;
             if(!myslot.compare_exchange_strong(readslot,newslot)){
               //This happens when after timeout we read a BUSY, so now we got SUCCESS
               retval = ELMSUCCESS;
               myslot.compare_exchange_strong(readslot,newslot);
             }
             return retval;             
           }
         }
        else{
          //Try to set to POP_WAITING
           newslot.status = POP_WAITING;
           if(myslot.compare_exchange_strong(readslot,newslot)){
           //The myslot is now in POP_WAITING
             while(loops<timeout){
               readslot = myslot.load(std::memory_order_relaxed);
               if(readslot.status == BUSY){
                 retval = readslot.data;
                 break;
               }
               loops++;
             }
             newslot.status = EMPTY;
             if(!myslot.compare_exchange_strong(readslot,newslot)){
               //This happens when after timeout we read a BUSY, so now we got SUCCESS
               retval = readslot.data;
               myslot.compare_exchange_strong(readslot,newslot);
             }
             return retval;             
           }          
        }
        break;
      case PUSH_WAITING:
        if(isPush)
          continue;
        else{
          newslot.status = BUSY;
          if(myslot.compare_exchange_strong(readslot,newslot)){
            return readslot.data;
          }
        }
        break;
      case POP_WAITING:
        if(isPush){
          newslot.status = BUSY;
          if(myslot.compare_exchange_strong(readslot,newslot)){
            return ELMSUCCESS;
          }
        }
        break;
      case BUSY:
        break;
      default: 
        break;      
     }
     loops++;
   }
 }
}

class EliminationArray{
  int capacity;
  Exchanger *ExArray;
  
  public:
  
  void EliminationArray(int mycapacity){
    Exchanger *temp1,*temp2;
    this.capacity = mycapacity;
    ExArray = new Exchanger(0);
    temp1= ExArray;
    
    for(int i=1;i<mycapacity;i++){
      temp2 = new Exchanger(0);
      temp1->next = temp2;
      temp1 = temp1->next;
    }
  }
  
  int visit(int isPush, int value, int timeout){
    int slotnumber;
    slotnumber = rand() % capacity;
    Exchanger *temp;
    
    temp = ExArray;
    std::cout<<"Slotnumber: "<<slotnumber<<"\n";
    
    for(int i=1; i<=slotnumber; i++){
      temp = temp->next;
    }
    
    return temp->Exchange_visit(isPush, value, timeout);
  }
}
