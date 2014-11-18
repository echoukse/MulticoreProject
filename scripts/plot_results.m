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
num_threads = {'1', '2', '4', '6', '8'};
%qd_size = {'128'};
qd_size = {'128', '256', '512', '1024', '2048'};
%elarray_size = {'4'};
elarray_size = {'2', '4', '8', '16'};
%pct_push = {'50'};
pct_push = {'50', '60', '70', '80', '90'};

% Default values
t_def = '4';
e_def = '4';
q_def = '256';
p_def = '50';

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

% Default values
t = t_def;
e = '0';
q = q_def;
p = p_def;
for i = 1:length(num_threads)
    t = num_threads{i};
    x(i) = str2num(t);

    %disp(['MT_t' t '_q' q '_e' e '_p' p '=' num2str(all_stats(['MT_t' t '_q' q '_e' e '_p' p '']))]);
    if i == 1
        yjava = [all_stats(['MT_t' t '_q' q '_e0_p' p ''])]; % Java
        ycpp = [all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    else
        yjava = [yjava, all_stats(['MT_t' t '_q' q '_e0_p' p ''])]; % Java
        ycpp = [ycpp, all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    end
end
x = [x; x];
y = [yjava; ycpp];

figure;
plot(x',y','-o');
lims = ylim();
ylim([lims(1) (1.1*lims(2))]);
leg = legend({MT_wo, QD_wo}, 'Location', 'north');
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

% Default values
t = t_def;
e = '0';
q = q_def;
p = p_def;
for i = 1:length(qd_size)
    q = qd_size{i};
    x(i) = str2num(q);

    %disp(['MT_t' t '_q' q '_e' e '_p' p '=' num2str(all_stats(['MT_t' t '_q' q '_e' e '_p' p '']))]);
    if i == 1
        yjava = [all_stats(['MT_t' t '_q' q '_e0_p' p ''])]; % Java
        ycpp = [all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    else
        yjava = [yjava, all_stats(['MT_t' t '_q' q '_e0_p' p ''])]; % Java
        ycpp = [ycpp, all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    end
end
x = [x; x];
y = [yjava; ycpp];

figure;
plot(x',y','-o');
lims = ylim();
ylim([lims(1) (1.1*lims(2))]);
leg = legend({MT_wo, QD_wo}, 'Location', 'northeast');
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

% Default values
t = t_def;
e = e_def;
q = q_def;
p = p_def;
for i = 1:length(num_threads)
    t = num_threads{i};
    x(i) = str2num(t);

    %disp(['MT_t' t '_q' q '_e' e '_p' p '=' num2str(all_stats(['QD_t' t '_q' q '_e' e '_p' p '']))]);
    if i == 1
        yjava = [all_stats(['MT_t' t '_q' q '_e0_p' p ''])]; % Java
        ycpp = [all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    else
        yjava = [yjava, all_stats(['MT_t' t '_q' q '_e0_p' p ''])]; % Java
        ycpp = [ycpp, all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    end
end
x = [x; x];
y = [yjava; ycpp];

figure;
plot(x',y','-o');
lims = ylim();
ylim([lims(1) (1.1*lims(2))]);
leg = legend({MT_wo, QD}, 'Location', 'north');
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
disp(['PLOT ' plotnum]);
x = [];
ycpp_wo = [];
ycpp = [];

% Default values
t = t_def;
e = e_def;
q = q_def;
p = p_def;
for i = 1:length(num_threads)
    t = num_threads{i};
    x(i) = str2num(t);

    if i == 1
        ycpp_wo = [all_stats(['QD_t' t '_q' q '_e0_p' p ''])]; % C++
        ycpp = [all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    else
        ycpp_wo = [ycpp_wo, all_stats(['QD_t' t '_q' q '_e0_p' p ''])]; % C++
        ycpp = [ycpp, all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    end
end
x = [x; x];
y = [ycpp_wo; ycpp];

figure;
plot(x',y','-o');
lims = ylim();
ylim([lims(1) (1.1*lims(2))]);
leg = legend({QD_wo, QD}, 'Location', 'north');
set(leg,'FontSize',16, 'Interpreter', 'latex');
set(gca, 'FontSize',14);
xlabel('Number of threads','FontSize',16, 'Interpreter', 'latex');
ylabel('Run time (ms)','FontSize',16, 'Interpreter', 'latex');
print_fname = ['../report/figs/' plotnum '_TimeVsThreads_cppElim_cppNoElim'];
print('-r600', '-depsc', print_fname); % .eps (better for LaTeX)

%======================================================================
%======================================================================
% Speedup C++ w/ elim array vs C++ no elim array.
% y = (C++ time w/o elim) / (C++ time w/ elim)
% x = elim array size
plotnum = '04';
disp(['PLOT ' plotnum]);
x = [];
ycpp_wo = [];
ycpp = [];

% Default values
t = '8';
e = e_def;
q = q_def;
p = p_def;
for i = 1:length(elarray_size)
    e = elarray_size{i};
    x(i) = str2num(e);

    if i == 1
        ycpp_wo = [all_stats(['QD_t' t '_q' q '_e0_p' p ''])]; % C++
        ycpp = [all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    else
        ycpp_wo = [ycpp_wo, all_stats(['QD_t' t '_q' q '_e0_p' p ''])]; % C++
        ycpp = [ycpp, all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    end
end
x = [x; x];
y = [ycpp_wo; ycpp];

figure;
plot(x',y','-o');
lims = ylim();
ylim([lims(1) (1.1*lims(2))]);
leg = legend({QD_wo, QD}, 'Location', 'northeast');
set(leg,'FontSize',16, 'Interpreter', 'latex');
set(gca, 'FontSize',14);
xlabel('Elimination array size','FontSize',16, 'Interpreter', 'latex');
ylabel('Run time (ms)','FontSize',16, 'Interpreter', 'latex');
print_fname = ['../report/figs/' plotnum '_TimeVsElsize_cppElim_cppNoElim'];
print('-r600', '-depsc', print_fname); % .eps (better for LaTeX)

%======================================================================
%======================================================================
% Speedup C++ w/ elim array vs C++ no elim array.
% y = (C++ time w/o elim) / (C++ time w/ elim)
% x = delegation queue size
plotnum = '05';
disp(['PLOT ' plotnum]);
x = [];
ycpp_wo = [];
ycpp = [];

% Default values
t = t_def;
e = e_def;
q = q_def;
p = p_def;
for i = 1:length(qd_size)
    q = qd_size{i};
    x(i) = str2num(q);

    if i == 1
        ycpp_wo = [all_stats(['QD_t' t '_q' q '_e0_p' p ''])]; % C++
        ycpp = [all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    else
        ycpp_wo = [ycpp_wo, all_stats(['QD_t' t '_q' q '_e0_p' p ''])]; % C++
        ycpp = [ycpp, all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    end
end
x = [x; x];
y = [ycpp_wo; ycpp];

figure;
plot(x',y','-o');
lims = ylim();
ylim([lims(1) (1.1*lims(2))]);
leg = legend({QD_wo, QD}, 'Location', 'northeast');
set(leg,'FontSize',16, 'Interpreter', 'latex');
set(gca, 'FontSize',14);
xlabel('Delegation queue size','FontSize',16, 'Interpreter', 'latex');
ylabel('Run time (ms)','FontSize',16, 'Interpreter', 'latex');
print_fname = ['../report/figs/' plotnum '_TimeVsQDsize_cppElim_cppNoElim'];
print('-r600', '-depsc', print_fname); % .eps (better for LaTeX)

%======================================================================
%======================================================================
% Time vs push percentages (use thread count = 4)
% y = C++ time w/ elim
% x = push %
plotnum = '06';
disp(['PLOT ' plotnum]);
x = [];
ycpp_wo = [];
ycpp = [];

% Default values
t = t_def;
e = e_def;
q = q_def;
p = p_def;
for i = 1:length(pct_push)
    p = pct_push{i};
    x(i) = str2num(p);

    if i == 1
        ycpp_wo = [all_stats(['QD_t' t '_q' q '_e0_p' p ''])]; % C++
        ycpp = [all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    else
        ycpp_wo = [ycpp_wo, all_stats(['QD_t' t '_q' q '_e0_p' p ''])]; % C++
        ycpp = [ycpp, all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
    end
end
x = [x; x];
y = [ycpp_wo; ycpp];

figure;
plot((x')./100,y','-o');
lims = ylim();
ylim([lims(1) (1.1*lims(2))]);
leg = legend({QD_wo, QD}, 'Location', 'northeast');
set(leg,'FontSize',16, 'Interpreter', 'latex');
set(gca, 'FontSize',14);
xlabel('Percentage of pushes','FontSize',16, 'Interpreter', 'latex');
ylabel('Run time (ms)','FontSize',16, 'Interpreter', 'latex');
print_fname = ['../report/figs/' plotnum '_TimeVsPctPush_cppElim_cppNoElim'];
print('-r600', '-depsc', print_fname); % .eps (better for LaTeX)

%===========================
%======================================================================
%======================================================================
% Queue size = 128
% Time vs. thread count, multiple line are elim array size
% y = time
% x = thread count, elim size
plotnum = '07a';
disp(['PLOT ' plotnum]);
x = [];
xtmp = [];
y = [];
ytmp = [];
plot_legend = {};

% Default values
t = t_def;
e = e_def;
q = '128';
p = p_def;
for i = 1:length(num_threads)
    t = num_threads{i};

    for j = 1:length(elarray_size)
        e = elarray_size{j};

        if j == 1
            xtmp = [str2num(t)];
            ytmp = [all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
        else
            xtmp = [xtmp str2num(t)];
            ytmp = [ytmp, all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
        end

        if i == 1
            plot_legend = [plot_legend; 'Elim. array size: ' e];
        end
    end

    x(i,:) = xtmp;
    y(i,:) = ytmp;
end

figure;
plot(x,y,'-o');
lims = ylim();
ylim([lims(1) (1.1*lims(2))]);
leg = legend(plot_legend, 'Location', 'southeast');
set(leg,'FontSize',16, 'Interpreter', 'latex');
set(gca, 'FontSize',14);
xlabel('Thread count','FontSize',16, 'Interpreter', 'latex');
ylabel('Run time (ms)','FontSize',16, 'Interpreter', 'latex');
print_fname = ['../report/figs/' plotnum '_Q128_TimeVsThreadsVsEsize_cppElim'];
print('-r600', '-depsc', print_fname); % .eps (better for LaTeX)

%======================================================================
%======================================================================
% Queue size = 256
% Time vs. thread count, multiple line are elim array size
% y = time
% x = thread count, elim size
plotnum = '07b';
disp(['PLOT ' plotnum]);
x = [];
xtmp = [];
y = [];
ytmp = [];
plot_legend = {};

% Default values
t = t_def;
e = e_def;
q = '256';
p = p_def;
for i = 1:length(num_threads)
    t = num_threads{i};

    for j = 1:length(elarray_size)
        e = elarray_size{j};

        if j == 1
            xtmp = [str2num(t)];
            ytmp = [all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
        else
            xtmp = [xtmp str2num(t)];
            ytmp = [ytmp, all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
        end

        if i == 1
            plot_legend = [plot_legend; 'Elim. array size: ' e];
        end
    end

    x(i,:) = xtmp;
    y(i,:) = ytmp;
end

figure;
plot(x,y,'-o');
lims = ylim();
ylim([lims(1) (1.1*lims(2))]);
leg = legend(plot_legend, 'Location', 'northwest');
set(leg,'FontSize',16, 'Interpreter', 'latex');
set(gca, 'FontSize',14);
xlabel('Thread count','FontSize',16, 'Interpreter', 'latex');
ylabel('Run time (ms)','FontSize',16, 'Interpreter', 'latex');
print_fname = ['../report/figs/' plotnum '_Q256_TimeVsThreadsVsEsize_cppElim'];
print('-r600', '-depsc', print_fname); % .eps (better for LaTeX)

%======================================================================
%======================================================================
% Queue size = 512
% Time vs. thread count, multiple line are elim array size
% y = time
% x = thread count, elim size
plotnum = '07c';
disp(['PLOT ' plotnum]);
x = [];
xtmp = [];
y = [];
ytmp = [];
plot_legend = {};

% Default values
t = t_def;
e = e_def;
q = '512';
p = p_def;
for i = 1:length(num_threads)
    t = num_threads{i};

    for j = 1:length(elarray_size)
        e = elarray_size{j};

        if j == 1
            xtmp = [str2num(t)];
            ytmp = [all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
        else
            xtmp = [xtmp str2num(t)];
            ytmp = [ytmp, all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
        end

        if i == 1
            plot_legend = [plot_legend; 'Elim. array size: ' e];
        end
    end

    x(i,:) = xtmp;
    y(i,:) = ytmp;
end

figure;
plot(x,y,'-o');
lims = ylim();
ylim([lims(1) (1.1*lims(2))]);
leg = legend(plot_legend, 'Location', 'northwest');
set(leg,'FontSize',16, 'Interpreter', 'latex');
set(gca, 'FontSize',14);
xlabel('Thread count','FontSize',16, 'Interpreter', 'latex');
ylabel('Run time (ms)','FontSize',16, 'Interpreter', 'latex');
print_fname = ['../report/figs/' plotnum '_Q512_TimeVsThreadsVsEsize_cppElim'];
print('-r600', '-depsc', print_fname); % .eps (better for LaTeX)

%======================================================================
%======================================================================
% Queue size = 1024
% Time vs. thread count, multiple line are elim array size
% y = time
% x = thread count, elim size
plotnum = '07d';
disp(['PLOT ' plotnum]);
x = [];
xtmp = [];
y = [];
ytmp = [];
plot_legend = {};

% Default values
t = t_def;
e = e_def;
q = '1024';
p = p_def;
for i = 1:length(num_threads)
    t = num_threads{i};

    for j = 1:length(elarray_size)
        e = elarray_size{j};

        if j == 1
            xtmp = [str2num(t)];
            ytmp = [all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
        else
            xtmp = [xtmp str2num(t)];
            ytmp = [ytmp, all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
        end

        if i == 1
            plot_legend = [plot_legend; 'Elim. array size: ' e];
        end
    end

    x(i,:) = xtmp;
    y(i,:) = ytmp;
end

figure;
plot(x,y,'-o');
lims = ylim();
ylim([lims(1) (1.1*lims(2))]);
leg = legend(plot_legend, 'Location', 'northwest');
set(leg,'FontSize',16, 'Interpreter', 'latex');
set(gca, 'FontSize',14);
xlabel('Thread count','FontSize',16, 'Interpreter', 'latex');
ylabel('Run time (ms)','FontSize',16, 'Interpreter', 'latex');
print_fname = ['../report/figs/' plotnum '_Q1024_TimeVsThreadsVsEsize_cppElim'];
print('-r600', '-depsc', print_fname); % .eps (better for LaTeX)
%===========================

%===========================
%======================================================================
%======================================================================
% Time vs queue size, multiple lines are num threads (elim size = 4)
% y = time
% x = queue size, thread count
plotnum = '08a';
disp(['PLOT ' plotnum]);
x = [];
xtmp = [];
y = [];
ytmp = [];
plot_legend = {};

% Default values
t = t_def;
e = '4';
q = q_def;
p = p_def;
for i = 1:length(qd_size)
    q = qd_size{i};


    for j = 1:length(num_threads)
        t = num_threads{j};
        if j == 1
            xtmp = [str2num(q)];
            ytmp = [all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
        else
            xtmp = [xtmp str2num(q)];
            ytmp = [ytmp, all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
        end

        if i == 1
            plot_legend = [plot_legend; 'Thread count: ' t];
        end
    end

    x(i,:) = xtmp;
    y(i,:) = ytmp;
end

figure;
plot(x,y,'-o');
lims = ylim();
ylim([lims(1) (1.1*lims(2))]);
leg = legend(plot_legend, 'Location', 'north');
set(leg,'FontSize',16, 'Interpreter', 'latex');
set(gca, 'FontSize',14);
xlabel('Delegation queue size','FontSize',16, 'Interpreter', 'latex');
ylabel('Run time (ms)','FontSize',16, 'Interpreter', 'latex');
print_fname = ['../report/figs/' plotnum '_E4_TimeVsEsizeVsThreads_cppElim'];
print('-r600', '-depsc', print_fname); % .eps (better for LaTeX)

%======================================================================
%======================================================================
% Time vs elim size, multiple lines are num threads (queue size = 128)
% y = time
% x = elim size, thread count
plotnum = '08b';
disp(['PLOT ' plotnum]);
x = [];
xtmp = [];
y = [];
ytmp = [];
plot_legend = {};

% Default values
t = t_def;
e = e_def;
q = '128';
p = p_def;
for i = 1:length(elarray_size)
    e = elarray_size{i};


    for j = 1:length(num_threads)
        t = num_threads{j};
        if j == 1
            xtmp = [str2num(e)];
            ytmp = [all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
        else
            xtmp = [xtmp str2num(e)];
            ytmp = [ytmp, all_stats(['QD_t' t '_q' q '_e' e '_p' p ''])]; % C++
        end

        if i == 1
            plot_legend = [plot_legend; 'Thread count: ' t];
        end
    end

    x(i,:) = xtmp;
    y(i,:) = ytmp;
end

figure;
plot(x,y,'-o');
lims = ylim();
ylim([lims(1) (1.1*lims(2))]);
leg = legend(plot_legend, 'Location', 'east');
set(leg,'FontSize',16, 'Interpreter', 'latex');
set(gca, 'FontSize',14);
xlabel('Elimination array size','FontSize',16, 'Interpreter', 'latex');
ylabel('Run time (ms)','FontSize',16, 'Interpreter', 'latex');
print_fname = ['../report/figs/' plotnum '_Q128_TimeVsEsizeVsThreads_cppElim'];
print('-r600', '-depsc', print_fname); % .eps (better for LaTeX)
%===========================

