@ECHO OFF

SET PLUGIN_NAME=%1
IF NOT EXIST "example-plugins\%PLUGIN_NAME%" (
  echo invalid arg: plugin name "%PLUGIN_NAME%"
  GOTO DONE
)

SET MAIN_CLASS=%2
IF "%MAIN_CLASS%"=="" (
  echo missing arg: main class
  GOTO DONE
)

SET MY_DIR=%CD%

CALL build.bat
IF ERRORLEVEL 1 (
  GOTO DONE
)

CD "example-plugins\%PLUGIN_NAME%"
CALL build.bat
IF ERRORLEVEL 1 (
  GOTO DONE
)

CD "%MY_DIR%"
%JAVA_HOME%\bin\java -cp %MY_CLASS_PATH%;example-plugins\%PLUGIN_NAME%\dist\%PLUGIN_NAME%.jar %2 %3 %4 %5 %6 %7 %8 %9

:DONE
CD "%MY_DIR%"
