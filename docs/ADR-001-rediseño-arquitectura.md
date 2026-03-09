# ADR-001 — Diagnóstico y Mejora Arquitectónica del Sistema ERP Iglesias

## Estado
Propuesto / Parcialmente Implementado

## Fecha
09 de marzo de 2026

## Autor
Miguel Angel Rivera Lozano

---

# Contexto del Sistema

El sistema **ERP Iglesias** es una aplicación diseñada para gestionar procesos administrativos dentro de iglesias. El sistema permite administrar diferentes módulos relacionados con la gestión organizacional, incluyendo:

- personas
- cursos
- inscripciones
- ofrendas
- pagos

La aplicación implementa una arquitectura basada en **Angular en el frontend** y **Spring Boot en el backend**, comunicándose mediante **APIs REST**.

Durante el análisis arquitectónico del sistema se identificaron oportunidades de mejora relacionadas con:

- separación de responsabilidades
- reducción del acoplamiento
- aplicación de patrones de diseño
- aplicación de principios SOLID
- mejora de la mantenibilidad del código

Este documento registra el diagnóstico arquitectónico del sistema, así como las decisiones arquitectónicas propuestas para mejorar la calidad del software.

---

# Stack Tecnológico

## Backend

- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- JPA / Hibernate
- Maven

## Frontend

- Angular
- TypeScript
- HTML
- CSS

## Base de Datos

- PostgreSQL

## Infraestructura

- Docker
- Docker Compose
- Nginx

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

````

Este enfoque permite una separación clara entre:

- capa de presentación
- lógica de negocio
- acceso a datos

---

# Modelo Multi-Tenant Basado en Iglesia

Durante el análisis del sistema se identificó que el ERP utiliza un modelo **multi-tenant basado en iglesia**.

Cada registro dentro del sistema pertenece a una iglesia específica.

Esto se evidencia en métodos del backend como:

```java
Church church = requireChurch();
````

Este diseño permite:

* aislamiento de datos entre organizaciones
* control de acceso basado en iglesia
* escalabilidad del sistema para múltiples iglesias

Esto implica que el flujo correcto del sistema requiere:

1. Crear una iglesia
2. Crear un usuario
3. Iniciar sesión
4. Registrar los demás datos del sistema

---

# Modelo Entidad Relación (MER)

## Entidades Principales

### Church

* id
* name

### Person

* id
* name
* lastName
* document
* phone
* email
* church_id

### Course

* id
* name
* description
* church_id

### Enrollment

* id
* person_id
* course_id

### Offering

* id
* amount
* date
* church_id

### Payment

* id
* type
* amount
* status
* attempts

---

# Relaciones

```
CHURCH (1) -------- (N) PERSON

CHURCH (1) -------- (N) COURSE

PERSON (1) -------- (N) ENROLLMENT

COURSE (1) -------- (N) ENROLLMENT

CHURCH (1) -------- (N) OFFERING

PAYMENT asociado a OFRENDA o INSCRIPCIÓN
```

---

# Diagnóstico Arquitectónico

Durante el análisis del sistema se identificaron varios aspectos relevantes de su arquitectura.

## Arquitectura Monolítica

El sistema sigue una arquitectura monolítica basada en capas.

Este enfoque facilita el desarrollo inicial, pero puede limitar la escalabilidad en sistemas de gran tamaño.

---

## Uso de JWT para Seguridad

El sistema utiliza autenticación basada en **JWT (JSON Web Tokens)** mediante Spring Security.

Esto permite:

* autenticación sin estado
* protección de endpoints
* escalabilidad en sistemas distribuidos

---

## Modelo Multi-Tenant

El sistema implementa un modelo multi-tenant basado en iglesia, donde todos los registros se filtran por la iglesia asociada al usuario autenticado.

---

## Simulación de Pasarela de Pagos

El sistema incluye un módulo de pagos que simula el comportamiento de una pasarela de pagos.

Estados disponibles del pago:

* INICIADO
* CONFIRMADO
* FALLIDO

Esto permite realizar pruebas del sistema sin depender de proveedores externos de pago.

---

# Decisiones Arquitectónicas

## ADR-01 — Aplicación del principio SRP

Separar responsabilidades dentro de las clases para mejorar la mantenibilidad del sistema.

---

## ADR-02 — Implementación de DTO

Evitar exponer directamente las entidades del dominio en las APIs para mejorar el desacoplamiento entre capas.

---

## ADR-03 — Implementación de Mapper

Separar la conversión entre entidades del dominio y objetos de transferencia de datos.

---

## ADR-04 — Uso del Repository Pattern

Centralizar el acceso a datos mediante repositorios.

---

## ADR-05 — Aplicación del principio DIP

Las clases deben depender de interfaces en lugar de implementaciones concretas.

---

## ADR-06 — Implementación de Builder Pattern

Facilitar la construcción de objetos complejos.

---

## ADR-07 — Uso de Strategy Pattern

Permitir manejar diferentes comportamientos en el procesamiento de pagos.

---

## ADR-08 — Uso de Adapter Pattern

Permitir integración entre diferentes capas del sistema.

---

## ADR-09 — Uso de Factory Pattern

Centralizar la creación de objetos.

---

## ADR-10 — Mejora de separación de capas

Fortalecer la separación entre:

* Controllers
* Services
* Repositories

---

# Cambios Implementados

Durante el desarrollo se realizaron mejoras orientadas a:

* mejorar la organización del código
* mantener una estructura clara entre capas
* fortalecer la seguridad del sistema mediante JWT
* mejorar la mantenibilidad del proyecto

Estos cambios permiten una arquitectura más clara y preparada para futuras mejoras.

---

# Pruebas Funcionales

Se realizaron pruebas funcionales para verificar el correcto funcionamiento del sistema.

## Flujo de pruebas

### 1. Creación de Iglesia

Se registró correctamente una iglesia dentro del sistema.

### 2. Creación de Usuario

Se registró un usuario asociado a la iglesia.

### 3. Inicio de sesión

Se verificó el correcto funcionamiento de la autenticación mediante JWT.

### 4. Registro de Personas

Se registraron personas asociadas a la iglesia.

### 5. Creación de Cursos

Se crearon cursos asociados a la iglesia.

### 6. Inscripción a Cursos

Se realizaron inscripciones de personas a cursos.

### 7. Registro de Ofrendas

Se registraron ofrendas correctamente.

### 8. Simulación de Pagos

El sistema permite confirmar o fallar pagos dentro del módulo de pagos.

Esto demuestra que el flujo completo del sistema funciona correctamente.

---

# Consecuencias Arquitectónicas

## Impacto positivo

* mejor organización del código
* mejor separación de responsabilidades
* mayor mantenibilidad
* arquitectura más clara

## Trade-offs

* incremento en el número de clases
* mayor complejidad inicial del proyecto

Sin embargo, los beneficios arquitectónicos superan ampliamente los costos.


