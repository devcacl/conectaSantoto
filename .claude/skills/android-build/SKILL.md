---
name: android-build
description: Compilar, probar, lintear e instalar la app Android conectaSantoto con Gradle en Windows, y diagnosticar fallos de build (JDK, SDK, wrapper, keystore, .env).
---

# android-build

Ejecutar desde la raíz del repo con PowerShell.

1. Prerrequisitos: `java -version` (17+), `$env:ANDROID_HOME` apuntando al SDK, `local.properties` con `sdk.dir`, `.env` (copiar de `.env.example`), `debug.keystore` en la raíz (ver CLAUDE.md), y `gradlew.bat` (si falta: `gradle wrapper --gradle-version 9.3.1`).
2. Compilar: `.\gradlew.bat assembleDebug`
3. Tests: `.\gradlew.bat testDebugUnitTest` (un test: `--tests "com.example.ExampleUnitTest"`)
4. Lint: `.\gradlew.bat lint` — reporte en `app\build\reports\lint-results-debug.html`
5. Instalar: `adb devices` y luego `adb install -r app\build\outputs\apk\debug\app-debug.apk`

Diagnóstico frecuente:
- `SDK location not found` → crear `local.properties` con `sdk.dir=C\:\\Users\\<user>\\AppData\\Local\\Android\\Sdk`
- `Failed to find Platform SDK with path: platforms;android-36.1` → `sdkmanager "platforms;android-36.1" "build-tools;36.0.0"` (ver compileSdk en `app/build.gradle.kts`)
- `Keystore file ... debug.keystore not found` → generarlo con el comando de CLAUDE.md
- Errores de KSP/Room → revisar entidades en `@Database` y versión
- Si falla el configuration cache, repetir con `--no-configuration-cache` para ver el error real

Reportar siempre la salida real del build; no afirmar éxito sin ver `BUILD SUCCESSFUL`.
