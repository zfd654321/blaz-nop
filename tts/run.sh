echo "kill tts..."
kill -9 `cat /home/software/server/8085-tts/tts.pid` 
sleep 1s
echo "start tts..."
java -Xms64m -Xmx512m -jar tts.jar > /dev/null 2>&1 &
echo $! >/home/software/server/8085-tts/tts.pid #将PID写入文件
echo "tts is started"