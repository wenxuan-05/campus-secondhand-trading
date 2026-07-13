@REM ----------------------------------------------------------------------------
@REM Maven Wrapper startup script
@REM ----------------------------------------------------------------------------
@echo off
setlocal enabledelayedexpansion

set "MAVEN_PROJECTBASEDIR=%~dp0"
set "MVNW_VERBOSE=false"

if not defined MAVEN_HOME (
    if exist "%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6-bin" (
        set "MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6-bin\apache-maven-3.9.6"
    )
)

if not defined MAVEN_HOME (
    echo Downloading Maven...
    set "MAVEN_DIST=apache-maven-3.9.6-bin.zip"
    set "MAVEN_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip"
    set "TMPDIR=%TEMP%\maven-wrapper"
    if not exist "!TMPDIR!" mkdir "!TMPDIR!"

    powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri '!MAVEN_URL!' -OutFile '!TMPDIR!\!MAVEN_DIST!'}"
    powershell -Command "& {Expand-Archive -Path '!TMPDIR!\!MAVEN_DIST!' -DestinationPath '!TMPDIR!' -Force}"
    set "MAVEN_HOME=!TMPDIR!\apache-maven-3.9.6"
)

set "MAVEN_CMD=%MAVEN_HOME%\bin\mvn.cmd"
if not exist "%MAVEN_CMD%" (
    set "MAVEN_CMD=%MAVEN_HOME%\bin\mvn"
)

if not exist "%MAVEN_CMD%" (
    echo ERROR: Maven not found. Please install Maven manually: https://maven.apache.org
    exit /b 1
)

set "MAVEN_OPTS=-Xmx512m"
"%MAVEN_CMD%" %*
