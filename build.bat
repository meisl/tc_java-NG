@echo off
set JAVA_HOME=c:\Programme\Java\jdk1.7.0_25
set JAVALIB=%COMMANDER_PATH%\javalib
del *.class 2>NUL
%JAVA_HOME%\bin\javac -classpath tc-apis-1.7.jar;%JAVALIB%\swt-win32-3.1.2.jar;%JAVALIB%\commons-logging-api-1.0.4.jar *.java
IF ERRORLEVEL 1 (
  del NtfsStreamsJ.jar 2>NUL
  GOTO DONE
)

%JAVA_HOME%\bin\jar cf NtfsStreamsJ.jar *.class
IF x%1==xtest (
  cd test
  createTestFiles.bat
  cd ..
  %JAVA_HOME%\bin\java -cp tc-apis-1.7.jar;NtfsStreamsJ.jar;%COMMANDER_PATH%\javalib\commons-logging-api-1.0.4.jar Main %2 %3 %4 %5 %6 %7 %8 %9
)

:DONE
del *.class 2>NUL
