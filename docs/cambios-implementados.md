# ERP Iglesias — Mejoras en la Arquitectura

## Descripción General

Este documento describe las mejoras arquitectónicas implementadas en el sistema **ERP Iglesias**.
El objetivo de estos cambios fue mejorar la **mantenibilidad**, **organización del código** y **escalabilidad** mediante la aplicación de **principios SOLID** y **patrones de diseño**.

Todos los cambios fueron implementados **sin afectar el funcionamiento del sistema**.

---

# Resumen de Cambios

| Cambio                    | Descripción                                                                 | Principio / Patrón              |
| ------------------------- | --------------------------------------------------------------------------- | ------------------------------- |
| Service Layer para Cursos | Se agregó una capa de servicios para manejar la lógica de negocio de cursos | Single Responsibility Principle |
| Refactor del Controlador  | Los controladores ahora dependen de servicios en lugar de repositorios      | Dependency Inversion Principle  |
| ChurchUtils               | Centralización de lógica repetida                                           | DRY                             |
| PersonValidator           | Separación de la lógica de validación de los controladores                  | Single Responsibility Principle |
| PaymentFactory            | Centralización de la creación de objetos Payment                            | Factory Pattern                 |

---

# 1. Implementación de Service Layer para Courses

## Principio aplicado

**Single Responsibility Principle (SRP)**

## Problema

El controlador `CourseController` contenía:

* lógica de negocio
* acceso directo al repositorio

Esto generaba **alto acoplamiento entre la capa de presentación y la capa de persistencia**, lo que dificulta el mantenimiento y las pruebas.

## Solución

Se implementó una **capa de servicios** para manejar la lógica de negocio relacionada con los cursos.

### Archivo creado

```
CourseService.java
```

## Beneficios

* mejor separación de responsabilidades
* mayor organización del código
* facilita las pruebas unitarias
* controladores más simples

---

# 2. Refactor del Controlador para usar Service Layer

## Principio aplicado

**Dependency Inversion Principle (DIP)**

## Problema

Los controladores dependían directamente de los repositorios.

Esto rompe la arquitectura por capas y aumenta el acoplamiento.

## Solución

El `CourseController` fue modificado para utilizar `CourseService` en lugar de acceder directamente al repositorio.

### Antes

```
CourseController → CourseRepository
```

### Después

```
CourseController → CourseService → CourseRepository
```

## Beneficios

* menor acoplamiento
* arquitectura más limpia
* mayor facilidad para modificar la lógica de negocio

---

# 3. Centralización de la lógica requireChurch()

## Principio aplicado

**DRY — Don't Repeat Yourself**

## Problema

El método `requireChurch()` estaba duplicado en múltiples controladores.

Esto genera:

* repetición de código
* mayor costo de mantenimiento
* riesgo de inconsistencias

## Solución

Se creó una clase utilitaria llamada **ChurchUtils** para centralizar esta lógica.

### Archivo creado

```
ChurchUtils.java
```

## Beneficios

* eliminación de código duplicado
* mayor reutilización
* controladores más simples

---

# 4. Implementación de PersonValidator

## Principio aplicado

**Single Responsibility Principle (SRP)**

## Problema

Las validaciones de datos estaban implementadas dentro de los controladores.

Los controladores deberían encargarse únicamente de manejar las solicitudes HTTP.

## Solución

Se creó una clase dedicada para las validaciones:

```
PersonValidator.java
```

Esta clase es responsable de validar:

* datos de personas
* campos obligatorios
* consistencia de la información

## Beneficios

* validaciones centralizadas
* controladores más limpios
* reutilización de reglas de validación
* mayor mantenibilidad

---

# 5. Implementación de PaymentFactory

## Patrón aplicado

**Factory Pattern**

## Problema

Los objetos `Payment` se creaban directamente en diferentes partes del sistema.

Esto generaba:

* duplicación de lógica de creación
* estados iniciales inconsistentes
* dificultad de mantenimiento

## Solución

Se implementó una **Factory** para centralizar la creación de objetos `Payment`.

### Archivo creado

```
PaymentFactory.java
```

### Ejemplo conceptual

```
Payment payment = PaymentFactory.createPayment(...);
```

## Beneficios

* centralización de la creación de objetos
* inicialización consistente de pagos
* mayor facilidad para extender el sistema con nuevos tipos de pago
* mejor mantenibilidad

---

# Impacto Arquitectónico

Estos cambios mejoran la arquitectura del sistema mediante:

* mejor **separación de capas**
* mayor **reutilización de código**
* menor **acoplamiento**
* mayor **facilidad para realizar pruebas**
* mejor **mantenibilidad a largo plazo**

---

# Verificación Funcional

Después de implementar los cambios:

* se reconstruyó la aplicación
* se realizaron pruebas funcionales
* el sistema continuó funcionando correctamente

Esto confirma que las mejoras arquitectónicas se implementaron **sin afectar el comportamiento existente del sistema**.

---

# Posibles Mejoras Futuras

Algunas mejoras que podrían implementarse en el futuro incluyen:

* incorporación de una **capa de DTO**
* manejo global de excepciones (**Global Exception Handler**)
* implementación de **pruebas de integración**
* adopción de una estructura basada en **Domain Driven Design**
* documentación de la API mediante **Swagger / OpenAPI**

---

# Autor

Miguel Angel Rivera Lozano