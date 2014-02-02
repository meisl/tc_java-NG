@ECHO OFF

SETLOCAL ENABLEDELAYEDEXPANSION
IF ERRORLEVEL 1 (
  ECHO no delayed expansion!
  GOTO FAULT
)
SET ROOT=%~dp0
REM strip trailing backslash:
SET ROOT=%ROOT:~0,-1%

SET TC_API=%ROOT%\dist\tc-apis-NG.jar
SET PLUGIN_NAME=%1
FOR /D %%i IN ("%ROOT%\example-plugins\%PLUGIN_NAME%*") DO (
  SET PLUGIN_DIR=%%i
)
SET JAR_NAME=%PLUGIN_DIR%\dist\%PLUGIN_NAME%.jar

IF NOT EXIST "%PLUGIN_DIR%" (
  ECHO no such plugin: "%PLUGIN_DIR%"
  GOTO FAULT
)

SET MAIN_CLASS=%2
IF "%MAIN_CLASS%"=="" (
  ECHO missing arg: main class
  GOTO FAULT
)

REM will call root build.bat s.t. java, JAVALIB and tc-apis-NG.jar are available
CALL "%PLUGIN_DIR%\build.bat"
IF ERRORLEVEL 1 (
  GOTO FAULT
)

SET MY_CP="%JAR_NAME%";"%TC_API%";"%JAVALIB%\swt-win32-3.1.2.jar";"%JAVALIB%\commons-logging-api-1.0.4.jar"

ECHO.
ECHO running %1/%2 %3 %4 %5 %6 %7 %8 %9

java -cp %MY_CP% %2 %3 %4 %5 %6 %7 %8 %9
IF ERRORLEVEL 1 (
  GOTO FAULT
)

:DONE
  ENDLOCAL & EXIT /B 0

:FAULT
  ENDLOCAL & EXIT /B 1
