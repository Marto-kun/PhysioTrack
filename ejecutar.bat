@echo off
REM Script para compilar y ejecutar PhysioTrack en Windows

echo ============================================
echo PhysioTrack - Sistema de Fisioterapia
echo ============================================
echo.

REM Verificar que Maven esté instalado
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven no está instalado o no está en el PATH
    echo Por favor, instala Maven desde: https://maven.apache.org/
    pause
    exit /b 1
)

REM Verificar que Java esté instalado
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java no está instalado o no está en el PATH
    echo Por favor, instala Java 17 o superior
    pause
    exit /b 1
)

echo Verificando Java version...
java -version

echo.
echo ============================================
echo Compilando el proyecto con Maven...
echo ============================================
echo.

cd /d "%~dp0"
call mvn clean install

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: La compilación falló
    pause
    exit /b 1
)

echo.
echo ============================================
echo Ejecutando la aplicación...
echo ============================================
echo.
echo La aplicación estará disponible en: http://localhost:8080
echo.

call mvn spring-boot:run

pause

