@ECHO OFF

set JAVA_HOME=c:\Programme\Java\jdk1.7.0_25
set JAVALIB=%COMMANDER_PATH%\javalib

set MY_CLASS_PATH=%JAVALIB%\swt-win32-3.1.2.jar;%JAVALIB%\commons-logging-api-1.0.4.jar
set JAR=%JAVA_HOME%\bin\jar

mkdir bin 2>NUL
del /S /Q bin >NUL
mkdir dist 2>NUL
del dist\tc-apis-NG.jar 2>NUL

set MY_DIR=%CD%
echo compiling tc-apis-NG in %MY_DIR%...

%JAVA_HOME%\bin\javac -Xlint -cp %MY_CLASS_PATH% -sourcepath src\java -d bin src\java\plugins\*.java src\java\plugins\wcx\*.java src\java\plugins\wdx\*.java  src\java\plugins\wfx\*.java  src\java\plugins\wlx\*.java
IF ERRORLEVEL 1 (
  EXIT /B 1
)

%JAR% cf dist\tc-apis-NG.jar -C bin plugins
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
  DEL /S /Q "%MY_DIR%\dist\*.zip" >NUL 2>&1
  RMDIR /S /Q "%MY_DIR%\dist\temp" >NUL 2>&1

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

      MKDIR "%MY_DIR%\dist\temp" 2>NUL
      COPY dist\* "%MY_DIR%\dist\temp\" >NUL
      DEL /Q "%MY_DIR%\dist\temp\.gitignore" >NUL 2>&1

      COPY "%MY_DIR%\vendor\tc_java\rename-me.w_x" "%MY_DIR%\dist\temp\%%i.%PLUGIN_TYPE%" >NUL
      COPY "%MY_DIR%\vendor\tc_java\license.txt" "%MY_DIR%\dist\temp\" >NUL
      COPY "%MY_DIR%\vendor\tc_java\errormessages.ini" "%MY_DIR%\dist\temp\" >NUL
      COPY "%MY_DIR%\vendor\tc_java\tc_javaplugin.ini.stub" "%MY_DIR%\dist\temp\tc_javaplugin.ini" >NUL

      ECHO [%PLUGIN_TYPE%]>>"%MY_DIR%\dist\temp\tc_javaplugin.ini"
      ECHO CLASS=%%i>>"%MY_DIR%\dist\temp\tc_javaplugin.ini"

      ECHO [plugininstall]>>"%MY_DIR%\dist\temp\pluginst.inf"
      ECHO description=%PLUGIN_TYPE% plugin %%i>>"%MY_DIR%\dist\temp\pluginst.inf"
      ECHO type=%PLUGIN_TYPE%>>"%MY_DIR%\dist\temp\pluginst.inf"
      ECHO file=%%i.%PLUGIN_TYPE%>>"%MY_DIR%\dist\temp\pluginst.inf"
      ECHO defaultdir=%%i>>"%MY_DIR%\dist\temp\pluginst.inf"

      %JAR% cMf "%MY_DIR%\dist\%%i.zip" -C "%MY_DIR%\dist\temp" .
      %JAR% uMf "%MY_DIR%\dist\%%i.zip" -C "%MY_DIR%\dist" tc-apis-NG.jar

      RMDIR /S /Q "%MY_DIR%\dist\temp" >NUL 2>&1
    )
    CD ..
  )

)

:DONE
CD "%MY_DIR%"