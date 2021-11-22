echo "kill bis..."
kill -9 `cat /home/software/server/nop/8082-bis/bis.pid` 
sleep 1s
echo "start bis..."
java -Xms64m -Xmx512m -jar --add-opens java.base/java.lang=ALL-UNNAMED bis.jar > /dev/null 2>&1 &
echo $! >/home/software/server/nop/8082-bis/bis.pid #将PID写入文件
echo "bis is started"