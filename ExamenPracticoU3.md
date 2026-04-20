# CASO 4

- **Título**: Exponer y mostrar los cupos disponibles de cada evento
- **Objetivo del cambio**: Añadir una mejora de lectura rápida para estudiantes y admins: además de “inscritos/capacidad”, mostrar explícitamente los cupos restantes.
- **Cambio mínimo requerido en backend**:
- Agregar `cuposDisponibles` en `EventoResponse` y en la respuesta de detalle de evento.
- **Cambio mínimo requerido en frontend**:
- Mostrar el número de cupos disponibles en tarjetas de evento y/o en el detalle.
- **Archivos o zonas del proyecto probablemente involucradas**:
- `backend/eventnode-api/src/main/java/com/eventnode/eventnodeapi/dtos/EventoResponse.java`
- `backend/eventnode-api/src/main/java/com/eventnode/eventnodeapi/controllers/EventoController.java`
- `web-app/src/components/EventCard.jsx`
- `web-app/src/pages/student/StudentEventDetail.jsx`
- `web-app/src/pages/admin/GestionEventos.jsx`
- **Pasos esperados de implementación**:
1. Calcular `capacidadMaxima - inscritos` en backend, asegurando no devolver negativos.
2. Incorporar el campo en listados y detalle.
3. Mostrar el dato en frontend en una zona clara del componente.
4. Verificar visualmente casos con cupos > 0 y evento lleno.
- **Cómo ejecutar la regresión de pruebas existentes**:
- `cd backend/eventnode-api && mvn test -q -Dtest=EventoResponseTest,EventoControllerTest`
- **Cómo demostrar el cambio en vivo**:
- Abrir listado de eventos.
- Mostrar un evento con cupos restantes y otro lleno.
- Validar que el número coincide con inscritos/capacidad.
- **Criterios de evaluación docente**:
- El campo sale del backend, no se improvisa solo en frontend.
- El cálculo es correcto y robusto.
- La UI no duplica información de forma confusa.
- **Errores comunes esperados**:
- Devolver valores negativos.
- Calcular distinto en distintas pantallas.
- Romper el constructor o serialización de `EventoResponse`.