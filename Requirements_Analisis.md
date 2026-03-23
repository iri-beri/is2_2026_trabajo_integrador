# Requerimientos del Sistema

## 1. Problema a Resolver

Actualmente, la gestión académica de la universidad presenta múltiples inconvenientes:

- Uso de **planillas y sistemas aislados**
- Falta de **integración entre módulos**
- Procesos **manuales y propensos a errores**
- Dificultad para validar:
  - Correlatividades
  - Inscripciones
- Baja **transparencia** para estudiantes y docentes
- Comunicación ineficiente entre actores

### Objetivo del sistema

Diseñar e implementar una plataforma web que permita gestionar la información académica de forma:

- Centralizada
- Consistente
- Segura
- Escalable
- Fácil de usar

El sistema deberá cubrir la gestión de estudiantes, docentes, materias, inscripciones y desempeño académico.

---

## 2. Usuarios del Sistema

### 2.1 Estudiante
- Consulta de información académica
- Inscripción a materias
- Visualización de historial y progreso

### 2.2 Profesor
- Consulta de materias asignadas
- Visualización de alumnos
- Carga de notas

### 2.3 Administrador Académico
- Gestión integral del sistema
- Administración de datos académicos

---

## 3. Funcionalidades

### 3.1 Gestión de Estudiantes
- Registro de datos personales
- Información de contacto
- Estado académico (ingresante / avanzado)
- Consulta de historial académico
- Visualización de materias cursadas y aprobadas

---

### 3.2 Gestión de Docentes
- Registro de docentes
- Asignación a materias
- Definición de rol docente:
  - Responsable de cátedra
  - JTP
  - Ayudante

---

### 3.3 Gestión Académica

#### Carreras y Planes de Estudio
- Definición de carreras
- Gestión de planes de estudio
- Asociación de materias a planes

#### Materias
- Alta, baja y modificación
- Definición de cupos
- Asociación a carreras

#### Correlatividades
- Definición de requisitos entre materias
- Validación automática al momento de inscripción

---

### 3.4 Inscripciones y Cursadas
- Inscripción a materias
- Validación de:
  - Correlativas
  - Cupos
- Registro de cursadas activas
- Consulta de estado de inscripción

---

### 3.5 Gestión de Notas
- Registro de notas finales
- Historial académico del estudiante
- Consulta de rendimiento

---

### 3.6 Asignación Académica
- Asignación de docentes a materias
- Asociación a períodos académicos

---

### 3.7 Consultas
- Materias disponibles
- Materias cursadas
- Listado de alumnos por materia
- Información académica del estudiante

---

### 3.8 Funcionalidades Futuras (No Funcionales / Analítica)
- Seguimiento de progreso académico
- Detección de riesgo de abandono
- Identificación de estudiantes destacados

---

## 4. Restricciones Técnicas

- Backend orientado a objetos (requisito académico)
- Arquitectura cliente-servidor
- Persistencia en base de datos relacional
- Aplicación web

---

## 5. Tamaño del Equipo

- 4 Integrantes en el momento que se escribe este documento.

---

## 6. Tecnologías Elegidas y Justificación

### Frontend
**Java+Mustache**
- Motor de plantillas
- HTML dinámico a partir de templates+datos
- Arquitectura de vistas separadas de lógica backend
### Backend
**Java + Maven**
- Orientado a objetos
- Fuertemente tipado
- Conservación de tecnologías usadas en IS-I
- Integración con BD

### Base de Datos
**SQLite**
- Adecuada para proyectos academicos
- Relacional
- Adecuada para integridad referencial

---

## 7. Plazo Estimado

- A definir

---

## 8. Cambios de Alcance

- Evolutivo durante el desarrollo

---

## 9. Problemas Encontrados

- Se documentarán durante el proceso

---

## 10. Organización del Equipo

- Metodología Kanban
- Uso de Git
- Gestión mediante Issues
- Trazabilidad mediante SRS