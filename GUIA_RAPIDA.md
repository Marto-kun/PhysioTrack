# 🏥 PhysioTrack - Guía Rápida de Uso

## ¿Qué es PhysioTrack?

PhysioTrack es un sistema de gestión integral para clínicas de fisioterapia que permite:
- 📋 Registrar y gestionar fisioterapeutas
- 👥 Administrar pacientes y sus lesiones
- 📊 Calcular automáticamente prioridades de atención
- ☁️ Almacenar datos en MongoDB Atlas

---

## 🚀 Inicio Rápido

### Opción 1: Ejecutar con Script (Windows)

**Con Batch (.bat):**
```bash
ejecutar.bat
```

**Con PowerShell (.ps1):**
```powershell
# Si es la primera vez, configura la política de ejecución:
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser

# Luego ejecuta:
.\ejecutar.ps1
```

### Opción 2: Ejecutar Manualmente

```bash
# 1. Compilar el proyecto
mvn clean install

# 2. Ejecutar la aplicación
mvn spring-boot:run
```

### Opción 3: Desde JAR (después de compilar)

```bash
java -jar target/PhysioTrack-1.0-SNAPSHOT.jar
```

---

## 📍 Acceder a la Aplicación

Una vez que veas este mensaje:
```
INFO: Starting PhysioTrack on YOUR-COMPUTER with PID XXXX
INFO: Started PhysioTrack in X.XXX seconds
```

Abre tu navegador en: **http://localhost:8080**

---

## 📝 Pantallas y Funcionalidades

### 1️⃣ Pantalla: Registrar Fisioterapeuta

**Ubicación:** Menu lateral → "Registrar Fisioterapeuta"

**Campos:**
- **Cédula de Identidad**: Número único del profesional (Ej: 1234567890)
- **Nombre Completo**: Nombre del fisioterapeuta (Ej: Juan Pérez)
- **Especialidad**: Selecciona una de las opciones:
  - Fisioterapia General
  - Ortopedia
  - Neurología
  - Cardiología
  - Pediatría
  - Deportología
  - Reumatología

**Botones:**
- 💾 **Guardar Fisioterapeuta**: Registra el profesional
- 🗑️ **Limpiar**: Borra los campos del formulario

**Ejemplo:**
```
Cédula: 1010123456
Nombre: María González
Especialidad: Ortopedia
```

---

### 2️⃣ Pantalla: Gestión de Pacientes

**Ubicación:** Menu lateral → "Gestión de Pacientes"

#### 2.1 Seleccionar Fisioterapeuta

En la parte superior, hay un dropdown para seleccionar el fisioterapeuta. Esto mostrará todos los pacientes asignados a ese profesional.

#### 2.2 Tabla de Pacientes

Muestra información del paciente:
| Campo | Descripción |
|-------|-------------|
| **Cédula** | Identificación única del paciente |
| **Nombre** | Nombre completo |
| **Edad** | Años de edad |
| **Tipo de Lesión** | Tipo de problema (Ej: "Esguince de tobillo") |
| **Nivel** | Gravedad 1-10 |
| **Prioridad** | 🔴 Urgente / 🟡 Seguimiento / 🟢 Preventivo |
| **Acciones** | ✏️ Editar / 🗑️ Eliminar |

#### 2.3 Cálculo de Prioridad (Automático)

La prioridad se calcula según:

```
SI (Nivel de Lesión ≥ 8) O (Edad ≥ 65)
   ENTONCES → 🔴 URGENTE
SI NO (Nivel de Lesión ≥ 5)
   ENTONCES → 🟡 SEGUIMIENTO
SI NO
   ENTONCES → 🟢 PREVENTIVO
```

**Ejemplos:**
- Paciente 45 años, nivel 9 → 🔴 Urgente (nivel alto)
- Paciente 70 años, nivel 3 → 🔴 Urgente (adulto mayor)
- Paciente 30 años, nivel 6 → 🟡 Seguimiento
- Paciente 28 años, nivel 2 → 🟢 Preventivo

#### 2.4 Registrar Nuevo Paciente

**Paso 1:** Click en botón "➕ Registrar Nuevo Paciente"

**Paso 2:** Completa el formulario:
- **Cédula**: Identificación única
- **Nombre Completo**: Nombre del paciente
- **Edad**: Años (1-150)
- **Tipo de Lesión**: Descripción (Ej: "Lumbalgia", "Esguince", "Fractura")
- **Nivel de Lesión**: 1-10 (donde 10 es muy grave)
- **Asignar Fisioterapeuta**: Selecciona el profesional a cargo

**Paso 3:** Click en "💾 Guardar"

**Ejemplo:**
```
Cédula: 1010654321
Nombre: Carlos Rodríguez
Edad: 45
Tipo de Lesión: Esguince de tobillo
Nivel: 7
Fisioterapeuta: María González (Ortopedia)
```

#### 2.5 Editar Paciente

**Paso 1:** En la tabla, click en ✏️ (Editar)

**Paso 2:** Modifica los campos (excepto cédula)

**Paso 3:** Click en "🔄 Actualizar"

#### 2.6 Eliminar Paciente

**Paso 1:** En la tabla, click en 🗑️ (Eliminar)

**Paso 2:** El paciente se eliminará inmediatamente

---

## ⚙️ Configuración

### Archivo .env

Ubicado en la raíz del proyecto. Contiene:

```ini
MONGODB_URI=mongodb+srv://martin456_db_user:Martokun32@cluster0.rwwx7sw.mongodb.net/?appName=Cluster0
```

**Nota:** Este archivo NO se sube a Git (está en .gitignore)

### MongoDB Atlas

Los datos se guardan automáticamente en MongoDB Atlas:
- **Base de datos:** `physiotrack`
- **Colecciones:** `fisioterapeutas`, `pacientes`

---

## 🎨 Diseño de la Interfaz

La aplicación utiliza colores profesionales acordes a una clínica:

- 🔵 **Azul (#0088cc)**: Color principal, botones, encabezados
- 🟢 **Verde Turquesa (#00b4a6)**: Color secundario, detalles
- 🔴 **Rojo (#ff6b6b)**: Alertas, urgencias
- 🟡 **Naranja (#ffa94d)**: Advertencias
- ✅ **Verde (#51cf66)**: Éxito, operaciones completadas

---

## 🆘 Solución de Problemas

### ❌ "Cannot connect to MongoDB"

**Solución:**
1. Verifica que `.env` exista en la raíz del proyecto
2. Verifica que la cadena de conexión sea correcta
3. Asegúrate de tener internet conectado
4. Verifica que MongoDB Atlas esté accesible

### ❌ "Port 8080 already in use"

**Solución:**
1. Cambia el puerto en `application.properties`:
   ```properties
   server.port=8081
   ```
2. O cierra la aplicación que usa el puerto 8080

### ❌ "Cannot resolve symbol" (en el IDE)

**Solución:**
1. Maven aún descarga las dependencias
2. Espera unos segundos
3. Si persiste: `mvn clean install` nuevamente

### ❌ "Java version mismatch"

**Solución:**
```bash
# Verifica tu versión Java
java -version

# Debe ser Java 17 o superior
# Si no, descárgalo desde: https://www.oracle.com/java/technologies/
```

---

## 📞 Datos de Ejemplo

### Fisioterapeuta:
```
Cédula: 1010123456
Nombre: Juan Pérez
Especialidad: Ortopedia
```

### Paciente:
```
Cédula: 1010654321
Nombre: María López
Edad: 45
Tipo de Lesión: Esguince de tobillo
Nivel: 7
Prioridad: 🟡 Seguimiento
```

---

## 📚 Más Información

Para documentación completa, ver: `INSTRUCCIONES.md`

---

**¡Bienvenido a PhysioTrack! 🏥**

