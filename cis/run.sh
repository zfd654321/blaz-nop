echo "kill cis..."
kill -9 `cat /home/software/server/nop/8083-cis/cis.pid` 
sleep 1s
echo "start cis..."
java -Xms64m -Xmx512m -jar --add-opens java.base/java.lang=ALL-UNNAMED cis.jar > /dev/null 2>&1 &
echo $! >/home/software/server/nop/8083-cis/cis.pid #将PID写入文件
echo "cis is started"
