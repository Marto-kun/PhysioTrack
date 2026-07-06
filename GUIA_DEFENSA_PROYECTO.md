# 🎓 Guía de Defensa del Proyecto PhysioTrack

**Objetivo:** Preparar al equipo para defender el proyecto en clase con confianza y claridad.

---

## 📑 Tabla de Contenidos
1. [Visión General del Proyecto](#visión-general-del-proyecto)
2. [Arquitectura del Sistema](#arquitectura-del-sistema)
3. [Modelo de Datos](#modelo-de-datos)
4. [Lógica de Negocio Central](#lógica-de-negocio-central)
5. [Flujos Principales de Uso](#flujos-principales-de-uso)
6. [Componentes de Interfaz](#componentes-de-interfaz)
7. [Decisiones Técnicas Importantes](#decisiones-técnicas-importantes)
8. [Puntos Clave para la Presentación](#puntos-clave-para-la-presentación)
9. [Preguntas Frecuentes (FAQ)](#preguntas-frecuentes-faq)

---

## 🎯 Visión General del Proyecto

### ¿Qué es PhysioTrack?

**PhysioTrack** es una aplicación web de gestión integral para clínicas de fisioterapia que permite:

- **Registrar y administrar pacientes** con sus datos clínicos y lesiones
- **Asignar fisioterapeutas** a pacientes de forma bidireccional
- **Crear planes de rehabilitación personalizados** con seguimiento de progreso
- **Gestionar inventario** de insumos y equipos biomédicos
- **Registrar sesiones de tratamiento** en tiempo real
- **Hacer seguimiento del cumplimiento** de rutinas de ejercicio

### Tecnología Stack

| Componente | Tecnología |
|-----------|-----------|
| **Backend** | Spring Boot 2.x (Java 11+) |
| **Frontend** | Vaadin Flow 24 (componentes UI nativos) |
| **Base de Datos** | MongoDB Atlas (NoSQL en la nube) |
| **Patrón de Arquitectura** | MVC (Model-View-Controller) |
| **Persistencia** | Spring Data MongoDB |

### Usuarios Objetivo

- **Administrador/Gerente de Clínica:** Gestionar recursos y personal
- **Fisioterapeuta:** Registrar sesiones, crear planes, ver pacientes asignados
- **Personal de Inventario:** Controlar stock de insumos y equipos

---

## 🏗️ Arquitectura del Sistema

### Estructura de Capas

```
┌─────────────────────────────────────────────────────────┐
│                 UI - Vaadin Flow (Frontend)              │
│  (Vistas, Grids, Diálogos, Componentes Interactivos)   │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│          Controllers & Services (Negocio)               │
│  (Lógica de aplicación, orquestación de procesos)      │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│       Repositories (Acceso a Datos)                     │
│  (Consultas a MongoDB, operaciones CRUD)               │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│         MongoDB Atlas (Persistencia)                    │
│  (Base de datos NoSQL en la nube)                       │
└─────────────────────────────────────────────────────────┘
```

### Componentes Principales

#### 1. **Modelos (Entidades)**
Clases que representan conceptos del negocio:
- `Paciente`: Persona bajo tratamiento fisioterápico
- `Fisioterapeuta`: Profesional que atiende pacientes
- `SesionTratamiento`: Evento de una cita de terapia
- `PlanRehabilitacion`: Programa de ejercicios personalizado
- `EquipoBiomedico`: Máquinas de la clínica
- `Insumo`: Materiales consumibles

#### 2. **Servicios (Business Logic)**
Clases que implementan reglas de negocio:
- `PacienteService`: Gestión de pacientes y asignaciones
- `FisioterapeutaService`: Administración de terapeutas
- `SesionTratamientoService`: Registro de sesiones
- `PlanRehabilitacionService`: Creación y seguimiento de planes
- `InsumoService`: Control de inventario consumible
- `EquipoBiomedicoService`: Gestión de equipos

#### 3. **Controladores de Negocio (En Memoria)**
Gestores que implementan reglas centrales:
- `GestorPacientes`: Mantiene relaciones bidireccionales paciente-fisioterapeuta
- `ControladorRecursos`: Valida disponibilidad de insumos y equipos
- `EvaluadorRutinas`: Aplica restricciones de seguridad en planes de ejercicio

#### 4. **Vistas (UI)**
Componentes Vaadin que interactúan con el usuario:
- `PacientesView`: Gestión y registro de pacientes
- `RegistroFisioterapeutaView`: Registro de profesionales
- `SesionesView`: Historial y registro de sesiones
- `InventarioInsumosView`: Gestión de consumibles
- `InventarioEquiposView`: Gestión de máquinas
- `PlanesView`: Creación y seguimiento de planes

#### 5. **Repositorios (Data Access)**
Interfaces Spring Data que comunican con MongoDB:
- `PacienteRepository`
- `FisioterapeutaRepository`
- `SesionTratamientoRepository`
- `PlanRehabilitacionRepository`
- `InsumoRepository`
- `EquipoBiomedicoRepository`

---

## 💾 Modelo de Datos

### Entidades Principales

#### **Paciente**
```
{
  _id: ObjectId (generado por Mongo),
  cedula: String (única),
  nombre: String (nombre completo),
  edad: Integer,
  tipoLesion: String (ej. "Esguince", "Fractura"),
  nivelLesion: Integer (1-10, escala de severidad),
  prioridad: String (calculada: "Urgente", "Seguimiento", "Preventivo"),
  fisioterapeutaAsignado: Fisioterapeuta (referencia embebida)
}
```

**Lógica de Prioridad:**
- **Urgente:** Nivel ≥ 8 O edad ≥ 65
- **Seguimiento:** Nivel ≥ 5
- **Preventivo:** Nivel < 5 Y edad < 65

---

#### **Fisioterapeuta**
```
{
  _id: ObjectId,
  cedula: String (única),
  nombre: String,
  especialidad: String (ej. "Ortopedia", "Neurología"),
  pacientesAsignados: [Paciente] (array de pacientes bajo su cuidado)
}
```

**Relación Bidireccional:** Cuando un paciente se asigna a un fisioterapeuta:
1. El paciente guarda la referencia del fisioterapeuta en `fisioterapeutaAsignado`
2. El fisioterapeuta agrega el paciente a su array `pacientesAsignados`

---

#### **SesionTratamiento**
```
{
  _id: ObjectId,
  idSesion: String,
  fecha: String (ISO format),
  paciente: Paciente (embebida, datos básicos),
  fisioterapeutaResponsable: Fisioterapeuta (embebida),
  equipo: EquipoBiomedico (máquina usada),
  insumo: Insumo (material consumido),
  observaciones: String (notas del terapeuta),
  duracionMinutos: Integer
}
```

**Notas de Persistencia:**
- Los objetos se guardan de forma "plana" (sin referencias DBRef)
- Se evita serialización recursiva para prevenir StackOverflowError

---

#### **PlanRehabilitacion**
```
{
  _id: ObjectId,
  idPlan: String,
  paciente: Paciente (referencia),
  ejercicio: String (ej. "Flexiones de rodilla"),
  series: Integer (cantidad de repeticiones),
  repeticiones: Integer,
  diasPorSemana: Integer (frecuencia),
  porcentajeCumplimiento: Double (0-100),
  progresoFisico: Double (0-100)
}
```

---

#### **EquipoBiomedico**
```
{
  _id: ObjectId,
  nombre: String (ej. "Ultrasonido 3"),
  fechaElaboracion: String,
  estado: String ("Disponible", "En Uso", "Mantenimiento"),
  horasDeUso: Integer (para detectar mantenimiento preventivo)
}
```

**Regla de Mantenimiento:** Cuando `horasDeUso >= 50`, el sistema alerta que necesita revisión.

---

#### **Insumo**
```
{
  _id: ObjectId,
  nombre: String (ej. "Electrodos"),
  fechaElaboracion: String,
  fechaCaducidad: String,
  stock: Integer (cantidad disponible),
  stockMinimo: Integer (límite de reorden)
}
```

**Alertas:** Si `stock <= stockMinimo`, el sistema marca como "Stock Bajo"

---

## 🧠 Lógica de Negocio Central

### 1. **Gestor de Pacientes (GestorPacientes)**

**Propósito:** Mantener sincronización bidireccional entre Pacientes y Fisioterapeutas.

**Métodos Clave:**
```java
// Agregar un paciente al sistema
agregarPaciente(Paciente paciente)

// Registrar la asignación bidireccional
registrarAsignacion(Paciente paciente, Fisioterapeuta fisioterapeuta)

// Obtener planes de un paciente
getPlanesPorPaciente(String cedulaPaciente)
```

**Caso de Uso: Asignar un Paciente a un Fisioterapeuta**

1. Se selecciona un paciente y un fisioterapeuta en la UI
2. El servicio `PacienteService.registrarPaciente()` es invocado:
   - **Valida** que paciente y fisioterapeuta no sean nulos
   - **Busca** la instancia persistente del fisioterapeuta en el repo
   - **Llama** a `gestorPacientes.agregarPaciente(paciente)`
   - **Llama** a `gestorPacientes.registrarAsignacion(paciente, fisioPersist)`
   - **Guarda** primero el paciente en MongoDB
   - **Guarda** después el fisioterapeuta (para actualizar su array)
3. Resultado: Relación bidireccional sincronizada en DB

---

### 2. **Controlador de Recursos (ControladorRecursos)**

**Propósito:** Validar y gestionar la disponibilidad de insumos y equipos.

**Métodos Clave:**
```java
// Registrar un insumo en el controlador (en memoria)
registrarInsumo(Insumo nuevo)

// Consumir cantidad de insumo con validaciones
consumirInsumo(Insumo usando, int cantidad)

// Registrar un equipo
registrarEquipo(EquipoBiomedico nuevo)

// Asignar equipo a una sesión (valida disponibilidad)
asignarEquipo(EquipoBiomedico asignado)

// Liberar equipo y actualizar horas de uso
liberarEquipo(EquipoBiomedico liberado, int horas)
```

**Reglas de Validación - Insumos:**
- Cantidad > 0
- Cantidad ≤ stock disponible
- Si stock restante ≤ stock mínimo, genera alerta

**Reglas de Validación - Equipos:**
- Solo pueden asignarse si estado = "Disponible"
- Si horas de uso ≥ 50, se envía automáticamente a mantenimiento
- No se puede liberar un equipo que no está "En Uso"

---

### 3. **Evaluador de Rutinas (EvaluadorRutinas)**

**Propósito:** Aplicar reglas de seguridad al crear planes de ejercicio.

**Métodos Clave:**
```java
// Asignar plan a paciente con validaciones por nivel de lesión
asignarPlan(String cedulaPaciente, PlanRehabilitacion nuevoPlan, int nivelLesion)

// Registrar día completado (no supera días asignados)
registrarDiaCompletado(String cedulaPaciente, String idPlan)

// Obtener porcentaje de cumplimiento semanal
seguimientoSemanal(String cedulaPaciente) → String "X% de la meta alcanzada"
```

**Regla Crítica de Seguridad:**
- Si `nivelLesion >= 7`:
  - Máximo **3 series** permitidas
  - Máximo **12 repeticiones** permitidas
  - Esto previene sobre-exigencia en lesiones severas
- Si `nivelLesion < 7`: Sin restricción de series/repeticiones

---

## 🔄 Flujos Principales de Uso

### Flujo 1: Registro de Paciente con Asignación a Fisioterapeuta

```
┌─────────────────┐
│  Usuario Abre   │
│  "Gestión de    │
│  Pacientes"     │
└────────┬────────┘
         │
         ▼
┌─────────────────────────────────┐
│ Completa formulario:            │
│ - Cédula, Nombre, Edad          │
│ - Tipo de Lesión, Nivel (1-10)  │
│ - Selecciona Fisioterapeuta     │
└────────┬────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ Sistema valida:                  │
│ - Paciente no existe (cédula)    │
│ - Fisioterapeuta existe          │
│ - Datos válidos                  │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ Calcula prioridad del paciente   │
│ basada en edad + nivel lesión    │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ GestorPacientes sincroniza:      │
│ 1. Paciente → setFisioterapeuta()│
│ 2. agregarPaciente()             │
│ 3. registrarAsignacion()         │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ Persistencia secuencial:         │
│ 1. Guarda Paciente en MongoDB    │
│ 2. Guarda Fisioterapeuta         │
│    (su array pacientesAsignados) │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ ✅ Notificación: "Paciente       │
│    registrado y asignado"        │
└──────────────────────────────────┘
```

---

### Flujo 2: Registro de Sesión de Tratamiento

```
┌─────────────────────────────┐
│  Usuario abre               │
│  "Registrar Sesiones"       │
└────────┬────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ Selecciona:                      │
│ - Paciente                       │
│ - Fisioterapeuta Responsable     │
│ - Equipo (solo "Disponible")     │
│ - Insumo                         │
│ - Duración (minutos)             │
│ - Cantidad de Insumo a Consumir  │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ SesionTratamientoService:        │
│                                  │
│ 1. Valida campos no nulos       │
│ 2. Clona objetos (plano, sin    │
│    referencias circulares)       │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ ControladorRecursos valida:      │
│                                  │
│ 1. asignarEquipo():             │
│    - Verifica estado "Disponible"│
│    - Cambia a "En Uso"          │
│                                  │
│ 2. consumirInsumo():            │
│    - Verifica stock > cantidad  │
│    - Utiliza el insumo          │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ Persistencia secuencial:         │
│                                  │
│ 1. Guarda Insumo (stock reducido)│
│ 2. Guarda EquipoBiomedico        │
│    (estado = "En Uso")           │
│ 3. Guarda SesionTratamiento      │
│    (plana, sin referencias)      │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ ✅ "Sesión registrada y recursos│
│    actualizados con éxito"       │
└──────────────────────────────────┘
```

---

### Flujo 3: Creación de Plan de Rehabilitación

```
┌─────────────────────────────┐
│  Usuario abre               │
│  "Planes de Rehabilitación" │
└────────┬────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ Selecciona Paciente en ComboBox  │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ Sistema muestra:                 │
│ - Banner de seguimiento semanal  │
│  "X% de la meta alcanzada"       │
│ - Grid con planes actuales       │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ Usuario hace clic en             │
│ "Asignar Nuevo Plan"             │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ Diálogo captura:                 │
│ - Nombre del ejercicio           │
│ - Series                         │
│ - Repeticiones                   │
│ - Días por semana                │
│ - Nivel de lesión del paciente   │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ EvaluadorRutinas valida:         │
│                                  │
│ Si nivel lesión >= 7:            │
│ - Máximo 3 series? ✓/✗          │
│ - Máximo 12 repeticiones? ✓/✗   │
│                                  │
│ Si falla restricción:            │
│ ❌ Muestra error específico      │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│ Si válido:                       │
│                                  │
│ 1. gestorPacientes.agregarPlan()│
│ 2. Persistir en MongoDB          │
│                                  │
│ ✅ "Plan asignado exitosamente" │
└──────────────────────────────────┘
```

---

## 🎨 Componentes de Interfaz

### Vistas Principales

#### **MainView (Navegación Central)**
- Drawer con botones de menú
- Header con título "PhysioTrack"
- Área de contenido dinámico que cambia según selección

**Botones del Menú:**
1. Registrar Fisioterapeuta
2. Gestión de Pacientes
3. Gestión de Inventario
4. Registrar Sesiones
5. Planes de Rehabilitación

---

#### **PacientesView**
- **Grid:** Lista de todos los pacientes
  - Columnas: Cédula, Nombre, Edad, Tipo Lesión, Nivel, Prioridad, Fisioterapeuta Asignado
  - Al seleccionar: habilita panel lateral
- **Panel Lateral de Acciones:**
  - Botón "Registrar Nuevo Paciente" → Abre diálogo
  - Botón "Editar Paciente" → Abre diálogo con datos precargados
  - Botón "Reasignar Fisioterapeuta" → Dialogo para cambiar asignación

---

#### **RegistroFisioterapeutaView**
- Formulario para crear nuevos fisioterapeutas
- Campos: Cédula, Nombre, Especialidad
- Validaciones: Cédula única, campos no nulos
- Al guardar: persiste en MongoDB

---

#### **SesionesView (con Filtrado)**
- **ComboBox de Pacientes:** Filtrar por paciente
- **Grid de Sesiones:** Muestra historial del paciente seleccionado
  - Columnas: Duración, Observaciones, Equipo, Insumo
  - **Nota:** No incluye fecha (según requerimientos)
- **Botón "Registrar Sesión":** Abre diálogo de creación

---

#### **InventarioInsumosView**
- **Grid:** Lista de todos los insumos
  - Columnas: Nombre, Stock, Stock Mínimo
- **Panel Lateral:**
  - IntegerField: Ingresa cantidad
  - Botón "Reabastecer Stock": Llama `insumo.agregarStock(cantidad)`
  - Botón "Consumir Stock": Llama `controladorRecursos.consumirInsumo()`
- **Barra Superior:**
  - Botón "Agregar Insumo" → Diálogo con formulario

---

#### **InventarioEquiposView**
- **Grid:** Lista de todos los equipos
  - Columnas: Nombre, Estado, Horas de Uso
- **Panel Lateral:**
  - IntegerField: Horas de uso para liberación
  - Botón "Enviar a Mantenimiento": Cambia estado
  - Botón "Liberar Equipo": Llama `controladorRecursos.liberarEquipo()`
- **Barra Superior:**
  - Botón "Agregar Equipo" → Diálogo con formulario

---

#### **PlanesView**
- **ComboBox:** Seleccionar paciente
- **Banner:** Muestra resultado de `EvaluadorRutinas.seguimientoSemanal(cedulaPaciente)`
  - Formato: "X% de la meta alcanzada"
- **Grid:** Planes del paciente seleccionado
  - Columnas: Ejercicio, Series, Repeticiones, Días/Semana, Cumplimiento, Progreso
- **Botones:**
  - "Asignar Nuevo Plan" → Diálogo con validaciones de EvaluadorRutinas
  - "Registrar Día Completado" → Incrementa cumplimiento

---

## 🛠️ Decisiones Técnicas Importantes

### 1. **Persistencia Plana (Sin DBRef)**

**Decisión:** Guardar documentos embebidos de forma plana, clonando objetos antes de persistir.

**Razón:**
- Previene StackOverflowError por referencias circulares
- Mejora rendimiento (menos queries anidadas)
- Simplifica la sincronización entre MongoDB y memoria

**Implementación:**
```java
// Antes de guardar en MongoDB:
Insumo sanitized = PersistenceSanitizer.sanitize(insumo);
insumoRepository.save(sanitized);
```

---

### 2. **Gestión en Memoria + Persistencia**

**Decisión:** Mantener `ControladorRecursos`, `EvaluadorRutinas` y `GestorPacientes` en memoria.

**Razón:**
- Validaciones rápidas sin consultar DB
- Sincronización bidireccional consistente
- Lógica de negocio centralizada y testeable

**Flujo:**
1. Operación en memoria (validaciones)
2. Persistencia en MongoDB (confirmación)

---

### 3. **Orden Secuencial de Guardado**

**Decisión:** Al asignar paciente a fisioterapeuta, guardar paciente primero, luego fisioterapeuta.

**Razón:**
- Genera ID único en Mongo para el paciente
- Fisioterapeuta puede referenciar ID ya existente
- Evita inconsistencias de integridad referencial

**Implementación:**
```java
pacienteRepository.save(paciente);      // Genera ID en Mongo
fisioterapeutaRepository.save(fisio);   // Actualiza array con ID generado
```

---

### 4. **Validaciones en el Controlador de Recursos**

**Decisión:** Centralizar todas las validaciones de insumos/equipos en `ControladorRecursos`.

**Razón:**
- Una sola fuente de verdad para reglas de negocio
- Facilita mantenimiento y cambios
- Reutilizable en servicios y vistas

---

## 🎤 Puntos Clave para la Presentación

### Al Presentar, Enfatizar:

#### **1. Problema Resuelto**
> "PhysioTrack automatiza la gestión de clínicas de fisioterapia, integrando pacientes, profesionales, planes de ejercicio e inventario en un único sistema coherente."

#### **2. Relaciones Bidireccionales**
> "Cuando un paciente se asigna a un fisioterapeuta, ambos se actualizan automáticamente. El paciente conoce su terapeuta asignado, y el terapeuta ve todos sus pacientes en su lista."

#### **3. Reglas de Seguridad**
> "Para lesiones severas (nivel ≥7), limitamos los ejercicios a máximo 3 series y 12 repeticiones para prevenir re-lesiones."

#### **4. Gestión de Inventario**
> "El sistema valida en tiempo real que hay suficiente insumo y equipo disponible antes de registrar una sesión. Si hay falta de stock, alerta al usuario."

#### **5. Seguimiento del Progreso**
> "Los terapeutas pueden ver en porcentaje el cumplimiento semanal de cada paciente: 'X% de la meta alcanzada'."

#### **6. Persistencia Inteligente**
> "Usamos MongoDB con documentos embebidos planos para evitar problemas de referencia circular y optimizar consultas."

---

## ❓ Preguntas Frecuentes (FAQ)

### P: ¿Por qué usamos MongoDB en lugar de SQL?
**R:** MongoDB es flexible para documentos dinámicos (embebidos), escalable en la nube (Atlas) y permite almacenar objetos complejos sin normalización excesiva. Perfecto para aplicaciones modernas que evolucionan.

---

### P: ¿Cómo se sincroniza el array de pacientes del fisioterapeuta?
**R:** A través de `GestorPacientes.registrarAsignacion()`, que:
1. Añade el paciente al array del fisioterapeuta
2. Llama a `fisioterapeutaRepository.save()` después de guardar el paciente
3. Garantiza que ambos lados de la relación se actualicen

---

### P: ¿Qué pasa si falla el consumo de un insumo?
**R:** El `ControladorRecursos` lanza una `IllegalArgumentException` con mensaje descriptivo. El servicio captura la excepción y la muestra en la UI. El insumo NO se persiste.

---

### P: ¿Cómo se calcula la prioridad de un paciente?
**R:** Mediante esta regla:
- Urgente: nivel lesión ≥ 8 O edad ≥ 65
- Seguimiento: 5 ≤ nivel < 8
- Preventivo: nivel < 5 Y edad < 65

---

### P: ¿Por qué no incluimos la fecha en la Grid de sesiones?
**R:** Requisito especificado para simplificar la visualización en pantalla. El historial se ve más limpio con solo: Duración, Observaciones, Equipo, Insumo.

---

### P: ¿Qué ocurre cuando un equipo alcanza 50 horas de uso?
**R:** El sistema automáticamente lo marca para mantenimiento. El usuario verá el estado como "Mantenimiento" y no podrá asignarlo a nuevas sesiones hasta que lo liberen.

---

### P: ¿Puede un paciente tener múltiples fisioterapeutas asignados?
**R:** En el modelo actual, cada paciente tiene un `fisioterapeutaAsignado` (singular). Reasignar cambia este único fisioterapeuta. Futuras versiones podrían permitir múltiples asignaciones con un array.

---

### P: ¿Cómo se valida que la cantidad de ejercicio es segura?
**R:** El `EvaluadorRutinas` verifica:
- Si `nivelLesion >= 7`: máximo 3 series y 12 repeticiones
- Lanza excepción si se viola

---

### P: ¿Qué es la "sanitización" de objetos?
**R:** Antes de guardar en MongoDB, clonamos el objeto vaciando colecciones internas. Esto evita serializar objetos anidados que podrían causar StackOverflowError.

---

### P: ¿Por qué Vaadin y no React/Angular?
**R:** Vaadin Flow ofrece:
- Componentes web Java-first (sin JavaScript innecesario)
- Integración directa con Spring Boot
- Full-stack Java (backend y frontend en el mismo lenguaje)
- Desarrollo más rápido para equipos con expertise en Java

---

## 📊 Tabla Resumen de Responsabilidades

| Componente | Responsabilidad |
|-----------|-----------------|
| **Paciente** | Entidad con datos clínicos y asignación fisioterapeuta |
| **Fisioterapeuta** | Profesional con lista de pacientes asignados |
| **GestorPacientes** | Mantener sincronización bidireccional |
| **ControladorRecursos** | Validar disponibilidad de insumos/equipos |
| **EvaluadorRutinas** | Aplicar reglas de seguridad en planes |
| **SesionTratamiento** | Registrar evento de terapia (integrador) |
| **PlanRehabilitacion** | Programa personalizado de ejercicios |
| **Servicio** | Coordinar modelos, validar, persistir |
| **Repositorio** | Acceso a datos en MongoDB |
| **Vista** | Presentar datos e interacción del usuario |

---

## 🎯 Sugerencias para la Defensa

### Abre Así:
> "PhysioTrack es un sistema integral de gestión clínica que conecta pacientes, fisioterapeutas, planes de tratamiento e inventario en una plataforma única. Lo diferencia que es completamente bidireccional: cualquier cambio en un paciente se refleja en su terapeuta y viceversa."

### Muestra Esto en Vivo:
1. **Registro de Paciente:** Completa un formulario → Muestra que aparece en la Grid
2. **Asignación a Fisioterapeuta:** Selecciona uno → Muestra que el array del terapeuta se actualizó
3. **Registro de Sesión:** Intenta consumir más insumo del disponible → Muestra error de validación
4. **Creación de Plan:** Intenta crear plan con demasiadas series en lesión severa → Muestra restricción

### Finaliza Con:
> "La arquitectura está diseñada para ser escalable: nuevas funcionalidades se pueden agregar sin afectar módulos existentes. La validación en memoria + persistencia en MongoDB garantiza consistencia y rendimiento."

---

## 📚 Recursos para Estudio Adicional

- Documentación de Spring Boot: https://spring.io/projects/spring-boot
- MongoDB Data Modeling: https://docs.mongodb.com/manual/core/data-models/
- Vaadin Flow Guide: https://vaadin.com/docs/latest/flow
- Patrones de Arquitectura: https://martinfowler.com/architecture/

---

**Última actualización:** 2026-07-05  
**Versión del Sistema:** 1.0  
**Equipo:** [Roberto Cordero, Rafaela Ludeña, Martín Vozmediano]


