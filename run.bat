@ECHO OFF

SETLOCAL EnableExtensions DisableDelayedExpansion
for /F %%a in ('echo prompt $E ^| cmd') do (
  set "ESC=%%a"
)


REM getting the number of instances to run for each service based on the CLI arguments
SET instancesR=%1
SET instancesL=%2
SET instancesB=%3
SET instancesP=%4
GOTO :main

REM Define the startService function.
:startService
    SETLOCAL ENABLEDELAYEDEXPANSION
    set project=%1
    set instances=%2
    set isMicroService=%3
    for /L %%x in (1,1,%instances%) do (
        START /b cmd /c "cd %project% && mvn spring-boot:run >nul 2>&1"
    )
    ECHO    Started %project%.
    IF %isMicroService% EQU true (
        ECHO            Instances : %instances%.
    )
    ENDLOCAL
EXIT /B 0

REM Define green echo function.
:GECHO
    SETLOCAL ENABLEDELAYEDEXPANSION
    echo %ESC%[32m%~1%ESC%[0m
    ENDLOCAL
EXIT /B 0

REM Define header credit logs.
:credits
    ECHO:
    CALL :GECHO "PrixBanque - PP3 - 8INF853"
    ECHO:
    CALL :GECHO "   Yann COTINEAU"
    CALL :GECHO "   Brayan SILLIAU"
    CALL :GECHO "   Aurelien TRICARD"
EXIT /B 0

REM Define the main function.
:main
CALL :credits
ECHO:
ECHO ---------------------------------------------------
ECHO:
CALL :GECHO "Starting server ..."
CALL :startService discovery-service 1 false
CALL :startService api-gateway 1 false
ECHO:
CALL :GECHO "Starting microservices ..."
CALL :startService registration-service %instancesR% true
CALL :startService login-service %instancesL% true
CALL :startService balance-service %instancesB% true
CALL :startService payment-service %instancesP% true
ECHO:
ECHO ---------------------------------------------------
ECHO:
SET /p Input=Press any key to stop:
ECHO Stopping all services...
TASKKILL /f /im java.exe >nul 2>&1
ECHO All services stopped !
