@echo off
set JAR_NAME=NtfsStreamsJ.jar
set JAVA_HOME=c:\Programme\Java\jdk1.7.0_25
set JAVALIB=%COMMANDER_PATH%\javalib
set MY_CLASS_PATH=tc-apis-1.7.jar;%JAVALIB%\swt-win32-3.1.2.jar;%JAVALIB%\commons-logging-api-1.0.4.jar
del bin\*.class 2>NUL
%JAVA_HOME%\bin\javac -Xlint -classpath %MY_CLASS_PATH% src/*.java -d bin
IF ERRORLEVEL 1 (
  del "%JAR_NAME%" 2>NUL
  GOTO DONE
)

cd bin\
%JAVA_HOME%\bin\jar cf "..\%JAR_NAME%" *.class
del *.class 2>NUL
cd ..
IF x%1==xtest (
  cd test
  createTestFiles.bat
  cd ..
  %JAVA_HOME%\bin\java -cp %MY_CLASS_PATH%;%JAR_NAME% Main %2 %3 %4 %5 %6 %7 %8 %9
)

:DONE
