@ECHO OFF
SET MY_DIR=%CD%

SET PLUGIN_DIR=example-plugins\%1
SET TC_API=%CD%\dist\tc-apis-NG.jar
SET PLUGIN_NAME=%1

IF NOT EXIST "%PLUGIN_DIR%" (
  ECHO no such plugin: "%PLUGIN_DIR%"
  GOTO DONE
)

SET MAIN_CLASS=%2
IF "%MAIN_CLASS%"=="" (
  ECHO missing arg: main class
  GOTO DONE
)

CALL build.bat
IF ERRORLEVEL 1 (
  GOTO DONE
)

CD "%PLUGIN_DIR%"
CALL build.bat
IF ERRORLEVEL 1 (
  GOTO DONE
)

CD "%MY_DIR%"
%JAVA_HOME%\bin\java -cp %MY_CLASS_PATH%;%TC_API%;%PLUGIN_DIR%\dist\%PLUGIN_NAME%.jar %2 %3 %4 %5 %6 %7 %8 %9

:DONE
CD "%MY_DIR%"
