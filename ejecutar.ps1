# Script para compilar y ejecutar PhysioTrack en Windows (PowerShell)

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "PhysioTrack - Sistema de Fisioterapia" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que Maven esté instalado
$mvnCheck = Get-Command mvn -ErrorAction SilentlyContinue
if (-not $mvnCheck) {
    Write-Host "ERROR: Maven no está instalado o no está en el PATH" -ForegroundColor Red
    Write-Host "Por favor, instala Maven desde: https://maven.apache.org/" -ForegroundColor Yellow
    Read-Host "Presiona Enter para salir"
    exit 1
}

# Verificar que Java esté instalado
$javaCheck = Get-Command java -ErrorAction SilentlyContinue
if (-not $javaCheck) {
    Write-Host "ERROR: Java no está instalado o no está en el PATH" -ForegroundColor Red
    Write-Host "Por favor, instala Java 17 o superior" -ForegroundColor Yellow
    Read-Host "Presiona Enter para salir"
    exit 1
}

Write-Host "Verificando Java version..." -ForegroundColor Green
java -version

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "Compilando el proyecto con Maven..." -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptPath

mvn clean install

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: La compilación falló" -ForegroundColor Red
    Read-Host "Presiona Enter para salir"
    exit 1
}

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "Ejecutando la aplicación..." -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "La aplicación estará disponible en: http://localhost:8080" -ForegroundColor Green
Write-Host ""

mvn spring-boot:run

Read-Host "Presiona Enter para salir"

