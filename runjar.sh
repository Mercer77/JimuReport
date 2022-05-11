#!/bin/bash
#name:jar包启动脚本;
#date:2020-08-15;
#author：mercer;
#此处修改脚本名称：
APP_NAME=jimureport-example-1.5.jar
#修改运行环境
environment=''
#修改日志路径
LOG_FILE=''
#部署文件备份目录    
history_path="./back/"
#保留文件数
reservedNum=5
#脚本菜单项
usage(){
 echo "Usage: sh 脚本名.sh [start|stop|restart|status]"
 exit 1
}

is_exist(){
 pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}'`
 #如果不存在返回1，存在返回0
 if [ -z "${pid}" ]; then
 return 1
 else
 return 0
 fi
}

#文件备份
back(){
  #判断目录是否存在，不存在就创建
  if [ ! -d "$history_path" ]; then
    mkdir $history_path
  fi
  #判断文件是否存在，存在就开始备份
  if [  -f "$APP_NAME" ];then
      #备份jar包到history_path,根据日期时间命名
      echo "开始备份"
      datetime=`date +%Y%m%d-%H%M%S`
      cp "$APP_NAME" "$history_path$datetime-$APP_NAME"
      echo "完成文件备份"
  fi
}

#清理备份
clear(){
FileNum=$(ls -l $history_path|grep ^- |wc -l)
while(( $FileNum > $reservedNum))
do
    OldFile=$(ls -rt $history_path| head -1)
    echo  $datetime "Delete File:"$OldFile
    rm -rf $history_path$OldFile
    let "FileNum--"
done
}

#启动脚本
start(){
 is_exist
 if [ $? -eq "0" ]; then
 echo "${APP_NAME} is already running. pid=${pid} ."
 else
#此处注意修改jar和log文件文件位置：
 nohup  java -Xms512m -Xmx1024m  -jar ${APP_NAME} ${environment} > /dev/null 2>&1 &
#此处打印log日志：
 #tail  ${LOG_FILE}
 echo "程序已启动"
 fi
}
#停止脚本
stop(){
 is_exist
 if [ $? -eq "0" ]; then
 kill -15 $pid
 sleep 5s
 echo "程序已停止"
 else
 echo "${APP_NAME} is not running"
 fi
}
#显示当前jar运行状态
status(){
 is_exist
 if [ $? -eq "0" ]; then
 echo "${APP_NAME} is running. Pid is ${pid}"
 else
 echo "${APP_NAME} is NOT running."
 fi
}
#重启脚本
restart(){
 back
 clear
 stop
 start
}

case "$1" in
 "start")
 start
 ;;
 "stop")
 stop
 ;;
 "status")
 status
 ;;
 "restart")
 restart
 ;;
 *)
 usage
 ;;
esac
