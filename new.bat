@ECHO OFF

SETLOCAL ENABLEDELAYEDEXPANSION
IF ERRORLEVEL 1 (
  ECHO no delayed expansion!
  GOTO FAULT
)
SET ROOT=%~dp0
REM strip trailing backslash:
SET ROOT=%ROOT:~0,-1%


IF /I x%1==xWDX (
  SET PLUGIN_TYPE=WDX
  GOTO GOT_TYPE
)
IF /I x%1==xWCX (
  SET PLUGIN_TYPE=WCX
  GOTO NYI
)
IF /I x%1==xWLX (
  SET PLUGIN_TYPE=WLX
  GOTO NYI
)
IF /I x%1==xWFX (
  SET PLUGIN_TYPE=WFX
  GOTO NYI
)
ECHO invalid plugin type "%1" - must be one of WDX, WCX, WLX or WFX
GOTO FAULT

:GOT_TYPE
SET PLUGIN_NAME=%2
IF x%PLUGIN_NAME%==x (
  ECHO missing arg: plugin name
  GOTO FAULT
)

:GOT_NAME
SET DUMMY=x%3

SET DESCRIPTION=%DUMMY:"=%
IF "%DESCRIPTION%"=="x" (
  ECHO missing arg: one-line description ^(enclose in double-quotes^)
  GOTO FAULT
)

SET TEMPLATES=%ROOT%\templates
SET PLUGIN_DIR=%ROOT%\example-plugins\%PLUGIN_NAME%.%PLUGIN_TYPE%
SET SRC=%PLUGIN_DIR%\src
SET DIST=%PLUGIN_DIR%\dist

IF EXIST "%ROOT%\example-plugins\%PLUGIN_NAME%*" (
  ECHO plugin "%PLUGIN_NAME%" already exists ^("%PLUGIN_DIR%"^)
  GOTO FAULT
)

REM TODO: check for valid plugin name (no spaces etc)

ECHO initializing %PLUGIN_TYPE% project %PLUGIN_NAME%: "%PLUGIN_DIR%"...
MKDIR "%PLUGIN_DIR%"
IF ERRORLEVEL 1 (
  ECHO error creating dir %PLUGIN_DIR%
  ECHO.
  GOTO FAULT
)

REM strip leading x:
ECHO %DESCRIPTION:~1%>>"%PLUGIN_DIR%\description.txt"
type "%PLUGIN_DIR%\description.txt"

COPY "%TEMPLATES%\build.bat" "%PLUGIN_DIR%\"

MKDIR "%SRC%"

TYPE "%TEMPLATES%\%PLUGIN_TYPE%-0.java" >>"%SRC%\%PLUGIN_NAME%.java"
REM like ECHO but without newline:
<NUL SET /p dummyName=%PLUGIN_NAME%>>"%SRC%\%PLUGIN_NAME%.java"
TYPE templates\%PLUGIN_TYPE%-1.java >>"%SRC%\%PLUGIN_NAME%.java"
<NUL SET /p dummyName=%PLUGIN_NAME%>>"%SRC%\%PLUGIN_NAME%.java"
TYPE templates\%PLUGIN_TYPE%-2.java >>"%SRC%\%PLUGIN_NAME%.java"

MKDIR "%DIST%"
ECHO done.
ECHO(

ECHO Now let's build and run it on this file (%~nx0) and README.md:
ECHO `run.bat %PLUGIN_NAME% %PLUGIN_NAME% %~nx0 README.md`
ECHO(
CALL "%ROOT%\run.bat" %PLUGIN_NAME% %PLUGIN_NAME% %~nx0 README.md


:DONE
  ENDLOCAL & EXIT /b 0

:FAULT
  ENDLOCAL & EXIT /B 1

:NYI
  ECHO sorry, %PLUGIN_TYPE% not yet supported.
  GOTO FAULT