# ADR-001 — Diagnóstico y Mejora Arquitectónica del Sistema ERP Iglesias

## Estado

Propuesto / Parcialmente Implementado

## Fecha

09 de marzo de 2026

## Autor

Miguel Angel Rivera Lozano

---

# Contexto del Sistema

El sistema **ERP Iglesias** es una aplicación orientada a la gestión administrativa de organizaciones religiosas. Permite administrar información relacionada con personas, cursos, inscripciones, ofrendas y pagos.

El objetivo del sistema es centralizar la gestión administrativa de una iglesia mediante una plataforma web que permita registrar y consultar información de manera estructurada.

La aplicación está compuesta por dos componentes principales:

* **Frontend:** desarrollado en Angular
* **Backend:** desarrollado en Spring Boot

Ambos componentes se comunican mediante una **API REST**.

Durante el análisis arquitectónico del sistema se identificaron oportunidades de mejora relacionadas con:

* separación de responsabilidades
* reducción del acoplamiento entre capas
* aplicación de patrones de diseño
* aplicación de principios SOLID
* mejora de la mantenibilidad del código

Este documento registra el diagnóstico arquitectónico del sistema, así como las decisiones arquitectónicas propuestas para mejorar la calidad del software.

---

# Stack Tecnológico

## Backend

* Java 17
* Spring Boot
* Spring Security
* JWT Authentication
* JPA / Hibernate
* Maven

## Frontend

* Angular
* TypeScript
* HTML
* CSS

## Base de Datos

* PostgreSQL

## Infraestructura

* Docker
* Docker Compose
* Nginx

---

# Arquitectura del Sistema

El sistema sigue una arquitectura **monolítica basada en capas**.

```
Frontend (Angular)
│
│ REST API
▼
Backend (Spring Boot)
├── Controllers
├── Services
├── Repositories
└── Entities
│
▼
Base de Datos (PostgreSQL)
```

Este enfoque permite una separación clara entre:

* capa de presentación (Controllers)
* lógica de negocio (Services)
* acceso a datos (Repositories)

Esta estructura facilita el mantenimiento del sistema y la evolución futura de la arquitectura.

---

# Modelo Multi-Tenant Basado en Iglesia

Durante el análisis del sistema se identificó que el ERP utiliza un modelo **multi-tenant basado en iglesia**.

Cada registro dentro del sistema pertenece a una iglesia específica, lo cual permite que múltiples iglesias puedan utilizar el mismo sistema sin mezclar sus datos.

Esto se evidencia en métodos del backend como:

```java
Church church = requireChurch();
```

Este diseño permite:

* aislamiento de datos entre organizaciones
* control de acceso basado en iglesia
* escalabilidad del sistema para múltiples iglesias

El flujo correcto del sistema es el siguiente:

1. Registrar una iglesia
2. Crear un usuario administrador
3. Iniciar sesión
4. Registrar personas, cursos, ofrendas o inscripciones

---

# Modelo Entidad Relación (MER)

## Entidades Principales

### Church

| Campo   | Tipo   | Descripción                       |
| ------- | ------ | --------------------------------- |
| id      | Long   | Identificador único de la iglesia |
| name    | String | Nombre de la iglesia              |
| address | String | Dirección de la iglesia           |

---

### Person

| Campo     | Tipo   | Descripción                       |
| --------- | ------ | --------------------------------- |
| id        | Long   | Identificador único de la persona |
| firstName | String | Nombre                            |
| lastName  | String | Apellido                          |
| document  | String | Documento                         |
| phone     | String | Teléfono                          |
| email     | String | Correo electrónico                |
| church_id | Long   | Iglesia asociada                  |

---

### Course

| Campo       | Tipo    | Descripción             |
| ----------- | ------- | ----------------------- |
| id          | Long    | Identificador del curso |
| name        | String  | Nombre                  |
| description | String  | Descripción             |
| price       | Decimal | Precio del curso        |
| church_id   | Long    | Iglesia asociada        |

---

### Enrollment

| Campo      | Tipo   | Descripción                  |
| ---------- | ------ | ---------------------------- |
| id         | Long   | Identificador de inscripción |
| person_id  | Long   | Persona inscrita             |
| course_id  | Long   | Curso                        |
| status     | String | Estado de inscripción        |
| payment_id | Long   | Pago asociado                |

---

### Offering

| Campo      | Tipo    | Descripción                    |
| ---------- | ------- | ------------------------------ |
| id         | Long    | Identificador de la ofrenda    |
| person_id  | Long    | Persona que realiza la ofrenda |
| amount     | Decimal | Valor                          |
| concept    | String  | Concepto                       |
| status     | String  | Estado                         |
| payment_id | Long    | Pago asociado                  |

---

### Payment

| Campo        | Tipo    | Descripción                         |
| ------------ | ------- | ----------------------------------- |
| id           | Long    | Identificador del pago              |
| type         | String  | Tipo de pago                        |
| amount       | Decimal | Valor                               |
| status       | String  | Estado                              |
| attempts     | Integer | Intentos de pago                    |
| reference_id | Long    | Referencia de inscripción u ofrenda |

---

# Relaciones

```
CHURCH (1) -------- (N) PERSON

CHURCH (1) -------- (N) COURSE

PERSON (1) -------- (N) ENROLLMENT

COURSE (1) -------- (N) ENROLLMENT

PERSON (1) -------- (N) OFFERING

ENROLLMENT (1) ---- (1) PAYMENT

OFFERING (1) ------ (1) PAYMENT
```

---

# Diagnóstico Arquitectónico

Durante el análisis del sistema se identificaron los siguientes aspectos:

## Arquitectura Monolítica

El sistema sigue una arquitectura monolítica basada en capas, lo cual facilita el desarrollo inicial pero puede limitar la escalabilidad a gran escala.

## Seguridad basada en JWT

El sistema implementa autenticación mediante **JWT**, lo cual permite:

* autenticación sin estado
* protección de endpoints
* escalabilidad en arquitecturas distribuidas

## Uso de Repositories

El acceso a datos se centraliza mediante repositorios basados en **Spring Data JPA**, lo cual facilita la persistencia de datos.

## Duplicación de lógica en controladores

Se identificó duplicación de lógica en algunos controladores, particularmente en la obtención de la iglesia asociada al sistema.

Esto representa una oportunidad de mejora mediante centralización de lógica reutilizable.

---

# Decisiones Arquitectónicas

## ADR-01 — Aplicación del principio SRP

Separar responsabilidades dentro de las clases para mejorar la mantenibilidad del sistema.

## ADR-02 — Uso de DTO en las APIs

Utilizar objetos de transferencia de datos para evitar exponer directamente las entidades del dominio.

## ADR-03 — Separación entre Controller y Service

Mover lógica de negocio a la capa de servicios para reducir responsabilidades en los controladores.

## ADR-04 — Uso del Repository Pattern

Centralizar el acceso a datos mediante repositorios.

## ADR-05 — Centralización de lógica reutilizable (DRY)

Se centralizó la lógica de obtención de la iglesia mediante la clase **ChurchUtils**.

## ADR-06 — Implementación de Factory Pattern

Se propone utilizar una fábrica para la creación de pagos.

## ADR-07 — Uso de Strategy Pattern para pagos

Permitir manejar diferentes tipos de pagos mediante estrategias.

## ADR-08 — Validación mediante clases especializadas

Separar las validaciones de datos del controlador.

## ADR-09 — Separación de responsabilidades en controladores

Reducir la cantidad de lógica en los controladores.

## ADR-10 — Mejora de organización del código

Reorganizar utilidades comunes para mejorar la estructura del proyecto.

---

# Cambios Implementados

Durante el desarrollo del parcial se implementaron los siguientes cambios:

### Cambio 1 — Implementación de Service Layer

Se creó la clase **CourseService** para separar lógica de negocio del controlador.

### Cambio 2 — Refactorización de CourseController

El controlador ahora utiliza el servicio en lugar de acceder directamente al repositorio.

### Cambio 3 — Centralización de lógica requireChurch()

Se creó la clase **ChurchUtils** para centralizar la lógica de obtención de la iglesia.

### Cambio 4 — Refactorización de Controllers

Se actualizaron los controladores:

* CourseController
* PersonController
* OfferingController
* EnrollmentController

para utilizar **ChurchUtils**.

### Cambio 5 — Mejora en la organización de capas

Se fortaleció la separación entre:

* Controllers
* Services
* Repositories

---

# Pruebas Funcionales

Se realizaron pruebas funcionales para validar que los cambios no afectaron el funcionamiento del sistema.

## Flujo de pruebas

1. Registro de iglesia
2. Creación de usuario administrador
3. Inicio de sesión mediante JWT
4. Registro de personas
5. Creación de cursos
6. Inscripción de personas a cursos
7. Registro de ofrendas
8. Simulación de pagos

Todas las funcionalidades operaron correctamente después de los cambios realizados.

---

# Consecuencias Arquitectónicas

## Impacto positivo

* mejor organización del código
* menor duplicación de lógica
* mejor separación de responsabilidades
* arquitectura más mantenible
* mayor claridad en la estructura del sistema

## Trade-offs

* incremento en la cantidad de clases
* mayor complejidad inicial en la estructura del proyecto

Sin embargo, estos cambios permiten una arquitectura más escalable y preparada para futuras mejoras.
