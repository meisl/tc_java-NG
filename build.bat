@echo off

set JAVA_HOME=c:\Programme\Java\jdk1.7.0_25
set JAVALIB=%COMMANDER_PATH%\javalib

set MY_CLASS_PATH=vendor\tc_java\tc-apis-1.7.jar;%JAVALIB%\swt-win32-3.1.2.jar;%JAVALIB%\commons-logging-api-1.0.4.jar
set JAR=%JAVA_HOME%\bin\jar

mkdir bin 2>NUL
del /S /Q bin >NUL
mkdir dist 2>NUL
del dist\tc-apis-NG.jar 2>NUL

set MY_DIR=%CD%
echo compiling tc-apis-NG in %MY_DIR%...

%JAVA_HOME%\bin\javac -Xlint -cp %MY_CLASS_PATH% -sourcepath src -d bin src/plugins/wdx/*.java
IF ERRORLEVEL 1 (
  GOTO DONE
)

copy /Y vendor\tc_java\tc-apis-1.7.jar dist\tc-apis-NG.jar >NUL
%JAR% uf dist\tc-apis-NG.jar -C bin plugins
rmdir /S /Q bin\plugins >NUL 2>&1


if "%1"=="test" (
  cd test
  createTestFiles.bat
  cd ..
  %JAVA_HOME%\bin\java -cp %MY_CLASS_PATH%;%JAR_NAME% Main %2 %3 %4 %5 %6 %7 %8 %9
)

if "%1"=="jdoc" (
  rmdir /S /Q "%MY_DIR%\doc\api" >NUL 2>&1
  mkdir "%MY_DIR%\doc\api" 2>NUL
  %JAVA_HOME%\bin\javadoc -d doc\api -use -sourcepath src;tc-apis-1.7;example-plugins\NtfsStreamsJ\src example-plugins\NtfsStreamsJ\src\*.java -subpackages plugins
)

if "%1"=="dist" (
  del /S /Q "%MY_DIR%\dist\*.zip" >NUL 2>&1
  rmdir /S /Q "%MY_DIR%\dist\temp" >NUL 2>&1

  cd example-plugins\
  for /D %%i in (*) do (
    cd %%i
    call build.bat
    set PLUGIN_TYPE=WDX
    echo %%i %PLUGIN_TYPE% done.
    echo .

    mkdir "%MY_DIR%\dist\temp" 2>NUL
    copy dist\* "%MY_DIR%\dist\temp\" >NUL
    del /Q "%MY_DIR%\dist\temp\.gitignore" >NUL 2>&1

    copy "%MY_DIR%\vendor\tc_java\rename-me.w_x" "%MY_DIR%\dist\temp\%%i.%PLUGIN_TYPE%" >NUL
    copy "%MY_DIR%\vendor\tc_java\license.txt" "%MY_DIR%\dist\temp\" >NUL
    copy "%MY_DIR%\vendor\tc_java\errormessages.ini" "%MY_DIR%\dist\temp\" >NUL
    copy "%MY_DIR%\vendor\tc_java\tc_javaplugin.ini.stub" "%MY_DIR%\dist\temp\tc_javaplugin.ini" >NUL

    echo [%PLUGIN_TYPE%]>>"%MY_DIR%\dist\temp\tc_javaplugin.ini"
    echo CLASS=%%i>>"%MY_DIR%\dist\temp\tc_javaplugin.ini"

    echo [plugininstall]>>"%MY_DIR%\dist\temp\pluginst.inf"
    echo description=%PLUGIN_TYPE% plugin %%i>>"%MY_DIR%\dist\temp\pluginst.inf"
    echo type=%PLUGIN_TYPE%>>"%MY_DIR%\dist\temp\pluginst.inf"
    echo file=%%i.%PLUGIN_TYPE%>>"%MY_DIR%\dist\temp\pluginst.inf"
    echo defaultdir=%%i>>"%MY_DIR%\dist\temp\pluginst.inf"

    %JAR% cMf "%MY_DIR%\dist\%%i.zip" -C "%MY_DIR%\dist\temp" .
    %JAR% uMf "%MY_DIR%\dist\%%i.zip" -C "%MY_DIR%\dist" tc-apis-NG.jar

    rmdir /S /Q "%MY_DIR%\dist\temp" >NUL 2>&1
    cd ..
  )

)

:DONE
cd "%MY_DIR%"