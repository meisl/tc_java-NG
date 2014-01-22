@ECHO OFF
SET MY_DIR=%CD%
SET PLUGIN_TYPE=WDX

SET PLUGIN_NAME=%1

IF "%PLUGIN_NAME%"=="" (
  ECHO missing arg: plugin name
  GOTO DONE
)
SET PLUGIN_DIR=example-plugins\%1

IF EXIST %PLUGIN_DIR% (
  ECHO %PLUGIN_DIR% already exists
  GOTO DONE
)

REM TODO: check for valid plugin name (no spaces etc)

ECHO initializing %PLUGIN_TYPE% project %PLUGIN_NAME%...
MKDIR %PLUGIN_DIR%

ECHO @ECHO OFF>>%PLUGIN_DIR%\build.bat
ECHO( >>%PLUGIN_DIR%\build.bat
ECHO SET PLUGIN_TYPE=%PLUGIN_TYPE%>>%PLUGIN_DIR%\build.bat
TYPE templates\build.bat >>%PLUGIN_DIR%\build.bat


MKDIR %PLUGIN_DIR%\src

TYPE templates\%PLUGIN_TYPE%-0.java >>%PLUGIN_DIR%\src\%PLUGIN_NAME%.java
REM like ECHO but without newline:
<NUL SET /p dummyName=%PLUGIN_NAME%>>%PLUGIN_DIR%\src\%PLUGIN_NAME%.java
TYPE templates\%PLUGIN_TYPE%-1.java >>%PLUGIN_DIR%\src\%PLUGIN_NAME%.java
<NUL SET /p dummyName=%PLUGIN_NAME%>>%PLUGIN_DIR%\src\%PLUGIN_NAME%.java
TYPE templates\%PLUGIN_TYPE%-2.java >>%PLUGIN_DIR%\src\%PLUGIN_NAME%.java

MKDIR %PLUGIN_DIR%\dist
ECHO done.
ECHO(

ECHO Now let's build and run it on this file (%0) and README.md:
ECHO `run.bat %PLUGIN_NAME% %PLUGIN_NAME% %0 README.md`
ECHO(
CALL run.bat %PLUGIN_NAME% %PLUGIN_NAME% %0 README.md


:DONE
CD "%MY_DIR%"
EXIT /b
