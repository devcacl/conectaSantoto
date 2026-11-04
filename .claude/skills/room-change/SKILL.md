---
name: room-change
description: Modificar el esquema de datos Room de conectaSantoto (entidades, DAO, versión de BD, seed del repositorio) de forma segura.
---

# room-change

Archivos: `data/model/*.kt` (entidades), `data/local/SantotoDao.kt`, `data/local/SantotoDatabase.kt` (versión actual 4, `fallbackToDestructiveMigration`), `data/repository/SantotoRepository.kt` (seed).

Pasos:
1. Editar/crear la entidad en `data/model/`.
2. Si es entidad nueva, registrarla en `entities = [...]` de `@Database`.
3. Agregar consultas al DAO (devolver `Flow` para datos observables).
4. Incrementar `version` en `@Database`. Hoy la migración destruye datos: avisar al usuario; si se necesita conservar datos, escribir una `Migration` explícita y quitar el fallback destructivo solo con su aprobación.
5. Actualizar el seed en el repositorio y exponer en el ViewModel si la UI lo necesita.
6. Compilar (KSP genera el código de Room) y correr tests: `.\gradlew.bat testDebugUnitTest`.
