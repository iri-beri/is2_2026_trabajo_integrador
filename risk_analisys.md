# 📊 Análisis de Riesgos

## Criterios de Evaluación

- **Probabilidad**: Baja / Media / Alta  
- **Impacto**: Bajo / Medio / Alto  
- **Nivel de Riesgo**:
  - 🔴 Alto = Alta probabilidad + Alto impacto  
  - 🟠 Medio = combinaciones intermedias  
  - 🟢 Bajo = Baja probabilidad + Bajo impacto  

---

## 📋 Tabla de Riesgos

| ID  | Tipo              | Riesgo                                                                 | Probabilidad | Impacto | Nivel |
|-----|------------------|------------------------------------------------------------------------|-------------|--------|-------|
| T1  | Técnico          | Complejidad en validación de correlatividades                         | Alta        | Alto   | 🔴 Alto |
| T2  | Técnico          | Limitaciones de concurrencia en SQLite                                | Media       | Medio  | 🟠 Medio |
| T3  | Técnico          | Falta de escalabilidad de la arquitectura                             | Media       | Medio  | 🟠 Medio |
| T4  | Técnico          | Alto acoplamiento entre capas (mala arquitectura)                     | Alta        | Alto   | 🔴 Alto |
| T5  | Técnico          | Limitaciones de Mustache para UI dinámica                             | Media       | Medio  | 🟠 Medio |
| T6  | Técnico          | Errores en integridad referencial                                     | Media       | Alto   | 🟠 Medio |
| O1  | Organizacional   | Falta de experiencia homogénea en tecnologías                         | Alta        | Medio  | 🟠 Medio |
| O2  | Organizacional   | Problemas de comunicación en el equipo                                | Media       | Alto   | 🟠 Medio |
| O3  | Organizacional   | Uso ineficiente de Kanban                                             | Media       | Medio  | 🟠 Medio |
| O4  | Organizacional   | Falta de definición temprana de arquitectura                          | Alta        | Alto   | 🔴 Alto |
| P1  | Planificación    | Subestimación del esfuerzo                                            | Alta        | Alto   | 🔴 Alto |
| P2  | Planificación    | Cambios de alcance constantes                                         | Alta        | Alto   | 🔴 Alto |
| P3  | Planificación    | Mala priorización de funcionalidades                                  | Media       | Alto   | 🟠 Medio |
| P4  | Planificación    | Integración tardía de módulos                                         | Alta        | Alto   | 🔴 Alto |
| P5  | Planificación    | Falta de testing sistemático                                          | Alta        | Alto   | 🔴 Alto |
| H1  | Humano           | Sobrecarga de algunos integrantes                                     | Media       | Medio  | 🟠 Medio |
| H2  | Humano           | Falta de motivación                                                   | Media       | Medio  | 🟠 Medio |
| H3  | Humano           | Curva de aprendizaje tecnológica                                      | Alta        | Medio  | 🟠 Medio |
| H4  | Humano           | Conflictos en decisiones técnicas                                     | Media       | Medio  | 🟠 Medio |

---

## 🎯 Riesgos Críticos Prioritarios

- T1 – Validación de correlatividades  
- T4 – Problemas de arquitectura  
- O4 – Falta de definición arquitectónica  
- P1 – Subestimación del esfuerzo  
- P2 – Cambios de alcance  
- P4 – Integración tardía  
- P5 – Falta de testing  

---

## 🧠 Observación General

El mayor riesgo del sistema se concentra en:

- **Lógica de negocio compleja (correlatividades e inscripciones)**
- **Diseño arquitectónico inicial**
- **Gestión del tiempo en un equipo pequeño**
