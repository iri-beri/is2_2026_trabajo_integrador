# Software Requirements Specification (SRS)
## Sistema de Gestión Académica

---

## 1. Introducción

### 1.1 Propósito

El presente documento especifica los requerimientos del Sistema de Gestión Académica, cuyo objetivo es centralizar y optimizar la administración de la información académica de una universidad.

Este documento está dirigido a:
- Equipo de desarrollo
- Stakeholders académicos
- Evaluadores del proyecto

---

### 1.2 Alcance

El sistema permitirá gestionar:

- Estudiantes
- Docentes
- Carreras y planes de estudio
- Materias y correlatividades
- Inscripciones y cursadas
- Notas y rendimiento académico

El sistema será una aplicación web basada en arquitectura cliente-servidor con persistencia en base de datos relacional.

---

### 1.3 Definiciones, Acrónimos y Abreviaturas

- **SRS**: Software Requirements Specification  
- **RF**: Requisito Funcional  
- **RNF**: Requisito No Funcional  
- **CRUD**: Create, Read, Update, Delete  
- **Correlatividad**: Requisito académico que exige la aprobación previa de otra materia  
- **Cursada**: Instancia en la que un estudiante está inscripto en una materia en un período determinado  

---

### 1.4 Referencias

- Formato de documento IEEE 830 / ISO/IEC/IEEE 29148  
- Documentación de tecnologías utilizadas (Java, Mustache, Maven, SQLite, Git)

---

### 1.5 Visión General del Documento

El documento se organiza en:
- Descripción general del sistema
- Requisitos específicos (funcionales y no funcionales)

---

## 2. Descripción General

### 2.1 Perspectiva del Producto

El sistema reemplazará procesos manuales y sistemas fragmentados actualmente utilizados por la Oficina de Alumnos.

Se integrará como una solución centralizada para la gestión académica.

---

### 2.2 Funciones del Producto

El sistema deberá:

- Gestionar estudiantes y docentes
- Administrar materias y planes de estudio
- Validar correlatividades automáticamente
- Permitir inscripción a materias
- Registrar notas
- Proveer consultas académicas

---

### 2.3 Características de los Usuarios

| Tipo de Usuario            | Descripción |
|----------------------------|------------|
| Estudiante                 | Consulta su información académica e inscripciones |
| Profesor                   | Gestiona alumnos y registra notas |
| Administrador Académico    | Administra toda la información del sistema |

---

### 2.4 Restricciones

- Backend orientado a objetos (requisito académico)
- Arquitectura cliente-servidor
- Base de datos relacional
- Aplicación web
- Posible uso de Docker 

---

### 2.5 Suposiciones y Dependencias

- Los usuarios disponen de acceso a internet
- La universidad define previamente carreras y planes de estudio
- Los datos ingresados por administradores son correctos

---

## 3. Requisitos Específicos

---

## 3.1 Requisitos Funcionales

### 3.1.1 Gestión de Estudiantes

- **RF-01**: El sistema deberá permitir registrar estudiantes con datos personales y de contacto.
- **RF-02**: El sistema deberá permitir consultar la información de un estudiante.
- **RF-03**: El sistema deberá permitir actualizar los datos de un estudiante.
- **RF-04**: El sistema deberá permitir visualizar el historial académico de un estudiante.

---

### 3.1.2 Gestión de Docentes

- **RF-05**: El sistema deberá permitir registrar docentes.
- **RF-06**: El sistema deberá permitir consultar docentes registrados.
- **RF-07**: El sistema deberá asignar docentes a materias.
- **RF-08**: El sistema deberá permitir definir el rol del docente (Titular, JTP, Ayudante).

---

### 3.1.3 Gestión de Carreras y Planes de Estudio

- **RF-09**: El sistema deberá permitir registrar carreras.
- **RF-10**: El sistema deberá permitir definir planes de estudio.
- **RF-11**: El sistema deberá asociar materias a un plan de estudio.

---

### 3.1.4 Gestión de Materias

- **RF-12**: El sistema deberá permitir crear, modificar y eliminar materias.
- **RF-13**: El sistema deberá definir cupos para cada materia.

---

### 3.1.5 Gestión de Correlatividades

- **RF-14**: El sistema deberá permitir definir correlatividades entre materias.
- **RF-15**: El sistema deberá validar automáticamente las correlatividades al momento de la inscripción.

---

### 3.1.6 Inscripciones y Cursadas

- **RF-16**: El sistema deberá permitir a los estudiantes inscribirse a materias.
- **RF-17**: El sistema deberá validar disponibilidad de cupos.
- **RF-18**: El sistema deberá registrar las cursadas activas.
- **RF-19**: El sistema deberá permitir cancelar una inscripción.

---

### 3.1.7 Gestión de Notas

- **RF-20**: El sistema deberá permitir a los docentes registrar notas finales.
- **RF-21**: El sistema deberá almacenar el historial de notas.
- **RF-22**: El sistema deberá permitir consultar el rendimiento académico.

---

### 3.1.8 Consultas

- **RF-23**: El sistema deberá mostrar materias disponibles.
- **RF-24**: El sistema deberá mostrar materias cursadas por estudiante.
- **RF-25**: El sistema deberá listar estudiantes por materia.

---

### 3.1.9 Gestión de Usuarios

- **RF-26**: El sistema deberá permitir registro de usuarios.
- **RF-27**: El sistema deberá permitir autenticación (login).
- **RF-28**: El sistema deberá gestionar roles (estudiante, profesor, administrador).
- **RF-29**: El sistema deberá restringir funcionalidades según el rol.

---

## 3.2 Requisitos No Funcionales

### 3.2.1 Rendimiento

- **RNF-01**: El sistema deberá responder a las solicitudes en menos de 2 segundos bajo carga normal.

---

### 3.2.2 Seguridad

- **RNF-02**: El sistema deberá autenticar usuarios mediante credenciales seguras.
- **RNF-03**: El sistema deberá garantizar control de acceso basado en roles.

---

### 3.2.3 Usabilidad

- **RNF-04**: El sistema deberá presentar una interfaz clara e intuitiva.
- **RNF-05**: El sistema deberá ser accesible desde navegadores modernos.

---

### 3.2.4 Mantenibilidad

- **RNF-06**: El sistema deberá estar estructurado en capas (Controller, Service, Repository).
- **RNF-07**: El código deberá seguir principios de programación orientada a objetos.

---

### 3.2.5 Portabilidad

- **RNF-08**: El sistema deberá poder ejecutarse en entornos contenerizados (Docker).

---

## 4. Requisitos Futuros (Opcionales)

- **RF-F01**: El sistema podrá analizar el progreso académico de los estudiantes.
- **RF-F02**: El sistema podrá detectar riesgo de abandono.
- **RF-F03**: El sistema podrá identificar estudiantes destacados.

---