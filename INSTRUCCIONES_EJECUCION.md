# 📋 Instrucciones para la Ejecución de PhysioTrack

## 📌 Tabla de Contenidos
1. [Requisitos del Sistema](#requisitos-del-sistema)
2. [Pasos para Abrir el Proyecto en el IDE](#pasos-para-abrir-el-proyecto-en-el-ide)
3. [Cómo Iniciar la Aplicación](#cómo-iniciar-la-aplicación)
4. [Verificación de la Instalación](#verificación-de-la-instalación)
5. [Solución de Problemas](#solución-de-problemas)

---

## 🖥️ Requisitos del Sistema

### Software Obligatorio
- **Java Development Kit (JDK):** Versión 11 o superior
  - Recomendado: JDK 17 LTS o superior
  - [Descargar JDK](https://www.oracle.com/java/technologies/downloads/)
  
- **Apache Maven:** Versión 3.6.0 o superior
  - El proyecto incluye un wrapper (`mvnw.cmd` y `mvnw`) que gestiona Maven automáticamente
  - [Descargar Maven](https://maven.apache.org/download.cgi)

- **MongoDB Atlas:** Base de datos en la nube
  - Cuenta activa en [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
  - Cluster configurado y cadena de conexión disponible
  - Base de datos `physiotrack` creada

### Herramientas de Desarrollo Recomendadas
- **IDE:** IntelliJ IDEA Community o Ultimate Edition
  - Alternativa: Eclipse IDE, VS Code con extensiones de Spring
  - [Descargar IntelliJ IDEA](https://www.jetbrains.com/idea/)

- **Git:** Para control de versiones
  - [Descargar Git](https://git-scm.com/)

- **Postman o Insomnia:** Opcional, para probar APIs
  - [Descargar Postman](https://www.postman.com/)

### Requisitos Mínimos de Hardware
- **Procesador:** Dual-core o superior
- **RAM:** Mínimo 4 GB (recomendado 8 GB)
- **Disco:** 2 GB de espacio libre

### Navegador Web Compatible
- Chrome/Chromium (recomendado)
- Firefox
- Safari
- Edge

---

## 🚀 Pasos para Abrir el Proyecto en el IDE

### Opción 1: Usando IntelliJ IDEA (Recomendado)

#### Paso 1: Clonar o Abrir el Proyecto
```bash
# Si aún no tienes el proyecto localmente
git clone https://github.com/Marto-kun/PhysioTrack.git
cd PyshioTrack
```

#### Paso 2: Abrir en IntelliJ IDEA
1. Abre **IntelliJ IDEA**
2. Selecciona **File → Open** (Archivo → Abrir)
3. Navega a la carpeta raíz del proyecto `PyshioTrack`
4. Haz clic en **Open** (Abrir)
5. Espera a que IntelliJ cargue y reconozca la estructura del proyecto

#### Paso 3: Configurar el JDK
1. Ve a **File → Project Structure** (Archivo → Estructura del Proyecto)
2. En la sección **Project**:
   - **SDK:** Selecciona tu JDK 11+ instalado
   - Si no aparece, haz clic en **Edit** y añade la ubicación de tu JDK
3. Haz clic en **Apply** y luego **OK**

#### Paso 4: Cargar Dependencias Maven
1. Abre el panel **Maven** en la derecha (View → Tool Windows → Maven)
2. Verás un árbol con el proyecto
3. Haz clic derecho en el proyecto → **Reload Projects**
4. Maven descargará todas las dependencias automáticamente
5. Espera a que termine (verás un checkmark verde)

---

### Opción 2: Usando Eclipse IDE

#### Paso 1: Clonar el Proyecto
```bash
git clone https://github.com/Marto-kun/PhysioTrack
cd PyshioTrack
```

#### Paso 2: Importar como Proyecto Maven
1. Abre **Eclipse**
2. Ve a **File → Import** (Archivo → Importar)
3. Selecciona **Maven → Existing Maven Projects** (Maven → Proyectos Maven Existentes)
4. Haz clic en **Next**
5. Navega a la carpeta `PyshioTrack` como raíz
6. Haz clic en **Finish**

#### Paso 3: Configurar el JDK
1. Haz clic derecho en el proyecto → **Properties** (Propiedades)
2. Selecciona **Java Build Path**
3. En la pestaña **Libraries**, verifica que el JDK sea correcto
4. Haz clic en **Apply and Close**

---

### Opción 3: Desde la Línea de Comandos (VS Code)

#### Paso 1: Clonar y Abrir
```bash
git clone https://github.com/Marto-kun/PhysioTrack.git
cd PyshioTrack
code .
```

#### Paso 2: Instalar Extensiones en VS Code
1. Abre VS Code
2. Ve a **Extensions** (Extensiones)
3. Instala:
   - **Extension Pack for Java** (Microsoft)
   - **Spring Boot Extension Pack** (VMware)
   - **Maven for Java** (Microsoft)

#### Paso 3: Maven cargará automáticamente
- VS Code reconocerá el archivo `pom.xml` y cargará las dependencias

---

## ⚙️ Cómo Iniciar la Aplicación

### Paso 1: Configurar la Conexión a MongoDB

#### Localizar el archivo de configuración
- Ruta: `physiotrack/src/main/resources/application.properties`

#### Editar las propiedades
```properties
# Cadena de conexión a MongoDB Atlas
spring.data.mongodb.uri=mongodb+srv://<USUARIO>:<CONTRASEÑA>@<CLUSTER>.mongodb.net/physiotrack?retryWrites=true&w=majority

# O configuración alternativa (si usas host local)
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=physiotrack
spring.data.mongodb.username=<USUARIO_OPCIONAL>
spring.data.mongodb.password=<CONTRASEÑA_OPCIONAL>
```

**Nota:** Reemplaza `<USUARIO>`, `<CONTRASEÑA>` y `<CLUSTER>` con tus credenciales reales de MongoDB Atlas.

### Paso 2: Ejecutar la Aplicación

#### Opción A: Desde IntelliJ IDEA
1. Abre el archivo `src/main/java/main/java/com/physiotrack/PhysioTrackApplication.java`
   - O busca la clase anotada con `@SpringBootApplication`
2. Haz clic en el botón **Run** (▶) verde en la esquina superior derecha
3. La aplicación se iniciará en el puerto `8080`

#### Opción B: Desde Eclipse
1. Haz clic derecho en el proyecto
2. Selecciona **Run As → Spring Boot App**
3. La aplicación se iniciará en el puerto `8080`

#### Opción C: Desde la Terminal (Recomendado)

**En Windows (PowerShell):**
```powershell
# Navega a la raíz del proyecto
cd C:\Ruta\A\PyshioTrack

# Ejecuta el comando Maven
.\mvnw.cmd -DskipTests spring-boot:run
```

**En Linux/macOS (Bash/Zsh):**
```bash
# Navega a la raíz del proyecto
cd /ruta/a/PyshioTrack

# Ejecuta el comando Maven
./mvnw -DskipTests spring-boot:run
```

**Explicación de flags:**
- `-DskipTests`: Omite la ejecución de tests (acelera el inicio)
- `spring-boot:run`: Inicia la aplicación Spring Boot

### Paso 3: Acceder a la Aplicación

Una vez que ves en la consola un mensaje similar a:
```
Started PhysioTrackApplication in 5.234 seconds (JVM running for 5.678)
```

1. Abre tu navegador web
2. Dirígete a: **http://localhost:8080**
3. Deberías ver la interfaz de inicio de **PhysioTrack**

---

## ✅ Verificación de la Instalación

### Verificar Java
```bash
java --version
```
Debe mostrar una versión 11 o superior.

### Verificar Maven
```bash
mvn --version
```
Debe mostrar Maven 3.6.0 o superior.

### Verificar Conexión a MongoDB
1. Abre la consola de la aplicación
2. Busca mensajes de conexión exitosa (típicamente verás logs sin errores de conexión)
3. Si hay errores, revisa la sección de **Solución de Problemas**

### Probar la Aplicación
1. Ve a http://localhost:8080
2. Deberías ver los botones del menú:
   - Registrar Fisioterapeuta
   - Gestión de Pacientes
   - Gestión de Inventario
   - Registrar Sesiones
   - Planes de Rehabilitación

---

## 🔧 Solución de Problemas

### Problema: "Port 8080 is already in use"
**Solución:**
```bash
# En Windows PowerShell
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# En Linux/macOS
lsof -i :8080
kill -9 <PID>
```

O cambiar el puerto en `application.properties`:
```properties
server.port=8081
```

### Problema: "Failed to connect to MongoDB"
**Solución:**
1. Verifica tu cadena de conexión en `application.properties`
2. Verifica que el cluster de MongoDB Atlas esté activo
3. Whitelist tu dirección IP en MongoDB Atlas (Security → Network Access)
4. Revisa usuario y contraseña

### Problema: Maven descarga lentamente
**Solución:**
```bash
# Limpiar caché local
mvn clean

# Reintentar descarga
mvn dependency:resolve
```

### Problema: "JDK not found"
**Solución:**
1. Verifica que Java esté instalado: `java -version`
2. En IntelliJ: File → Project Structure → Project → SDK → selecciona JDK
3. En Windows, agrega Java a la variable de entorno `PATH`

### Problema: Aplicación carga pero no se ve contenido
**Solución:**
1. Abre la consola del navegador (F12)
2. Busca errores en la pestaña **Console**
3. Verifica que MongoDB esté accesible
4. Reinicia la aplicación con `mvn clean spring-boot:run`

---

## 📝 Comandos Útiles

```bash
# Compilar sin ejecutar
mvn clean install

# Ejecutar tests
mvn test

# Ejecutar con debug
mvn -Dspring-boot.run.arguments="--debug" spring-boot:run

# Ver logs en tiempo real
mvn spring-boot:run -Dspring-boot.run.arguments="--logging.level.root=DEBUG"

# Detener la aplicación (en terminal)
Ctrl + C
```

---

## 🎓 Recursos Adicionales

- [Documentación de Spring Boot](https://spring.io/projects/spring-boot)
- [Documentación de Vaadin Flow](https://vaadin.com/docs)
- [Documentación de MongoDB](https://docs.mongodb.com/)
- [Guía de Maven](https://maven.apache.org/guides/)

---

## 📞 Contacto y Soporte

Si encuentras problemas:
1. Revisa los **logs de la consola** (mensaje de error exacto)
2. Consulta la sección **Solución de Problemas**
3. Verifica que todos los requisitos estén instalados
4. Reinicia el IDE y la aplicación

---

**Última actualización:** 2026-07-05  
**Versión de la Aplicación:** 1.0  
**Plataforma:** Spring Boot 2.x + Vaadin Flow 24 + MongoDB

