@ECHO OFF

CALL :FIND_JAVA_AND_TC
IF ERRORLEVEL 1 (
  EXIT /B 1
)
IF "%1"=="init" (
  EXIT /B 0
)
IF DEFINED ROOTBUILDRUNNING (
  REM ECHO ROOTBUILDRUNNING=%ROOTBUILDRUNNING%
  EXIT /B 0
)

SETLOCAL ENABLEDELAYEDEXPANSION
IF ERRORLEVEL 1 (
  ECHO no delayed expansion!
  GOTO FAULT
)

SET ROOTBUILDRUNNING=1

SET ROOT=%~dp0
REM strip trailing backslash:
SET ROOT=%ROOT:~0,-1%

SET SRC=%ROOT%\src\java
SET BIN=%ROOT%\bin
SET DIST=%ROOT%\dist
SET JDOC=%ROOT%\doc\api
SET TEMP=%ROOT%\dist\temp
SET TEMPLATES=%ROOT%\templates
SET TC_API=%ROOT%\vendor\tc_java\tc-apis-1.7.jar

SET MY_DIR=%ROOT%

SET MY_CP=%JAVALIB%\swt-win32-3.1.2.jar;%JAVALIB%\commons-logging-api-1.0.4.jar

MKDIR "%BIN%" 2>NUL
DEL /S /Q "%BIN%" >NUL
MKDIR "%DIST%" 2>NUL
DEL "%DIST%\tc-apis-NG.jar" 2>NUL

ECHO compiling tc-apis-NG...
javac -Xlint -cp %MY_CP% -sourcepath "%SRC%";"%TC_API%" -d "%BIN%" "%SRC%"\plugins\*.java "%SRC%"\plugins\wdx\*.java
IF ERRORLEVEL 1 (
  ECHO tc-apis-NG failed!
  ECHO.
  GOTO FAULT
)

COPY /Y "%TC_API%" "%DIST%\tc-apis-NG.jar" >NUL
jar uf "%DIST%\tc-apis-NG.jar" -C "%BIN%" .
ECHO tc-apis-NG done.
ECHO.


IF "%1"=="jdoc" (
  ECHO creating javadoc in "%JDOC%\"...
  RMDIR /S /Q "%JDOC%" >NUL 2>&1
  MKDIR "%JDOC%" 2>NUL
  javadoc -d "%JDOC%" -quiet -classpath "%MY_CP%" -use -author -windowtitle "tc_java API" -doctitle "Total Commander Plugin Interface API" -sourcepath "%SRC%";"%TC_API%" -subpackages plugins
)

IF "%1"=="dist" (
  DEL /S /Q "%DIST%\*.zip" >NUL 2>&1
  RMDIR /S /Q "%TEMP%" >NUL 2>&1
  COPY /Y "%TEMPLATES%\dist-README-0.md" "%DIST%\README.md" >NUL

  FOR /D %%i in ("%ROOT%\example-plugins\*") do (
    REM ECHO calling "%%i\build.bat"...
    CALL "%%i\build.bat"
    IF ERRORLEVEL 1 (
      ECHO %%i failed
    ) ELSE (
      CALL :MAKE_DIST %%i
      IF ERRORLEVEL 1 (
        ECHO %%i failed
      )
    )
    ECHO.
  )
  TYPE "%TEMPLATES%\dist-README-1.md" >>"%DIST%\README.md"
)


:DONE
  ENDLOCAL & EXIT /B 0


:FAULT
  ENDLOCAL & EXIT /B 1


:MAKE_DIST
  SETLOCAL ENABLEDELAYEDEXPANSION
  SET PLUGIN_PATH=%1
  SET PLUGIN_DIR=%~nx1
  SET PLUGIN_NAME=%~n1
  SET PLUGIN_TYPE=%~x1
  IF x%PLUGIN_TYPE%==x GOTO MAKE_DIST_INVALID_TYPE
  REM remove leading dot:
  SET PLUGIN_TYPE=%PLUGIN_TYPE:~1%
  IF x%PLUGIN_TYPE%==xWDX GOTO MAKE_DIST_NAME_OK
  IF x%PLUGIN_TYPE%==xWCX GOTO MAKE_DIST_NAME_OK
  IF x%PLUGIN_TYPE%==xWLX GOTO MAKE_DIST_NAME_OK
  IF x%PLUGIN_TYPE%==xWFX GOTO MAKE_DIST_NAME_OK
  GOTO MAKE_DIST_INVALID_TYPE

:MAKE_DIST_NAME_OK
  SET ZIP_NAME=%PLUGIN_DIR%.zip
  SET ZIP_PATH=%DIST%\%ZIP_NAME%
  SET ZIP_URL=http://github.com/meisl/tc_java-NG/blob/master/dist/%ZIP_NAME%?raw=true
  
  REM ECHO creating "%ZIP_PATH%"...

  REM note the ^( and ^), they're escapes
  ECHO * [%PLUGIN_DIR%]^(%ZIP_URL%^): TODO: description>>"%DIST%\README.md"

  MKDIR "%TEMP%" 2>NUL
  COPY "%PLUGIN_PATH%\dist\*" "%TEMP%\" >NUL
  DEL /Q "%TEMP%\.gitignore" >NUL 2>&1

  COPY "%MY_DIR%\vendor\tc_java\rename-me.w_x" "%TEMP%\%PLUGIN_DIR%" >NUL
  COPY "%MY_DIR%\vendor\tc_java\license.txt" "%TEMP%\" >NUL
  COPY "%MY_DIR%\vendor\tc_java\errormessages.ini" "%TEMP%\" >NUL
  COPY "%MY_DIR%\vendor\tc_java\tc_javaplugin.ini.stub" "%TEMP%\tc_javaplugin.ini" >NUL

  ECHO [%PLUGIN_TYPE%]>>"%TEMP%\tc_javaplugin.ini"
  ECHO CLASS=%PLUGIN_NAME%>>"%TEMP%\tc_javaplugin.ini"

  ECHO [plugininstall]>>"%TEMP%\pluginst.inf"
  ECHO description=%PLUGIN_TYPE% plugin %PLUGIN_NAME%>>"%TEMP%\pluginst.inf"
  ECHO type=%PLUGIN_TYPE%>>"%TEMP%\pluginst.inf"
  ECHO file=%PLUGIN_DIR%>>"%TEMP%\pluginst.inf"
  ECHO defaultdir=%PLUGIN_NAME%>>"%TEMP%\pluginst.inf"

  jar cMf "%ZIP_PATH%" -C "%TEMP%" .
  jar uMf "%ZIP_PATH%" -C "%DIST%" tc-apis-NG.jar
  ECHO %ZIP_NAME% created.

  RMDIR /S /Q "%TEMP%" >NUL 2>&1
  ENDLOCAL & EXIT /B 0

:MAKE_DIST_INVALID_TYPE
  ECHO invalid plugin type "%PLUGIN_TYPE%"
:MAKE_DIST_FAULT
  RMDIR /S /Q "%TEMP%" >NUL 2>&1
  ENDLOCAL & EXIT /B 1



:FIND_JAVA_AND_TC
  REM first check if javac is on the PATH:
  FOR /F %%i IN ("javac.exe") DO (
    IF NOT "%%~$PATH:i"=="" (
      GOTO FIND_JAVA_AND_TC_FOUNDJAVA
    )
  )

  IF NOT EXIST "%JAVA_HOME%\bin\javac*" (
    FOR /D %%i IN (%PROGRAMFILES%\java\*) DO (
      IF EXIST "%%i\bin\javac*" (
        PATH %%i\bin;%PATH%
        GOTO FIND_JAVA_AND_TC_FOUNDJAVA
      )
    )
    ECHO could not find JDK in "%PROGRAMFILES%\java\"!
    EXIT /B 1
  )

:FIND_JAVA_AND_TC_FOUNDJAVA
  IF NOT EXIST "%COMMANDER_PATH%\plugins\" (
    FOR /D %%i IN (%PROGRAMFILES%\*) DO (
      IF "%%i"=="%PROGRAMFILES%\totalcmd" (
        IF EXIST "%PROGRAMFILES%\totalcmd\plugins\" (
          SET COMMANDER_PATH=%%i
          GOTO FIND_JAVA_AND_TC_FOUNDTC
        )
      )
    )
    ECHO could not find TC in "%PROGRAMFILES%\"!
    ECHO please open command prompt *from within TC*
    EXIT /B 1
  )

:FIND_JAVA_AND_TC_FOUNDTC
  SET JAVALIB=%COMMANDER_PATH%\javalib
  IF NOT EXIST "%JAVALIB%" (
    ECHO missing "%JAVALIB%\"!
    ECHO please extract javalib.tgz to "%JAVALIB%\"
    EXIT /B 1
  )
 
  EXIT /B 0
