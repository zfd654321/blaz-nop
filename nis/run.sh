echo "kill nis..."
kill -9 `cat /home/software/server/nop/8084-nis/nis.pid` 
sleep 1s
echo "start nis..."
java -Xms64m -Xmx512m -jar --add-opens java.base/java.lang=ALL-UNNAMED nis.jar > /dev/null 2>&1 &
echo $! >/home/software/server/nop/8084-nis/nis.pid #将PID写入文件
echo "nis is started"
