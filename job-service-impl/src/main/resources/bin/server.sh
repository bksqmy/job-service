#!/bin/bash
export JAVA_HOME=/data/server/jdk1.7.0_79
export PATH=$JAVA_HOME/bin:$PATH
WHO=`whoami`
PROJECT=job-service
start() {
if [ "`ps aux | grep -w $PROJECT/lib | grep -v grep  | awk '{print $2}'`" = "" ];then
if [[ $WHO == root ]];then
                su - admin -c   /data/App/$PROJECT/bin/start.sh |awk '{printf "..."}'| echo -e '\e[32mserver start Success \e[m'
        elif [[ $WHO == admin ]];then
                /data/App/$PROJECT/bin/start.sh|awk '{printf "..."}'| echo -e '\e[32mserver start Success \e[m'
        fi
else 
	echo -e "\e[1;31mThis service  already runing\e[0m"
	exit;
fi
}
stop() {
	kill -9 `ps aux | grep -w $PROJECT/lib | grep -v grep  | awk '{print $2}'`
	echo -e '\e[32mserver stop Success \e[m'
}

restart(){
        stop
        start
}

case "$1" in
  start)
        start
        ;;
  stop)
        stop
        ;;
  restart)
        restart
        ;;
  ?|help | -h)
        echo $"Usage: $0 {start|stop|restart|help|?}"
        ;;
  *)
        restart
esac
