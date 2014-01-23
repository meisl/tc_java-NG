@ECHO OFF
set MY_DIR=%CD%

set JAVA_HOME=c:\Programme\Java\jdk1.7.0_25
set JAVALIB=%COMMANDER_PATH%\javalib

set MY_CLASS_PATH=vendor\tc_java\tc-apis-1.7.jar;%JAVALIB%\swt-win32-3.1.2.jar;%JAVALIB%\commons-logging-api-1.0.4.jar
set JAR=%JAVA_HOME%\bin\jar
set JDOC=%JAVA_HOME%\bin\javadoc

mkdir bin 2>NUL
del /S /Q bin >NUL
mkdir dist 2>NUL
del dist\tc-apis-NG.jar 2>NUL

ECHO compiling tc-apis-NG in %MY_DIR%...

%JAVA_HOME%\bin\javac -Xlint -cp %MY_CLASS_PATH% -sourcepath src\java -d bin src/java/plugins/wdx/*.java
IF ERRORLEVEL 1 (
  GOTO DONE
)

copy /Y vendor\tc_java\tc-apis-1.7.jar dist\tc-apis-NG.jar >NUL
%JAR% uf dist\tc-apis-NG.jar -C bin plugins
rmdir /S /Q bin\plugins >NUL 2>&1


IF "%1"=="jdoc" (
  ECHO creating javadoc in doc\api\...
  RMDIR /S /Q "%MY_DIR%\doc\api" >NUL 2>&1
  MKDIR "%MY_DIR%\doc\api" 2>NUL
  %JDOC% -d doc\api -quiet -classpath %MY_CLASS_PATH% -use -author -windowtitle "tc_java API" -doctitle "Total Commander Plugin Interface API" -sourcepath src\java;vendor\tc_java\tc-apis-1.7.jar -subpackages plugins
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