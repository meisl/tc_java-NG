@echo off
set JAVA_HOME=c:\Programme\Java\jdk1.7.0_25
set JAVALIB=%COMMANDER_PATH%\javalib
%JAVA_HOME%\bin\javac -classpath tc-apis-1.7.jar;%JAVALIB%\swt-win32-3.1.2.jar;%JAVALIB%\commons-logging-api-1.0.4.jar *.java
IF ERRORLEVEL 1 (
  del ADS.class 2>NUL
  del ADS.jar 2>NUL
) ELSE (
  %JAVA_HOME%\bin\jar cvf NtfsStreamsJ.jar *.class
)
