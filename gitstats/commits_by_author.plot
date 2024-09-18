set terminal png transparent size 640,240
set size 1.0,1.0

set terminal png transparent size 640,480
set output 'commits_by_author.png'
set key left top
set yrange [0:]
set xdata time
set timefmt "%s"
set format x "%Y-%m-%d"
set grid y
set ylabel "Commits"
set xtics rotate
set bmargin 6
plot 'commits_by_author.dat' using 1:2 title "梁肖" w lines, 'commits_by_author.dat' using 1:3 title "侯杰" w lines, 'commits_by_author.dat' using 1:4 title "liangxiao6" w lines, 'commits_by_author.dat' using 1:5 title "houjie" w lines, 'commits_by_author.dat' using 1:6 title "liangxiao6@live.com" w lines, 'commits_by_author.dat' using 1:7 title "liangxiao" w lines, 'commits_by_author.dat' using 1:8 title "[houjie]" w lines, 'commits_by_author.dat' using 1:9 title "GeekAPP" w lines
