@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-21
cd /d "E:\campus-secondhand-trading\backend"
call "E:\campus-secondhand-trading\backend\mvnw.cmd" -f "E:\campus-secondhand-trading\backend\pom.xml" package -DskipTests -q 2^>^&1
