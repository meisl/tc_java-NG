@echo off
call build.bat
%JAVA_HOME%\bin\java -cp %MY_CLASS_PATH%;%JAR_NAME% %1 %2 %3 %4 %5 %6 %7 %8 %9