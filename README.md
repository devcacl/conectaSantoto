# Santoto Admisiones • Universidad Santo Tomás Seccional Tunja

Aplicación móvil oficial de **Admisiones, Mercadeo y Experiencia de Aspirantes** de la **Universidad Santo Tomás Seccional Tunja (Colombia)** para el periodo académico **2026 - 2027**. Desarrollada con tecnologías modernas de Android: **Kotlin**, **Jetpack Compose**, **Material Design 3**, arquitectura **MVVM**, persistencia reactiva con **Room Database** e inteligencia artificial con **RAG Dinámico**.

---

## 🏛️ Características Principales

### Para Aspirantes y Familias
1. **Asesor IA Tomasino ("Tommy Bot"):**
   - Motor RAG dinámico que responde consultas sobre carreras, costos, requisitos de matrícula, homologaciones y becas consultando documentos institucionales actualizados en tiempo real.
2. **Explora 360° & Modo VR:**
   - Recorrido virtual inmersivo por el Campus Central y Campus Avenida Universitaria con soporte de giroscopio de alta precisión, calibración de orientación vertical y horizontal, y modo VR para visores tipo Cardboard.
3. **Agendamiento de Visitas al Campus:**
   - Selección interactiva de fechas y franjas horarias con generación inmediata de **Pase Digital QR** para ingreso a portería.
4. **Test de Orientación Vocacional:**
   - Cuestionario de afinidades basado en el modelo RIASEC adaptado a la oferta académica de la Santoto Tunja con cálculo automático de programas recomendados.
5. **Oferta Académica 2026 - 2027:**
   - Catálogo interactivo de Pregrados, Especializaciones, Maestrías y el programa especial de **Microcredenciales para Grado 11°**.
6. **Eventos & Boletos Digitales Oficiales:**
   - Explorador de Open Days, talleres y ferias con control de cupos disponibles en tiempo real.
   - Proceso de inscripción directo: confirmación con datos del aspirante y generación instantánea del **Boleto Digital Oficial** con código único de acceso, código de barras digital y opciones para compartir.
7. **Portal de Noticias & Actualidad:**
   - Sala de prensa universitaria con filtros por categoría (*Admisiones, Colegios, Innovación & IA, Académico, Internacional*), búsqueda en tiempo real y lectura completa de artículos.

### Para el Equipo de Admisiones (Panel Administrativo)
- **Pipeline de Gestión de Leads:** Clasificación por estados (*Nuevo, Contactado, Visita Agendada, Documentos Presentados, Admitido, Matriculado*) con contacto rápido por WhatsApp y llamada telefónica.
- **Calendario Centralizado de Visitas:** Supervisión y confirmación de visitas guiadas programadas.
- **Control de Asistencia a Eventos:**
   - Visualización de la lista de asistentes registrados en tiempo real.
   - Marcación de llegada (*"Llegó ✓"* / *"Registrar Llegada"*) con un solo toque.
   - Registro de asistentes en puerta (*Walk-in*) con check-in inmediato.
   - Envío de recordatorios y confirmación vía WhatsApp con boleto asignado.
- **Gestión de Contenido de Noticias:** Creación, edición y eliminación de publicaciones institucionales.
- **Base de Conocimiento Tommy Bot (.md):** Módulo CMS para agregar, editar o restaurar documentos en formato Markdown que alimentan las respuestas del bot.
- **Botón Flotante Contextual (+):** Botón flotante inteligente que adapta su acción automáticamente según la pestaña seleccionada (agregar lead, agendar visita, crear evento, redactar noticia o crear documento .md).

---

## 🛠️ Stack Tecnológico & Arquitectura

- **Lenguaje:** Kotlin 1.9+
- **Interfaz de Usuario:** Jetpack Compose con Material Design 3 (M3)
- **Patrón Arquitectural:** MVVM (Model-View-ViewModel) con Clean Architecture
- **Flujos y Concurrencia:** Kotlin Coroutines & `StateFlow` reactivo
- **Persistencia Local:** Android Jetpack Room (SQLite) con DAOs tipados
- **Carga de Imágenes:** Coil Compose
- **Integración Web & Sensores:** Android `WebView` con puente bidireccional JavaScript, `SensorManager` y eventos W3C `DeviceOrientation`
- **Paleta Institucional:**
  - **Azul Tomasino:** `#002B49`
  - **Oro Tomasino:** `#D1A153`
  - **Amarillo Tomasino:** `#FFD100`
  - **Fondo Suave:** `#F8FAFC`

---

## 📂 Estructura del Proyecto

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/
│   ├── MainActivity.kt                # Navegación y Scaffold principal
│   ├── data/
│   │   ├── local/                     # Room Database, DAO y TypeConverters
│   │   ├── model/                     # Entidades de datos (Lead, CampusVisit, CampusEvent, CampusNews, RagDocument)
│   │   ├── rag/                       # Motor RAG y base de conocimiento por defecto
│   │   └── repository/                # Repositorio centralizado con seed de datos
│   ├── ui/
│   │   ├── components/                # Componentes reutilizables (Dock BottomBar, Header, Cards)
│   │   ├── screens/                   # Pantallas de la aplicación:
│   │   │   ├── HomeScreen.kt
│   │   │   ├── ChatbotScreen.kt
│   │   │   ├── Explora360Screen.kt
│   │   │   ├── EventsScreen.kt
│   │   │   ├── EventRegistrationScreen.kt
│   │   │   ├── EventTicketScreen.kt
│   │   │   ├── NewsScreen.kt
│   │   │   ├── ScheduleVisitScreen.kt
│   │   │   ├── VocationalTestScreen.kt
│   │   │   ├── ProgramsCatalogScreen.kt
│   │   │   └── AdmissionsDashboardScreen.kt
│   │   └── theme/                     # ColorScheme institucional, Tipografía y Shapes M3
│   └── viewmodel/
│       └── SantotoViewModel.kt        # ViewModel unificado con StateFlow
└── res/
    ├── drawable/                      # Iconos vectoriales y recursos gráficos
    ├── mipmap-*/                      # Iconos adaptativos de la aplicación
    └── values/
        ├── strings.xml                # Nombre y cadenas oficiales
        └── themes.xml                 # Tema base Android
```

---

## 🚀 Requisitos Previos & Ejecución

### Requisitos
- **Android Studio:** Iguana (2023.2.1), Jellyfish, Koala o superior.
- **JDK:** Java 17 o superior.
- **Android SDK:** Compile SDK 34, Min SDK 26 (Android 8.0+).

### Clonar e Instalar
```bash
# Clonar el repositorio
git clone <URL_DE_TU_REPOSITORIO_GITHUB>
cd <CARPETA_DEL_PROYECTO>

# Compilar la aplicación en modo Debug
./gradlew assembleDebug

# O usando Gradle directamente:
gradle assembleDebug
```

---

## 🔒 Variables de Entorno & Seguridad

Este proyecto utiliza el plugin de secretos de Gradle para proteger credenciales sensibles.
- Las variables se gestionan a través de `.env` (ignorado en el control de versiones).
- Consulta `.env.example` para conocer las variables disponibles:
  ```env
  GEMINI_API_KEY=TU_LLAVE_AQUI
  ```

---

## 📄 Licencia y Derechos

© 2026 Universidad Santo Tomás, Seccional Tunja. Todos los derechos reservados.
Uso oficial para la Dirección de Admisiones y Mercadeo.
