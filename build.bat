@ECHO OFF
SET MY_DIR=%CD%
SET DIST=%CD%\dist
SET ZIP_TEMPLATE=%CD%\templates\plugin.zip

SET JAVA_HOME=c:\Programme\Java\jdk1.7.0_25
SET JAVALIB=%COMMANDER_PATH%\javalib

SET MY_CLASS_PATH=%JAVALIB%\swt-win32-3.1.2.jar;%JAVALIB%\commons-logging-api-1.0.4.jar
SET JAR=%JAVA_HOME%\bin\jar

MKDIR bin 2>NUL
DEL /S /Q bin >NUL
MKDIR %DIST% 2>NUL
DEL %DIST%\tc-apis-NG.jar 2>NUL

ECHO compiling tc-apis-NG in %MY_DIR%...

%JAVA_HOME%\bin\javac -Xlint -cp %MY_CLASS_PATH% -sourcepath src\java -d bin src\java\plugins\*.java src\java\plugins\wcx\*.java src\java\plugins\wdx\*.java  src\java\plugins\wfx\*.java  src\java\plugins\wlx\*.java
IF ERRORLEVEL 1 (
  EXIT /B 1
)

%JAR% cf %DIST%\tc-apis-NG.jar -C bin plugins

rem RMDIR /S /Q bin\plugins >NUL 2>&1


IF "%1"=="test" (
  CD test
  createTestFiles.bat
  CD ..
  %JAVA_HOME%\bin\java -cp %MY_CLASS_PATH%;%JAR_NAME% Main %2 %3 %4 %5 %6 %7 %8 %9
)

IF "%1"=="jdoc" (
  RMDIR /S /Q "%MY_DIR%\doc\api" >NUL 2>&1
  MKDIR "%MY_DIR%\doc\api" 2>NUL

  REM TODO: make javadoc for *all* plugins in example-plugins
  %JAVA_HOME%\bin\javadoc -d doc\api -use -sourcepath src\java;example-plugins\NtfsStreamsJ\src example-plugins\NtfsStreamsJ\src\*.java -subpackages plugins
)

IF "%1"=="dist" (
  DEL /S /Q "%DIST%\*.zip" >NUL 2>&1
  RMDIR /S /Q "%DIST%\temp" >NUL 2>&1

  CD example-plugins\
  FOR /D %%i in (*) do (
    CD %%i
    CALL build.bat
    IF ERRORLEVEL 1 (
      ECHO %%i %PLUGIN_TYPE% failed!
    ) ELSE (
      SET PLUGIN_TYPE=WDX
      ECHO %%i %PLUGIN_TYPE% done.
      ECHO(

      MKDIR "%DIST%\temp" 2>NUL
      REM everything from plugin's dist\ into our %DIST%\temp\:
      COPY dist\* "%DIST%\temp\" >NUL
      DEL /Q "%DIST%\temp\.gitignore" >NUL 2>&1

      COPY "%ZIP_TEMPLATE%\rename-me.w_x" "%DIST%\temp\%%i.%PLUGIN_TYPE%" >NUL
      COPY "%ZIP_TEMPLATE%\license.txt" "%DIST%\temp\" >NUL
      COPY "%ZIP_TEMPLATE%\errormessages.ini" "%DIST%\temp\" >NUL
      COPY "%ZIP_TEMPLATE%\tc_javaplugin.ini.stub" "%DIST%\temp\tc_javaplugin.ini" >NUL

      ECHO [%PLUGIN_TYPE%]>>"%DIST%\temp\tc_javaplugin.ini"
      ECHO CLASS=%%i>>"%DIST%\temp\tc_javaplugin.ini"

      ECHO [plugininstall]>>"%DIST%\temp\pluginst.inf"
      ECHO description=%PLUGIN_TYPE% plugin %%i>>"%DIST%\temp\pluginst.inf"
      ECHO type=%PLUGIN_TYPE%>>"%DIST%\temp\pluginst.inf"
      ECHO file=%%i.%PLUGIN_TYPE%>>"%DIST%\temp\pluginst.inf"
      ECHO defaultdir=%%i>>"%DIST%\temp\pluginst.inf"

      %JAR% cMf "%DIST%\%%i.zip" -C "%DIST%\temp" .
      %JAR% uMf "%DIST%\%%i.zip" -C "%DIST%" tc-apis-NG.jar

      RMDIR /S /Q "%DIST%\temp" >NUL 2>&1
    )
    CD ..
  )

)

:DONE
CD "%MY_DIR%"