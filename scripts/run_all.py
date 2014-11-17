#!/usr/bin/python
'''
Authors:        Esha Choukse (ec27876)
                Mike Thomson (mt29253)
Date:           2014-11-20
Course:         Multicore Computing
Description:    Queue delegation with elimation test.
'''

import sys, os, subprocess, argparse, re

#=====================================================================
# Set stuff up.

num_iterations = 1000000
#num_threads = {1, 2, 4, 8}
num_threads = {4}
qd_size = {128}
#qd_size = {128, 256, 512, 1024}
elarray_size = {4}
#elarray_size = {2, 4, 8, 16}
pct_push = {50}
#pct_push = {50, 60, 70, 80, 90}

orig_dir = os.path.abspath('.')
QD_dir = os.path.abspath('../examples/stack/')
MT_dir = os.path.abspath('../monitor/build/')
output_dir = os.path.abspath(os.path.join(orig_dir, 'output'))
data_file = os.path.join(output_dir, 'data.csv')

data = {}

if not os.path.exists(output_dir):
    os.makedirs(output_dir)

execs = []
for thr in num_threads:
    for qds in qd_size:
        for elsize in elarray_size:
            for pct in pct_push:
                execs.append({'thr': thr, 'qds': qds, 'el': elsize, 'pct': pct, 'cmdQD': "./counter_qd{}_{} {} {} {}".format(thr, qds, elsize, pct, num_iterations), 'cmdMT': "java -cp ../dist/lib/ActiveMonitor-20141116.jar:. examples/Stack/WarmUpRandomTest {} {} {} 0 {}".format(num_iterations, thr, pct, qds)})

#=====================================================================
# Run "make" for all of the DQ C++ options.
def do_make(cmd):
    ps = subprocess.Popen(cmd, shell=True, preexec_fn=os.setsid)
    ps.wait()

#=====================================================================
# Run a command.
def do_run(cmd, fout_name):
    with open(os.path.join(output_dir, fout_name),'w') as fout:
        ps = subprocess.Popen(cmd, shell=True, stdout=fout, preexec_fn=os.setsid)
        ps.wait()
        fout.flush()

#=====================================================================
# Iterate through all of the programs.
for exe in execs:
    os.chdir(QD_dir)
    cmd = exe['cmdQD']
    fout_name = 'QDout_t{}_q{}_e{}_p{}.txt'.format(exe['thr'], exe['qds'], exe['el'], exe['pct'])
    do_make('make counter_qd{}_{}'.format(exe['thr'], exe['qds']))
    do_run(cmd, fout_name)

    os.chdir(MT_dir)
    cmd = exe['cmdMT']
    fout_name = 'MTout_t{}_q{}_e{}_p{}.txt'.format(exe['thr'], exe['qds'], exe['el'], exe['pct'])
    do_run(cmd, fout_name)

os.chdir(orig_dir)
#=====================================================================
# Parse data.
for exe in execs:
    fin_name = 'QDout_t{}_q{}_e{}_p{}.txt'.format(exe['thr'], exe['qds'], exe['el'], exe['pct'])
    with open(os.path.join(output_dir, fin_name), 'r') as fin:
        lines = fin.readlines()
        # Match groups:              (   1  )
        m = re.match(r'^time needed: ([0-9]*) .*', lines[1])
        if m:
            key = 'QD_t{}_q{}_e{}_p{}'.format(exe['thr'], exe['qds'], exe['el'], exe['pct'])
            data[key] = m.group(1)
        else:
            print 'Error with %' % key

    fin_name = 'MTout_t{}_q{}_e{}_p{}.txt'.format(exe['thr'], exe['qds'], exe['el'], exe['pct'])
    with open(os.path.join(output_dir, fin_name), 'r') as fin:
        line = fin.readline()
        # Match groups: (   1  )
        m = re.match(r'^([0-9]*).*', line)
        if m:
            key = 'MT_t{}_q{}_e{}_p{}'.format(exe['thr'], exe['qds'], exe['el'], exe['pct'])
            data[key] = m.group(1)
        else:
            print 'Error with %' % key

#=====================================================================
# Save data to file.
with open(data_file, 'w') as fdata:
    fdata.write('run,time\n')
    for key, d in data.items():
        fdata.write('{},{}\n'.format(key,d))

#=====================================================================
# Plot results.
#cmd = '/usr/local/MATLAB/R2013b/bin/matlab -nosplash -nodesktop -r "run(\'plot_results.m\');exit;"'
#do_run(cmd, os.path.join(output_dir, 'matlab_console.txt'))
