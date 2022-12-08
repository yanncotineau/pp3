@echo off

echo server launch :

cd discovery-service
start "" /b mvn spring-boot:run >nul 2>&1
echo       discovery-service started.

cd ../api-gateway
start "" /b mvn spring-boot:run >nul 2>&1
echo       api-gateway started.

cd ../login-service
start "" /b mvn spring-boot:run >nul 2>&1
echo       login-service started.

set /p Input=Press any key to stop: