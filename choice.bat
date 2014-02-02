@ECHO OFF

SETLOCAL
CALL :CHOICE "Choose type of plugin: " "WLX (lister)" "WCX (packer)" "WDX (content)" "WFX (file system)"
rem
ECHO RESULT=%RESULT%
EXIT /B 0

:CHOICE
  SETLOCAL DISABLEDELAYEDEXPANSION
  SET /A N=0
  SET Q=%1
  SHIFT

  CALL :CHOICE_PRINT %1
  IF ERRORLEVEL 1 (
    ECHO need at least one option!
    ENDLOCAL&EXIT /B 1
  )
  
  
  CALL :CHOICE_PRINT %2
  IF ERRORLEVEL 1 GOTO CHOICE_PRINTDONE
  
  CALL :CHOICE_PRINT %3
  IF ERRORLEVEL 1 GOTO CHOICE_PRINTDONE
  
  CALL :CHOICE_PRINT %4
  IF ERRORLEVEL 1 GOTO CHOICE_PRINTDONE
  
  CALL :CHOICE_PRINT %5
  IF ERRORLEVEL 1 GOTO CHOICE_PRINTDONE
  
  IF NOT x%6==x (
      ECHO cannot handle more than 5 options!
    ENDLOCAL&EXIT /B 1
  )

:CHOICE_PRINTDONE
  ECHO.
:CHOICE_ASK
  SET /P choice=%Q:~1,-1%
  IF %choice% LSS 1 (
    ECHO Invalid choice %choice%, try again!
    GOTO :CHOICE_ASK
  )
  IF %choice% GTR %N% (
    ECHO Invalid choice %choice%, try again!
    GOTO :CHOICE_ASK
  )
  ENDLOCAL&SET RESULT=%choice%
  EXIT /B 0

:CHOICE_PRINT
  IF x%1==x EXIT /B 1
  SET X=%1
  SET /A N=%N%+1
  ECHO   %N%: %X:~1,-1%
  EXIT /B 0
