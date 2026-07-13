@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-21
set PATH=%JAVA_HOME%\bin;%PATH%

echo Using Java:
java -version

echo.
echo Installing dependencies...
call mvnw.cmd clean package -DskipTests -q

echo.
echo Starting application...
call mvnw.cmd spring-boot:run
