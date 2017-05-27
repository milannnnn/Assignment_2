% check clusters
close all;
clear all;
clc;

a = csvread('cluster_1.csv');
b = csvread('cluster_2.csv');
c = csvread('cluster_3.csv');
d = csvread('cluster_4.csv');
%%
a1 = zeros(size(a,1),1);
b1 = zeros(size(b,1),1);
c1 = zeros(size(c,1),1);
d1 = zeros(size(d,1),1);
a2 = zeros(size(a,1),1);
b2 = zeros(size(b,1),1);
c2 = zeros(size(c,1),1);
d2 = zeros(size(d,1),1);
for k=1:size(a,2)/2
    a1 = a1+a(:,2*k-1)/(size(a,2)/2);
    b1 = b1+b(:,2*k-1)/(size(b,2)/2);
    c1 = c1+c(:,2*k-1)/(size(c,2)/2);
    d1 = d1+d(:,2*k-1)/(size(d,2)/2);

    a2 = a2+a(:,2*k)/(size(a,2)/2);
    b2 = b2+b(:,2*k)/(size(b,2)/2);
    c2 = c2+c(:,2*k)/(size(c,2)/2);
    d2 = d2+d(:,2*k)/(size(d,2)/2);
end

%%
figure();
hold all;
plot(a1,a2,'.');
plot(b1,b2,'.');
plot(c1,c2,'.');
plot(d1,d2,'.');
hold off;

%%
% 
% column=16;
% for column=1:18
%     figure()
%     plot(a(:,column));
%     hold on
%     plot(b(:,column));
%     plot(c(:,column));
%     plot(d(:,column));
%     hold off
% end