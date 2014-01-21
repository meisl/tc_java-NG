@echo off

set PLUGIN_NAME=NtfsStreamsJ
set PLUGIN_TYPE=WDX

set JAVA_HOME=c:\Programme\Java\jdk1.7.0_25
set JAVALIB=%COMMANDER_PATH%\javalib

set MY_CLASS_PATH=..\..\vendor\tc_java\tc-apis-1.7.jar;%JAVALIB%\swt-win32-3.1.2.jar;%JAVALIB%\commons-logging-api-1.0.4.jar
set JAR=%JAVA_HOME%\bin\jar

mkdir bin 2>NUL
del /S /Q bin >NUL 2>&1
mkdir dist 2>NUL
for %%i in (dist\*) do (
  if NOT "%%i"=="dist\.gitignore" del /Q "%%i" >NUL 2>&1
)

echo .
echo compiling %PLUGIN_NAME%...

%JAVA_HOME%\bin\javac -Xlint -cp %MY_CLASS_PATH% -sourcepath ..\..\src;src -d bin src\*.java
IF ERRORLEVEL 1 (
  GOTO DONE
)
%JAVA_HOME%\bin\jar cf "dist\%PLUGIN_NAME%.jar" -C bin .
copy vendor\lads\lads.exe dist\ >NUL
copy vendor\streams\streams.exe dist\ >NUL

IF "%1"=="test" (
  cd test
  createTestFiles.bat
  cd ..
  %JAVA_HOME%\bin\java -cp %MY_CLASS_PATH%;%PLUGIN_NAME%.jar Main %2 %3 %4 %5 %6 %7 %8 %9
)

:DONE
