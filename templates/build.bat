REM set variable PLUGIN_NAME to name of containing folder:
CALL :SET_PLUGIN_NAME %CD%

SET JAVA_HOME=c:\Programme\Java\jdk1.7.0_25
SET JAVALIB=%COMMANDER_PATH%\javalib

SET MY_CLASS_PATH=..\..\vendor\tc_java\tc-apis-1.7.jar;%JAVALIB%\swt-win32-3.1.2.jar;%JAVALIB%\commons-logging-api-1.0.4.jar
SET JAR=%JAVA_HOME%\bin\jar

MKDIR bin 2>NUL
DEL /S /Q bin >NUL 2>&1
MKDIR dist 2>NUL
FOR %%i IN (dist\*) DO (
  IF NOT "%%i"=="dist\.gitignore" del /Q "%%i" >NUL 2>&1
)

ECHO(
ECHO compiling %PLUGIN_NAME%...

%JAVA_HOME%\bin\javac -Xlint -cp %MY_CLASS_PATH% -sourcepath ..\..\src;src -implicit:none -d bin src\*.java
IF ERRORLEVEL 1 (
  EXIT /B 1
)
%JAR% cf "dist\%PLUGIN_NAME%.jar" -C bin .
REM COPY vendor\whatever-else-you-need dist\ >NUL

IF "%1"=="test" (
  %JAVA_HOME%\bin\java -cp %MY_CLASS_PATH%;%PLUGIN_NAME%.jar Main %2 %3 %4 %5 %6 %7 %8 %9
)

:DONE
  EXIT /B

:SET_PLUGIN_NAME
  SET PLUGIN_NAME=%~n1
  EXIT /B