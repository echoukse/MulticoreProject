% Authors:      Esha Choukse (ec27876)
%               Mike Thomson (mt29253)
% Date:         2014-11-20
% Course:       Multicore computing
% Description:  Matlab script for parsing and plotting results.


MT = 'MonitorT';
QD = 'QD C++ Library';
MT_wo = 'MonitorT without elimination';
QD_wo = 'QD C++ Library without elimination';
progs = {MT, QD};
progs_wo = {MT_wo, QD_wo};

langs = {'Java', 'C++'};
java = 1;
cpp = 2;

%num_threads = {'4'};
num_threads = {'1', '2', '4', '8'};
%qd_size = {'128'};
qd_size = {'128', '256', '512', '1024'};
elarray_size = {'4'};
%elarray_size = {'2', '4', '8', '16'};
pct_push = {'50'};
%pct_push = {'50', '60', '70', '80', '90'};

% Read all the stats, indexed by stat name.
stats = readtable(['output/data.csv']);
all_stats = containers.Map('KeyType','char','UniformValues',false);
all_stats = containers.Map(stats{:,1}, stats{:,2});

%% Plots:

%======================================================================
%======================================================================
% 01
% Baseline: C++ over Java (no elimination arrays). Q size = 256, push 50%
% y = Java time, C++ time
% x = num threads
plotnum = '00';
disp(['PLOT ' plotnum]);
x = [];
yjava = [];
ycpp = [];

q = '256';
e = '0';
p = '50';
for i = 1:length(num_threads)
    t = num_threads{i};

    x(i) = str2num(t);
    disp(['MT_t' t '_q' q '_e' e '_p' p '=' num2str(all_stats(['MT_t' t '_q' q '_e' e '_p' p '']))]);
    if i == 1
        yjava = [all_stats(['MT_t' t '_q' q '_e0_p' p ''])]; % Java
        ycpp = [all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    else
        yjava = [yjava, all_stats(['MT_t' t '_q' q '_e0_p' p ''])]; % Java
        ycpp = [ycpp, all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    end
end
y = [yjava; ycpp];

figure;
plot(x,yjava,'-o',x,ycpp,'-o');
leg = legend({MT_wo, QD_wo}, 'Location', 'northwest');
set(leg,'FontSize',16, 'Interpreter', 'latex');
set(gca, 'FontSize',14);
xlabel('Number of threads','FontSize',16, 'Interpreter', 'latex');
ylabel('Run time (ms)','FontSize',16, 'Interpreter', 'latex');
print_fname = ['../report/figs/' plotnum '_TimeVsThreads_cppNoElim_javaNoElim'];
print('-r600', '-depsc', print_fname); % .eps (better for LaTeX)
%print('-r600', '-dpng', print_fname); % .png


%======================================================================
%======================================================================
% Baseline: C++ over Java (no elimination arrays). threads = 4, push 50%
% y = Java time, C++ time
% x = queue size
plotnum = '01';
disp(['PLOT ' plotnum]);
x = [];
yjava = [];
ycpp = [];

t = '4';
e = '0';
p = '50';
for i = 1:length(qd_size)
    q = qd_size{i};
    x(i) = str2num(q);

    disp(['MT_t' t '_q' q '_e' e '_p' p '=' num2str(all_stats(['MT_t' t '_q' q '_e' e '_p' p '']))]);
    if i == 1
        yjava = [all_stats(['MT_t' t '_q' q '_e0_p' p ''])]; % Java
        ycpp = [all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    else
        yjava = [yjava, all_stats(['MT_t' t '_q' q '_e0_p' p ''])]; % Java
        ycpp = [ycpp, all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    end
end
y = [yjava; ycpp];

figure;
plot(x,yjava,'-o',x,ycpp,'-o');
leg = legend({MT_wo, QD_wo}, 'Location', 'northwest');
set(leg,'FontSize',16, 'Interpreter', 'latex');
set(gca, 'FontSize',14);
xlabel('Delegation queue size','FontSize',16, 'Interpreter', 'latex');
ylabel('Run time (ms)','FontSize',16, 'Interpreter', 'latex');
print_fname = ['../report/figs/' plotnum '_TimeVsQDsize_cppNoElim_javaNoElim'];
print('-r600', '-depsc', print_fname); % .eps (better for LaTeX)

%======================================================================
%======================================================================
% C++ over Java (C++ only has elimination array). Elim size = 4, Q size = 256, push 50%
% y = Java time, C++ time
% x = num threads
plotnum = '02';
disp(['PLOT ' plotnum]);
x = [];
yjava = [];
ycpp = [];

q = '256';
e = '4';
p = '50';
for i = 1:length(num_threads)
    t = num_threads{i};

    x(i) = str2num(t);
    disp(['MT_t' t '_q' q '_e' e '_p' p '=' num2str(all_stats(['QD_t' t '_q' q '_e' e '_p' p '']))]);
    if i == 1
        yjava = [all_stats(['MT_t' t '_q' q '_e0_p' p ''])]; % Java
        ycpp = [all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    else
        yjava = [yjava, all_stats(['MT_t' t '_q' q '_e0_p' p ''])]; % Java
        ycpp = [ycpp, all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    end
end
y = [yjava; ycpp];

figure;
plot(x,yjava,'-o',x,ycpp,'-o');
leg = legend({MT_wo, QD}, 'Location', 'northwest');
set(leg,'FontSize',16, 'Interpreter', 'latex');
set(gca, 'FontSize',14);
xlabel('Number of threads','FontSize',16, 'Interpreter', 'latex');
ylabel('Run time (ms)','FontSize',16, 'Interpreter', 'latex');
print_fname = ['../report/figs/' plotnum '_TimeVsThreads_cppElim_javaNoElim'];
print('-r600', '-depsc', print_fname); % .eps (better for LaTeX)

%======================================================================
%======================================================================
% C++ w/ elim array over C++ no elim array.
% y = (C++ time w/o elim) / (C++ time w/ elim)
% x = num threads
plotnum = '03';

%======================================================================
%======================================================================
% Speedup C++ w/ elim array vs C++ no elim array.
% y = (C++ time w/o elim) / (C++ time w/ elim)
% x = elim array size
plotnum = '04';

%======================================================================
%======================================================================
% Speedup C++ w/ elim array vs C++ no elim array.
% y = (C++ time w/o elim) / (C++ time w/ elim)
% x = delegation queue size
plotnum = '05';

%======================================================================
%======================================================================
% Time vs push percentages (use thread count = 4)
% y = C++ time w/ elim
% x = push %
plotnum = '06';

%===========================
%======================================================================
%======================================================================
% Queue size = 128
% Time vs. thread count, multiple line are elim array size
% y = time
% x = thread count, elim size
plotnum = '07a';

%======================================================================
%======================================================================
% Queue size = 256
% Time vs. thread count, multiple line are elim array size
% y = time
% x = thread count, elim size
plotnum = '07b';

%======================================================================
%======================================================================
% Queue size = 512
% Time vs. thread count, multiple line are elim array size
% y = time
% x = thread count, elim size
plotnum = '07c';

%======================================================================
%======================================================================
% Queue size = 1024
% Time vs. thread count, multiple line are elim array size
% y = time
% x = thread count, elim size
plotnum = '07d';
%===========================

%===========================
%======================================================================
%======================================================================
% Time vs queue size, multiple lines are num threads (elim size = 4)
% y = time
% x = queue size, thread count
plotnum = '08a';

%======================================================================
%======================================================================
% Time vs elim size, multiple lines are num threads (queue size = 128)
% y = time
% x = elim size, thread count
plotnum = '08b';
%===========================

