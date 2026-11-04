# CLAUDE.md — Santoto Admisiones (conectaSantoto)

App Android de Admisiones de la Universidad Santo Tomás Seccional Tunja. Kotlin + Jetpack Compose (Material 3), MVVM, Room, RAG con Gemini/Cohere. El README.md describe funcionalidades; este archivo describe cómo trabajar en el código.

## Datos clave
- `applicationId`: `com.aistudio.santototunja.admxpz`, namespace/paquete de código: `com.example`
- AGP 9.1.1, Kotlin 2.2.10, KSP, compileSdk 36.1, minSdk 24, targetSdk 36 (versiones en `gradle/libs.versions.toml`)
- Requiere **JDK 17+** (el código compila a Java 11). `org.gradle.configuration-cache=true`
- Un solo módulo: `:app`. Idioma de UI y textos: español (Colombia)

## Comandos (Windows, desde la raíz)
```
.\gradlew.bat assembleDebug          # compilar
.\gradlew.bat testDebugUnitTest      # tests unitarios (JUnit + Robolectric)
.\gradlew.bat lint                   # lint
.\gradlew.bat connectedDebugAndroidTest   # requiere emulador/dispositivo
adb install -r app\build\outputs\apk\debug\app-debug.apk
```
Nota: el repo NO incluía `gradlew`/`gradle-wrapper`; si falta, generarlo con `gradle wrapper --gradle-version 9.3.1` (AGP 9.1.1 requiere Gradle >= 9.3.1) y commitearlo.

## Arquitectura (`app/src/main/java/com/example/`)
- `MainActivity.kt` — Scaffold, header, bottom bar y `when (screen)` de navegación propia (sealed `Screen`, estado en el ViewModel; **no usa navigation-compose**)
- `viewmodel/SantotoViewModel.kt` — ViewModel único con `StateFlow`
- `data/model/` — entidades Room (Lead, CampusVisit, CampusEvent, CampusNews, EventRegistration, RagDocument…)
- `data/local/` — `SantotoDatabase` (versión 5, `fallbackToDestructiveMigration`, `exportSchema=false`) y `SantotoDao`
- `data/repository/SantotoRepository.kt` — repositorio + seed de datos
- `data/rag/` — motor RAG y base de conocimiento por defecto; `data/remote/` — `GeminiClient`, `CohereClient`
- `data/remote/AdmissionsBackend.kt` — puente con Firestore. Aspirantes: envía leads, visitas e inscripciones con sesión anónima y bandeja de salida persistente con reintento (solo **crean** documentos). Admisiones: login con Firebase Auth (correo/contraseña + documento `admins/{uid}`), escucha en tiempo real y escribe de vuelta estados/asistencia/borrados. Room es la fuente local; las entidades sincronizadas tienen `remoteId` (null = dato de ejemplo local, nunca se sube ni se borra)
- `firestore.rules` / `firebase.json` / `.firebaserc` — reglas y config de Firebase (proyecto `santoto-admisiones-tunja`). Desplegar con `firebase deploy --only firestore` o el MCP de Firebase
- `ui/screens/`, `ui/components/`, `ui/theme/` — Compose. Paleta real (`Color.kt`): Azul `#003865`, Amarillo `#FFC72C`, Fondo `#F8FAFC`. Usar tokens (`SantotoOutline`, `SantotoTextBody`, `SantotoTextSecondary`…) y estilos de `MaterialTheme.typography`; no colores `Color(0xFF…)` ni `fontSize` < 11sp sueltos

## Convenciones
- Compose + M3; reutilizar `SantotoCard`, `SantotoHeader`, `SantotoBottomBar` y colores de `ui/theme/Color.kt` antes de crear nuevos
- Nueva pantalla = archivo en `ui/screens/` + caso en `Screen` + título en el `when` de `MainActivity` + estado en el ViewModel
- Cambios de esquema Room: subir `version`; hoy se destruye la BD al migrar (tener en cuenta el seed)
- Strings visibles al usuario en español; preferir `strings.xml` para texto nuevo

## Secretos y seguridad (IMPORTANTE)
- `GEMINI_API_KEY` se lee de `.env` (copiar `.env.example` → `.env`, ignorado por git) vía secrets-gradle-plugin → `BuildConfig.GEMINI_API_KEY`
- Firma: `debug.keystore` (ignorado por git, **hay que generarlo**, ver abajo) y release vía env vars `KEYSTORE_PATH`, `STORE_PASSWORD`, `KEY_PASSWORD`
- `COHERE_API_KEY` también viene de `.env` → `BuildConfig`. La clave de Cohere antigua sigue en el historial de git: **debe rotarse** en el panel de Cohere. No escribir nuevas claves en código.
- `app/google-services.json` (config pública del cliente Firebase) se versiona; la seguridad real está en `firestore.rules`. Los administradores se autorizan con un documento `admins/{uid}` creado a mano en la consola de Firebase
- El `ADMIN_PIN` ya no se usa (excluido de BuildConfig con `ignoreList`); si queda en tu `.env` local, bórralo
- Nunca commitear `.env`, `*.jks`, `*.keystore`, `local.properties`

## Generar debug.keystore (una vez)
```
keytool -genkey -v -keystore debug.keystore -storepass android -alias androiddebugkey -keypass android -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=Android Debug,O=Android,C=US"
```

## Flujo de trabajo
Trabajar en ramas (`feat/...`, `fix/...`), commits convencionales (`feat:`, `fix:`, `chore:`), compilar y correr tests antes de commitear. Skills del proyecto en `.claude/skills/`.
