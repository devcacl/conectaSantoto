---
name: compose-screen
description: Crear o modificar una pantalla Jetpack Compose (Material 3) en conectaSantoto siguiendo la navegación propia, el ViewModel único y la paleta institucional.
---

# compose-screen

Antes de escribir, leer una pantalla existente similar (p. ej. `ui/screens/NewsScreen.kt`) y `MainActivity.kt`.

Pasos para una pantalla nueva:
1. Crear `ui/screens/<Nombre>Screen.kt` con un `@Composable` que reciba el `SantotoViewModel` o solo los estados/callbacks necesarios (preferir state hoisting).
2. Agregar el destino al sealed `Screen` (buscar su definición con Grep `sealed .*Screen`).
3. Añadir el título en el `when (currentScreen)` de `MainActivity.kt` y el caso en el `when (screen)` que renderiza.
4. Si necesita datos: exponer `StateFlow` en `SantotoViewModel` y función en `SantotoRepository`/`SantotoDao`.
5. Ajustar visibilidad de bottom bar/botón atrás si aplica.

Reglas de UI: Material 3, colores de `ui/theme/Color.kt` (Azul #002B49, Oro #D1A153, Amarillo #FFD100, Fondo #F8FAFC), reutilizar `SantotoCard`/`SantotoHeader`, textos en español, `collectAsStateWithLifecycle()`, añadir `@Preview` para pantallas nuevas. Verificar con `android-build` (assembleDebug).
