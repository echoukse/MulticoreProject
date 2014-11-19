TO RUN:

1) Compile monitor:
cd monitor
ant test

2) Compile QD Libraries:
cd scripts
./run_all.py -m

3) Run tests. Full run takes about 30 minutes on my laptop. If you want to reduce the number of tests run, then modify the scripts/run_all.py script variables "num_threads", "qd_size", "elarray_size" and "pct_push" to have fewer options to iterate through. After selecting the ranges of parameters to test, run them all with:
cd scripts
./run_all.py -e

4) Parse the data and save to a .csv file. Before running this step, you should make sure that your output files (located in scripts/output) contain the resulting data (run time in milliseconds). For the MT results, the files will only contain a number. For the QD results, the files will contain additional text, along with the run time in the format "time needed: ### ms" on the second line of the file. To parse and save as .csv, run:
cd scripts
./run_all.py -d

5) Plot the data. This assumes you have MATLAB installed at "/usr/local/MATLAB/R2013b/bin/matlab". If your installation is somewhere else, then modify the "matlab_path" variable at the top of the run_all.py script. The script assumes you have a folder named "report", and it will automatically save the figures into "report/figs/". To plot and save the plots, run:
cd scripts
./run_all.py -p

