% check clusters
close all;
% 
% column=16;
for column=1:18
    figure()
    plot(a(:,column));
    hold on
    plot(b(:,column));
    plot(c(:,column));
    plot(d(:,column));
    hold off
end