@ECHO OFF

CALL :FIND_JAVA_AND_TC
IF ERRORLEVEL 1 (
  EXIT /B 1
)
IF "%1"=="init" (
  EXIT /B 0
)


set MY_DIR=%CD%

set MY_CLASS_PATH=vendor\tc_java\tc-apis-1.7.jar;%JAVALIB%\swt-win32-3.1.2.jar;%JAVALIB%\commons-logging-api-1.0.4.jar

mkdir bin 2>NUL
del /S /Q bin >NUL
mkdir dist 2>NUL
del dist\tc-apis-NG.jar 2>NUL

ECHO compiling tc-apis-NG...

javac -Xlint -cp %MY_CLASS_PATH% -sourcepath src\java -d bin src/java/plugins/wdx/*.java
IF ERRORLEVEL 1 (
  ECHO tc-apis-NG failed!
  GOTO DONE
)

COPY /Y vendor\tc_java\tc-apis-1.7.jar dist\tc-apis-NG.jar >NUL
jar uf dist\tc-apis-NG.jar -C bin plugins
RMDIR /S /Q bin\plugins >NUL 2>&1


IF "%1"=="jdoc" (
  ECHO creating javadoc in doc\api\...
  RMDIR /S /Q "%MY_DIR%\doc\api" >NUL 2>&1
  MKDIR "%MY_DIR%\doc\api" 2>NUL
  javadoc -d doc\api -quiet -classpath %MY_CLASS_PATH% -use -author -windowtitle "tc_java API" -doctitle "Total Commander Plugin Interface API" -sourcepath src\java;vendor\tc_java\tc-apis-1.7.jar -subpackages plugins
)

IF "%1"=="dist" (
  DEL /S /Q "%MY_DIR%\dist\*.zip" >NUL 2>&1
  RMDIR /S /Q "%MY_DIR%\dist\temp" >NUL 2>&1
  COPY /Y templates\dist-README-0.md "%MY_DIR%\dist\README.md" >NUL

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

      REM note the ^( and ^), they're escapes
      ECHO * [%%i]^(http://github.com/meisl/tc_java-NG/blob/master/dist/%%i.zip?raw=true^), %PLUGIN_TYPE%: TODO: description>>"%MY_DIR%\dist\README.md"

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

      jar cMf "%MY_DIR%\dist\%%i.zip" -C "%MY_DIR%\dist\temp" .
      jar uMf "%MY_DIR%\dist\%%i.zip" -C "%MY_DIR%\dist" tc-apis-NG.jar

      RMDIR /S /Q "%MY_DIR%\dist\temp" >NUL 2>&1
    )
    CD ..
  )
  TYPE "%MY_DIR%\templates\dist-README-1.md" >>"%MY_DIR%\dist\README.md"

)

:DONE
CD "%MY_DIR%"
EXIT /B 0


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
