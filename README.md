# ERP Iglesias

Sistema ERP diseñado para la **gestión administrativa de iglesias**, permitiendo administrar personas, cursos, inscripciones, ofrendas y pagos dentro de una organización religiosa.

Este proyecto fue analizado y mejorado arquitectónicamente como parte del parcial de **Arquitectura de Software**, aplicando principios **SOLID** y patrones de diseño.

---

# Descripción del Sistema

El sistema permite gestionar diferentes procesos administrativos de una iglesia mediante una aplicación web.

Funcionalidades principales:

* gestión de personas
* gestión de cursos
* inscripción a cursos
* registro de ofrendas
* simulación de pagos
* autenticación con JWT

El sistema está diseñado para soportar múltiples iglesias utilizando un modelo **multi-tenant basado en iglesia**.

---

# Arquitectura del Sistema

El sistema sigue una arquitectura **monolítica basada en capas**.

```id="arch001"
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

Cada capa cumple una responsabilidad específica:

| Capa         | Responsabilidad          |
| ------------ | ------------------------ |
| Controllers  | Manejo de endpoints REST |
| Services     | Lógica de negocio        |
| Repositories | Acceso a datos           |
| Entities     | Modelo de dominio        |

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

# Modelo Multi-Tenant

El sistema implementa un modelo **multi-tenant basado en iglesia**, donde cada registro pertenece a una iglesia específica.

Esto permite que múltiples organizaciones utilicen el sistema sin mezclar sus datos.

Flujo típico del sistema:

1. Registrar iglesia
2. Crear usuario
3. Iniciar sesión
4. Registrar personas
5. Crear cursos
6. Inscribir personas
7. Registrar ofrendas
8. Procesar pagos

---

# Modelo Entidad Relación

## Entidades principales

### Church

| Campo   | Tipo   |
| ------- | ------ |
| id      | Long   |
| name    | String |
| address | String |

---

### Person

| Campo     | Tipo   |
| --------- | ------ |
| id        | Long   |
| firstName | String |
| lastName  | String |
| document  | String |
| phone     | String |
| email     | String |
| church_id | Long   |

---

### Course

| Campo       | Tipo    |
| ----------- | ------- |
| id          | Long    |
| name        | String  |
| description | String  |
| price       | Decimal |
| church_id   | Long    |

---

### Enrollment

| Campo      | Tipo   |
| ---------- | ------ |
| id         | Long   |
| person_id  | Long   |
| course_id  | Long   |
| status     | String |
| payment_id | Long   |

---

### Offering

| Campo      | Tipo    |
| ---------- | ------- |
| id         | Long    |
| person_id  | Long    |
| amount     | Decimal |
| concept    | String  |
| status     | String  |
| payment_id | Long    |

---

### Payment

| Campo        | Tipo    |
| ------------ | ------- |
| id           | Long    |
| type         | String  |
| amount       | Decimal |
| status       | String  |
| attempts     | Integer |
| reference_id | Long    |

---

# Relaciones

```id="mer001"
CHURCH (1) ---- (N) PERSON

CHURCH (1) ---- (N) COURSE

PERSON (1) ---- (N) ENROLLMENT

COURSE (1) ---- (N) ENROLLMENT

PERSON (1) ---- (N) OFFERING

ENROLLMENT (1) ---- (1) PAYMENT

OFFERING (1) ---- (1) PAYMENT
```

---

# Cambios Arquitectónicos Implementados

Durante el análisis del sistema se aplicaron mejoras arquitectónicas basadas en **principios SOLID y patrones de diseño**.

## 1. Implementación de Service Layer

Se creó `CourseService` para separar la lógica de negocio del controlador.

Principio aplicado:

```
SRP — Single Responsibility Principle
```

---

## 2. Refactorización de Controllers

Los controladores fueron simplificados para delegar responsabilidades.

Controladores modificados:

* CourseController
* PersonController
* OfferingController
* EnrollmentController

---

## 3. Centralización de lógica con ChurchUtils

Se creó la clase:

```
ChurchUtils
```

para evitar duplicación del método `requireChurch()`.

Principio aplicado:

```
DRY — Don't Repeat Yourself
```

---

## 4. Uso de DTO

Se utilizan **DTO (Data Transfer Objects)** para separar la capa de dominio de la capa de presentación.

Esto evita exponer directamente las entidades del dominio.

---

## 5. Implementación de Factory Pattern

Se implementó:

```
PaymentFactory
```

para centralizar la creación de objetos `Payment`.

Beneficios:

* evita duplicación
* centraliza la creación de pagos
* mejora mantenibilidad

---

# Pruebas Funcionales

Se realizaron pruebas para validar que los cambios no afectaran la funcionalidad del sistema.

Flujo probado:

1. creación de iglesia
2. registro de usuarios
3. autenticación JWT
4. registro de personas
5. creación de cursos
6. inscripción a cursos
7. registro de ofrendas
8. confirmación de pagos

Todas las funcionalidades operaron correctamente.

---

# Cómo ejecutar el proyecto

## 1. Clonar repositorio

```id="gitclone"
git clone https://github.com/lanvargas94/erp_iglesias.git
```

---

## 2. Levantar contenedores Docker

```id="dockrun"
docker compose up --build
```

Esto iniciará:

* backend Spring Boot
* frontend Angular
* base de datos PostgreSQL

---

## 3. Acceder a la aplicación

Frontend:

```
http://localhost
```

API backend:

```
http://localhost:8080
```

---

# Estructura del Proyecto

```id="struct001"
erp_iglesias
│
├── backend
│   ├── controllers
│   ├── services
│   ├── repositories
│   ├── entities
│   └── utils
│
├── frontend
│   └── angular app
│
├── docker-compose.yml
└── README.md
```

---

# Autor

Miguel Angel Rivera Lozano
Proyecto académico — Arquitectura de Software
