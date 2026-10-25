---
name: rag-knowledge
description: Editar la base de conocimiento y el motor RAG del asesor "Tommy Bot" (documentos institucionales, recuperación, clientes Gemini/Cohere) en conectaSantoto.
---

# rag-knowledge

Archivos: `data/rag/SantotoKnowledgeBase.kt` (contenido por defecto), `data/rag/SantotoRagEngine.kt` (recuperación), `data/model/RagDocument.kt`, `data/remote/GeminiClient.kt`, `data/remote/CohereClient.kt`, `ui/screens/ChatbotScreen.kt`.

Reglas:
- La información institucional (costos, requisitos, becas, fechas) debe venir del usuario o de documentos oficiales; no inventar datos. Si falta, preguntar.
- Documentos en Markdown, en español, con títulos claros para mejorar la recuperación.
- Claves de API: jamás en código. `GEMINI_API_KEY` va en `.env` (BuildConfig). La clave de Cohere hoy está hardcodeada en `CohereClient.kt`: al tocar ese archivo, migrarla a `.env`/BuildConfig y recomendar rotarla.
- Tras cambios, compilar y probar el flujo del chatbot en emulador si hay uno disponible.
