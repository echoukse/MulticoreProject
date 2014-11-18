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
#num_threads = {4}
num_threads = {1, 2, 4, 6, 8}
#qd_size = {128}
qd_size = {128, 256, 512, 1024, 2048}
#elarray_size = {4}
elarray_size = {2, 4, 8, 16}
#pct_push = {50}
pct_push = {50, 60, 70, 80, 90}

orig_dir    = os.path.abspath('.')
QD_dir      = os.path.abspath('../qd_library_withelimination/examples/stack/')
QD_noEA_dir = os.path.abspath('../qd_library-master_withoutelimination/examples/stack/')
MT_dir      = os.path.abspath('../monitor/build/')
output_dir  = os.path.abspath(os.path.join(orig_dir, 'output'))
data_file   = os.path.join(output_dir, 'data.csv')
matlab_path = '/usr/local/MATLAB/R2013b/bin/matlab'

data = {}

if not os.path.exists(output_dir):
    os.makedirs(output_dir)

execs = []
execs_noElArray = []
for thr in num_threads:
    for qds in qd_size:
        for pct in pct_push:
            # Without elimination array:
            execs_noElArray.append({'thr': thr, 'qds': qds, 'pct': pct, 'cmdQD_noEA': "./counter_qd{}_{} 0 {} {}".format(thr, qds, pct, num_iterations), 'cmdMT': "java -cp ../dist/lib/ActiveMonitor-20141116.jar:. examples/Stack/WarmUpRandomTest {} {} {} 0 {}".format(num_iterations, thr, pct, qds)})

            # With elimination array:
            for elsize in elarray_size:
                execs.append({'thr': thr, 'qds': qds, 'el': elsize, 'pct': pct, 'cmdQD': "./counter_qd{}_{} {} {} {}".format(thr, qds, elsize, pct, num_iterations)})

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
# Make all of the C++ programs.
def make_all():
    for exe in execs:
        os.chdir(QD_dir)
        do_make('make counter_qd{}_{}'.format(exe['thr'], exe['qds']))

    for exe in execs_noElArray:
        os.chdir(QD_noEA_dir)
        do_make('make counter_qd{}_{}'.format(exe['thr'], exe['qds']))

    os.chdir(orig_dir)

#=====================================================================
# Iterate through all of the programs.
def exe_all():
    for exe in execs:
        os.chdir(QD_dir)
        cmd = exe['cmdQD']
        fout_name = 'QD_t{}_q{}_e{}_p{}.txt'.format(exe['thr'], exe['qds'], exe['el'], exe['pct'])
        print fout_name
        #do_make('make counter_qd{}_{}'.format(exe['thr'], exe['qds']))
        do_run(cmd, fout_name)

    for exe in execs_noElArray:
        os.chdir(QD_noEA_dir)
        cmd = exe['cmdQD_noEA']
        fout_name = 'QD_t{}_q{}_e0_p{}.txt'.format(exe['thr'], exe['qds'], exe['pct'])
        print fout_name
        #do_make('make counter_qd{}_{}'.format(exe['thr'], exe['qds']))
        do_run(cmd, fout_name)

        os.chdir(MT_dir)
        cmd = exe['cmdMT']
        fout_name = 'MT_t{}_q{}_e0_p{}.txt'.format(exe['thr'], exe['qds'], exe['pct'])
        print fout_name
        do_run(cmd, fout_name)

    os.chdir(orig_dir)
#=====================================================================
# Parse data.
def parse_data():
    for exe in execs:
        # QD with elimination array.
        fin_name = 'QD_t{}_q{}_e{}_p{}.txt'.format(exe['thr'], exe['qds'], exe['el'], exe['pct'])
        with open(os.path.join(output_dir, fin_name), 'r') as fin:
            lines = fin.readlines()
            # Match groups:              (   1  )
            m = re.match(r'^time needed: ([0-9]*) .*', lines[1])
            if m:
                key = 'QD_t{}_q{}_e{}_p{}'.format(exe['thr'], exe['qds'], exe['el'], exe['pct'])
                data[key] = m.group(1)
            else:
                print 'Error with %' % key

    for exe in execs_noElArray:
        # QD without elimination array.
        fin_name = 'QD_t{}_q{}_e0_p{}.txt'.format(exe['thr'], exe['qds'], exe['pct'])
        with open(os.path.join(output_dir, fin_name), 'r') as fin:
            lines = fin.readlines()
            # Match groups:              (   1  )
            m = re.match(r'^time needed: ([0-9]*) .*', lines[1])
            if m:
                key = 'QD_t{}_q{}_e0_p{}'.format(exe['thr'], exe['qds'], exe['pct'])
                data[key] = m.group(1)
            else:
                print 'Error with %' % key

        # MonitorT
        fin_name = 'MT_t{}_q{}_e0_p{}.txt'.format(exe['thr'], exe['qds'], exe['pct'])
        with open(os.path.join(output_dir, fin_name), 'r') as fin:
            line = fin.readline()
            # Match groups: (   1  )
            m = re.match(r'^([0-9]*).*', line)
            if m:
                key = 'MT_t{}_q{}_e0_p{}'.format(exe['thr'], exe['qds'], exe['pct'])
                data[key] = m.group(1)
            else:
                print 'Error with %' % key

    # Save data to file.
    with open(data_file, 'w') as fdata:
        fdata.write('run,time\n')
        for key, d in data.items():
            fdata.write('{},{}\n'.format(key,d))

#=====================================================================
# Plot results.
def plot():
    cmd = '{} -nosplash -nodesktop -r "run(\'plot_results.m\');exit;"'.format(matlab_path)
    do_run(cmd, os.path.join(output_dir, 'matlab_console.txt'))

#=====================================================================
arg_parser = argparse.ArgumentParser(description = 'Run the various parts of the project.')
arg_parser.add_argument('-m', '--make', action='store_true',
        help='Make all of the C++ files.')
arg_parser.add_argument('-e', '--execute', action='store_true',
        help='Execute all of the tests, automatically saving output data file per test.')
arg_parser.add_argument('-d', '--data', action='store_true',
        help='Parse the data from the executed tests, and save to a unified .csv file.')
arg_parser.add_argument('-p', '--plots', action='store_true',
        help='Generate the plots (using MATLAB) from the parsed data .csv file.')
arg_parser.add_argument('-a', '--all', action='store_true',
        help='Run everything: Make, Execute, Parse/save and generate plots.')

args = arg_parser.parse_args()

if args.make or args.all:
    make_all()

if args.execute or args.all:
    exe_all()

if args.data or args.all:
    parse_data()

if args.plots or args.all:
    plot()

