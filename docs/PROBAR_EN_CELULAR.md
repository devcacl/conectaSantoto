# Probar la app en tu celular Android

Ya tienes instalado JDK 17, Android SDK y `adb` (en el `Path` de usuario; abre una terminal nueva).

## 1. Preparar el celular (una sola vez)
1. Ajustes → Acerca del teléfono → toca **Número de compilación** 7 veces (activa "Opciones de desarrollador").
2. Ajustes → Sistema → **Opciones de desarrollador** → activa **Depuración por USB**.
3. Conecta el cable USB (usa un cable de datos). En el celular acepta **"¿Permitir depuración USB?"** (marca "Permitir siempre desde este equipo").
4. Si Windows no lo reconoce: instala el driver del fabricante (Samsung, Xiaomi, Motorola...) o el *Google USB Driver*.

Verifica:
```
adb devices
```
Debe aparecer tu dispositivo como `device` (no `unauthorized`).

## 2. Compilar e instalar
Desde la raíz del proyecto:
```
.\gradlew.bat installDebug
```
Instala y deja la app lista en el celular ("Santoto Admisiones"). Para abrirla desde la PC:
```
adb shell monkey -p com.aistudio.santototunja.admxpz -c android.intent.category.LAUNCHER 1
```

## 3. Alternativa sin cable (Android 11+)
1. Celular y PC en la misma red Wi-Fi.
2. Opciones de desarrollador → **Depuración inalámbrica** → activar → "Vincular dispositivo con código".
3. En la PC: `adb pair IP:PUERTO_DE_VINCULACION` e ingresa el código.
4. `adb connect IP:PUERTO` (el puerto que muestra la pantalla principal de Depuración inalámbrica) y luego `.\gradlew.bat installDebug`.

## 4. Ciclo de trabajo tipo "AI Studio"
- Pídele cambios a Claude Code → él compila → ejecuta `.\gradlew.bat installDebug` → la app se actualiza en tu celular.
- Ver logs en vivo: `adb logcat --pid=$(adb shell pidof -s com.aistudio.santototunja.admxpz)`
- Capturar pantalla: `adb exec-out screencap -p > captura.png`

## 5. Configuración para que todo funcione
En `.env` (raíz, no se sube a git):
- `GEMINI_API_KEY` y `COHERE_API_KEY`: claves reales para el asesor IA. Sin ellas el chatbot no responderá con IA.
- `ADMIN_PIN`: PIN para entrar al panel de Admisiones (ícono de administrador en el encabezado).
Después de editar `.env`, vuelve a ejecutar `installDebug`.

## Problemas frecuentes
- `adb` no se reconoce → abre una terminal nueva (el `Path` se actualizó después).
- `INSTALL_FAILED_UPDATE_INCOMPATIBLE` → desinstala la versión anterior del celular (distinta firma).
- Celular `unauthorized` → revoca autorizaciones USB en Opciones de desarrollador y reconecta.
