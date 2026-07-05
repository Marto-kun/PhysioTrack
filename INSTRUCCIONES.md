# PhysioTrack - Sistema de Gestión de Fisioterapia

## 📋 Descripción

PhysioTrack es una aplicación web desarrollada con **Vaadin** y **Spring Boot** que permite gestionar de manera eficiente:

- **Registro de Fisioterapeutas**: Crear perfiles de profesionales de la salud con sus datos básicos y especialidades.
- **Gestión de Pacientes**: Visualizar, registrar y asignar pacientes a los fisioterapeutas.
- **Persistencia de Datos**: Almacenamiento en **MongoDB Atlas** en la nube.

## 🎨 Interfaz Visual

La aplicación utiliza una paleta de colores acorde a una clínica de fisioterapia:

- **Azul Profesional** (#0088cc): Color principal
- **Verde Turquesa** (#00b4a6): Color secundario
- **Rojo Suave** (#ff6b6b): Alertas y urgencias
- **Verde** (#51cf66): Éxito
- **Naranja** (#ffa94d): Advertencias

## 🚀 Instalación y Configuración

### Requisitos Previos

- **Java 17+**
- **Maven 3.6+**
- **MongoDB Atlas Account** (opcional, para usar la nube)
- **Git**

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone <URL-del-repositorio>
   cd PyshioTrack
   ```

2. **Configurar Variables de Entorno**
   
   Crea un archivo `.env` en la raíz del proyecto:
   ```
   MONGODB_URI=mongodb+srv://martin456_db_user:Martokun32@cluster0.rwwx7sw.mongodb.net/?appName=Cluster0
   ```

   **Nota**: El archivo `.env` está en `.gitignore` por seguridad.

3. **Compilar el Proyecto**
   ```bash
   mvn clean install
   ```

4. **Ejecutar la Aplicación**
   ```bash
   mvn spring-boot:run
   ```

   O, si está compilado:
   ```bash
   java -jar target/PhysioTrack-1.0-SNAPSHOT.jar
   ```

5. **Acceder a la Aplicación**
   
   Abre tu navegador web en: `http://localhost:8080`

## 📱 Características Principales

### 1. Pantalla Principal
- **Navegación lateral** para acceder a las diferentes secciones
- **Encabezado** con branding de PhysioTrack
- Interfaz limpia y profesional

### 2. Registro de Fisioterapeutas
- Formulario para registrar nuevos fisioterapeutas
- Campos: Cédula, Nombre Completo, Especialidad
- Validación de datos
- Notificaciones de éxito/error

**Especialidades disponibles**:
- Fisioterapia General
- Ortopedia
- Neurología
- Cardiología
- Pediatría
- Deportología
- Reumatología

### 3. Gestión de Pacientes
- **Selector de Fisioterapeuta**: Ver pacientes de cada profesional
- **Tabla de Pacientes**: Mostrar todos los pacientes asignados
  - Cédula
  - Nombre
  - Edad
  - Tipo de Lesión
  - Nivel de Lesión (1-10)
  - Prioridad (Urgente, Seguimiento, Preventivo)
- **Acciones**:
  - Registrar nuevo paciente
  - Editar paciente existente
  - Eliminar paciente
  - Asignar paciente a fisioterapeuta

### 4. Cálculo Automático de Prioridad
La prioridad se calcula automáticamente según:
- **Urgente**: Nivel de Lesión ≥ 8 O Edad ≥ 65
- **Seguimiento**: Nivel de Lesión ≥ 5
- **Preventivo**: Nivel de Lesión < 5

## 💾 Persistencia de Datos - MongoDB Atlas

La aplicación utiliza MongoDB Atlas para almacenar todos los datos en la nube.

### Colecciones

1. **fisioterapeutas**
   ```json
   {
     "_id": "ObjectId",
     "cedula": "string",
     "nombre": "string",
     "especialidad": "string",
     "pacientesAsignados": ["array de Pacientes"]
   }
   ```

2. **pacientes**
   ```json
   {
     "_id": "ObjectId",
     "cedula": "string",
     "nombre": "string",
     "edad": "number",
     "tipoLesion": "string",
     "nivelLesion": "number",
     "prioridad": "string",
     "fisioterapeutaAsignado": "DBRef a Fisioterapeuta"
   }
   ```

## 📁 Estructura del Proyecto

```
PyshioTrack/
├── .env                                          # Variables de entorno (NO commit)
├── .gitignore                                    # Archivos ignorados por Git
├── pom.xml                                       # Dependencias Maven
│
└── physiotrack/src/main/
    ├── java/
    │   ├── com/physiotrack/
    │   │   ├── Application.java                 # Clase principal de Spring Boot
    │   │   └── gui/
    │   │       ├── MainView.java                # Vista principal
    │   │       └── views/
    │   │           ├── RegistroFisioterapeutaView.java
    │   │           └── PacientesView.java
    │   ├── config/
    │   │   └── EnvironmentConfiguration.java    # Configuración de variables de entorno
    │   ├── main/java/
    │   │   ├── modelo/
    │   │   │   ├── Fisioterapeuta.java
    │   │   │   └── Paciente.java
    │   │   └── negocio/
    │   │       ├── GestorPacientes.java
    │   │       └── ...
    │   ├── repositories/
    │   │   ├── FisioterapeutaRepository.java
    │   │   └── PacienteRepository.java
    │   └── services/
    │       ├── FisioterapeutaService.java
    │       └── PacienteService.java
    │
    └── resources/
        ├── application.properties                # Configuración Spring Boot
        ├── styles.css                           # Estilos personalizados
        └── META-INF/resources/
```

## 🔧 Configuración de Spring Boot

El archivo `application.properties` configura:

```properties
# Puerto del servidor
server.port=8080

# Logging
logging.level.org.atmosphere=warn

# Vaadin
vaadin.launch-browser=true
vaadin.allowed-packages=com.vaadin,org.vaadin,com.flowingcode,com.physiotrack

# MongoDB
spring.data.mongodb.uri=${MONGODB_URI:mongodb://localhost:27017/physiotrack}
spring.data.mongodb.database=physiotrack
```

## 🔐 Seguridad

- Las credenciales de MongoDB se almacenan en `.env` (excluido de Git)
- La aplicación valida todos los datos de entrada
- Se utilizan notificaciones para errores y éxito

## 📝 Notas Importantes

### MongoDB Atlas
- La conexión utiliza la cadena de conexión proporcionada
- Si no se proporciona MONGODB_URI, se intenta conectar a `mongodb://localhost:27017`
- Asegúrate de que el archivo `.env` esté en la raíz del proyecto

### Variables de Entorno
```bash
# .env
MONGODB_URI=mongodb+srv://usuario:contraseña@cluster.mongodb.net/?appName=Cluster0
```

### Compilación
El proyecto utiliza Maven con Java 17. Asegúrate de tener la versión correcta instalada:
```bash
java -version
```

## 🚧 Desarrollo Futuro

Características planeadas:
- [ ] Generación de reportes PDF
- [ ] Gestión de sesiones de tratamiento
- [ ] Calendario de citas
- [ ] Sistema de notificaciones
- [ ] Dashboard con estadísticas
- [ ] Autenticación de usuarios
- [ ] Control de acceso por rol

## 👥 Autor

**Martín Vozmediano**
- Desarrollo de la arquitectura y lógica de negocio
- Integración con Vaadin y Spring Boot
- Configuración de MongoDB

## 📞 Soporte

Para problemas o consultas, contacta al equipo de desarrollo.

---

**Última actualización**: 4 de Julio de 2026

