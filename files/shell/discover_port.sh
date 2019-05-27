#! /bin/sh

#
#-tnlp=Tcp协议+不显示别名+listen状态+显示程序名称
#$1~$9表示输出的第几个参数
#awk -F':' '{if ($NF~/^[0-9]*$/) print $NF}表示截取冒号后面的值，且只能是0~9的数字
#sort|uniq表示排序和去重


portarray=(` sudo netstat -tnlp | egrep -i "$1" | awk {'print $4'} | awk -F ':' '{if ($NF~/^[0-9]*$/) print $NF}' | sort | uniq` )
length=${#portarray[@]}
printf "{\n"
printf '\t'"\"data\":["
for (( i=0;i<$length;i++))
  do
    printf '\n\t\t{'
    printf "\"{#TCP_PORT}\":\"${portarray[$i]}\"}"
    if [ $i -lt $[$length-1] ];then
                 printf ','
    fi
  done
printf "\n\t]\n"
printf "}\n"
