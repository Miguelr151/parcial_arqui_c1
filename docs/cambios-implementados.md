# Cambios Implementados en la Arquitectura

Este documento describe los cambios realizados en el sistema ERP Iglesias con el objetivo de mejorar la mantenibilidad, escalabilidad y calidad del código mediante la aplicación de principios SOLID y patrones de diseño.

---

# Cambio 1 — Uso de DTO para respuestas de API

## Principio aplicado
SRP — Single Responsibility Principle

## Problema identificado

Las entidades del dominio estaban siendo utilizadas directamente en las respuestas de los controladores, lo que generaba acoplamiento entre la capa de persistencia y la capa de presentación.

## Solución

Se implementó el uso de **DTOs (Data Transfer Objects)** para desacoplar la representación de datos utilizada en la API.

## Archivo donde se aplica
 
 PersonResponse.java


## Beneficios

- desacoplamiento entre capas
- mayor control sobre los datos expuestos
- mejor mantenibilidad

---

# Cambio 2 — Uso de Repository Pattern

## Patrón aplicado

Repository Pattern

## Problema identificado

El acceso a datos debe centralizarse en repositorios para separar la lógica de persistencia de la lógica de negocio.

## Solución

Se utilizaron repositorios de Spring Data JPA para manejar el acceso a la base de datos.

## Archivos donde se aplica
 PersonRepository.java
 CourseRepository.java
 EnrollmentRepository.java
 OfferingRepository.java
 PaymentRepository.java


## Beneficios

- separación de responsabilidades
- acceso a datos más limpio
- código más mantenible

---

# Cambio 3 — Implementación de seguridad con JWT

## Principio aplicado

DIP — Dependency Inversion Principle

## Problema identificado

Los endpoints del sistema requerían protección para evitar accesos no autorizados.

## Solución

Se implementó autenticación basada en **JWT (JSON Web Token)** mediante Spring Security.

## Archivos donde se aplica
 SecurityConfig.java
 JwtAuthFilter.java
 JwtService.java
 AuthUserDetailsService.java


## Beneficios

- seguridad en los endpoints
- autenticación sin estado
- escalabilidad del sistema

---

# Cambio 4 — Modelo Multi-Tenant basado en Iglesia

## Patrón aplicado

Multi-Tenant Architecture

## Problema identificado

El sistema debía soportar múltiples iglesias utilizando la misma aplicación.

## Solución

Se implementó un modelo donde cada registro pertenece a una iglesia específica.

Esto se implementa utilizando el método:

```java
Church church = requireChurch();

Beneficios

aislamiento de datos

escalabilidad organizacional

seguridad por organización

Cambio 5 — Simulación de pasarela de pagos
Patrón aplicado

Strategy Pattern (conceptual)

Problema identificado

El sistema requería procesar pagos sin depender de proveedores externos.

Solución

Se implementó un sistema de estados para simular pagos.

Estados disponibles:

INICIADO

CONFIRMADO

FALLIDO