@echo off
rem set JAR_NAME=NtfsStreamsJ.jar
set JAVA_HOME=c:\Programme\Java\jdk1.7.0_25
set JAVALIB=%COMMANDER_PATH%\javalib
set MY_CLASS_PATH=vendor\tc_java\tc-apis-1.7.jar;%JAVALIB%\swt-win32-3.1.2.jar;%JAVALIB%\commons-logging-api-1.0.4.jar

set JAR=%JAVA_HOME%\bin\jar
del /S /Q bin >NUL
%JAVA_HOME%\bin\javac -Xlint -cp %MY_CLASS_PATH% -sourcepath src -d bin src\*.java
IF ERRORLEVEL 1 (
  del "%JAR_NAME%" 2>NUL
  GOTO DONE
)

rem %JAR% cf "%JAR_NAME%" -C bin .
rem del /S /Q bin >NUL
IF x%1==xtest (
  cd test
  createTestFiles.bat
  cd ..
  %JAVA_HOME%\bin\java -cp %MY_CLASS_PATH%;%JAR_NAME% Main %2 %3 %4 %5 %6 %7 %8 %9
)

IF x%1==xdist (
  mkdir dist\temp 2>NUL
  del /S /Q dist >NUL
  del /S /Q dist\temp >NUL

  copy /Y vendor\tc_java\tc-apis-1.7.jar dist\tc-apis-NG.jar >NUL
  %JAR% uf dist\tc-apis-NG.jar -C bin plugins
  rmdir /S /Q bin\plugins >NUL

  set PLUGIN_NAME=NtfsStreamsJ
  set PLUGIN_TYPE=WDX

  set JAR_NAME=%PLUGIN_NAME%.jar
  set ZIP_NAME=%PLUGIN_NAME%.zip

  %JAR% cf "dist\temp\%JAR_NAME%" -C bin .

  copy vendor\tc_java\rename-me.w_x "dist\temp\%PLUGIN_NAME%.%PLUGIN_TYPE%" >NUL
  copy vendor\tc_java\license.txt dist\temp\ >NUL
  copy vendor\tc_java\errormessages.ini dist\temp\ >NUL
  copy vendor\tc_java\tc_javaplugin.ini.stub dist\temp\tc_javaplugin.ini >NUL

  echo [%PLUGIN_TYPE%]>>dist\temp\tc_javaplugin.ini
  echo CLASS=%PLUGIN_NAME%>>dist\temp\tc_javaplugin.ini

  echo [plugininstall]>>dist\temp\pluginst.inf
  echo description=%PLUGIN_TYPE% plugin %PLUGIN_NAME%>>dist\temp\pluginst.inf
  echo type=%PLUGIN_TYPE%>>dist\temp\pluginst.inf
  echo file=%PLUGIN_NAME%.%PLUGIN_TYPE%>>dist\temp\pluginst.inf
  echo defaultdir=%PLUGIN_NAME%>>dist\temp\pluginst.inf

  %JAR% cMf "dist\%ZIP_NAME%" -C dist\temp .
  %JAR% uMf "dist\%ZIP_NAME%" -C dist tc-apis-NG.jar

  rmdir /S /Q dist\temp
  GOTO DONE


)
:DONE
