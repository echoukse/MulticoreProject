#ifndef qd_qd_hpp
#define qd_qd_hpp qd_qd_hpp

#include "locks/tatas_lock.hpp"
//#include "locks/pthreads_lock.hpp"
#include "locks/mutex_lock.hpp"

#include "queues/buffer_queue.hpp"
#include "queues/simple_locked_queue.hpp"

#include "qdlock.hpp"
#include "mrqdlock.hpp"

#include "qd_condition_variable.hpp"


using qdlock = qdlock_impl<tatas_lock, buffer_queue<DELEGATIONQLEN>>;
using mrqdlock = mrqdlock_impl<tatas_lock, buffer_queue<16384>, reader_groups<64>, 65536>;
using qd_condition_variable = qd_condition_variable_impl<mutex_lock, simple_locked_queue>;

#define DELEGATE_F(function, ...) template delegate_f<decltype(function), function>(__VA_ARGS__)
#define DELEGATE_N(function, ...) template delegate_n<decltype(function), function>(__VA_ARGS__)
#define DELEGATE_P(function, ...) template delegate_p<decltype(function), function>(__VA_ARGS__)
#define DELEGATE_FP(function, ...) template delegate_fp<decltype(function), function>(__VA_ARGS__)
#define WAIT_REDELEGATE_P(function, ...) template wait_redelegate_p<decltype(function), function>(__VA_ARGS__)

#endif /* qd_qd_hpp */
