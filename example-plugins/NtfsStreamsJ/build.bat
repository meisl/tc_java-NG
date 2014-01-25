@ECHO OFF

REM make sure we have javac on PATH and JAVALIB set, as well as tc-apis-NG.jar ready:
CALL %~dp0..\..\build.bat
IF ERRORLEVEL 1 (
  EXIT /B 1
)

SETLOCAL ENABLEDELAYEDEXPANSION
IF ERRORLEVEL 1 (
  ECHO no delayed expansion!
  EXIT /B 1
)
SET PLUGIN_TYPE=WDX

SET MY_PATH=%~dp0
REM strip trailing backslash:
SET MY_PATH=%MY_PATH:~0,-1%
REM set variable MY_NAME to name of containing folder:
CALL :ASSIGN_NAMEPART MY_NAME %MY_PATH%

SET ROOT=%~dp0..\..
SET ROOT_SRC=%ROOT%\src\java
SET MY_CP="%ROOT%\dist\tc-apis-NG.jar";"%ROOT%\vendor\tc_java\tc-apis-1.7.jar";"%JAVALIB%\swt-win32-3.1.2.jar";"%JAVALIB%\commons-logging-api-1.0.4.jar"
SET SRC=%MY_PATH%\src
SET BIN=%MY_PATH%\bin
SET DIST=%MY_PATH%\dist
SET JAR_NAME=%DIST%\%MY_NAME%.jar

MKDIR "%BIN%" 2>NUL
DEL /S /Q "%BIN%" >NUL 2>&1
MKDIR "%DIST%" 2>NUL
FOR %%i IN ("%DIST%\*") DO (
  IF NOT "%%i"=="%DIST%\.gitignore" (
    DEL /Q "%%i" >NUL 2>&1
  )
)

ECHO(
ECHO compiling %MY_NAME% to %BIN%...

javac -Xlint -cp %MY_CP% -sourcepath "%ROOT_SRC%" -implicit:none -d "%BIN%" "%SRC%"\*.java "%SRC%"\fun\*.java "%SRC%"\seq\*.java
IF ERRORLEVEL 1 (
  ECHO %MY_NAME% failed!
  EXIT /B 1
)

jar cf "%JAR_NAME%" -C "%BIN%" .
COPY "%MY_PATH%\vendor\lads\lads.exe" "%DIST%\" >NUL
COPY "%MY_PATH%\vendor\streams\streams.exe" "%DIST%\" >NUL

IF "%1"=="test" (
  rem CD test
  rem createTestFiles.bat
  rem CD ..
  java -cp %MY_CP%;"%JAR_NAME%" Main %2 %3 %4 %5 %6 %7 %8 %9
)

:DONE
  EXIT /B 0

:ASSIGN_NAMEPART
  SET %1=%~n2
  EXIT /B 0