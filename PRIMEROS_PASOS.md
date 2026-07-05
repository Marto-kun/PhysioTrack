🚀 PRIMEROS PASOS CON PHYSIOTRACK

===============================================
PASO 1: VERIFICAR REQUISITOS
===============================================

Antes de ejecutar, asegúrate de tener:

1. ✅ Java 17 o superior instalado
   Verifica con: java -version
   
2. ✅ Maven 3.6 o superior instalado
   Verifica con: mvn -version
   
Si no tienes ambos, descárgalos de:
- Java: https://www.oracle.com/java/technologies/
- Maven: https://maven.apache.org/download.cgi

===============================================
PASO 2: CONFIGURAR VARIABLES DE ENTORNO
===============================================

1. En la carpeta raíz del proyecto, verifica que existe: .env

2. El archivo .env debe contener:
   MONGODB_URI=mongodb+srv://martin456_db_user:Martokun32@cluster0.rwwx7sw.mongodb.net/?appName=Cluster0

3. Si NO existe, créalo con este contenido:
   
   ----- COPIA ESTO EN UN ARCHIVO .env -----
   MONGODB_URI=mongodb+srv://martin456_db_user:Martokun32@cluster0.rwwx7sw.mongodb.net/?appName=Cluster0
   ----- FIN DEL CONTENIDO -----

📌 IMPORTANTE: El archivo .env NO se sube a Git (está protegido en .gitignore)

===============================================
PASO 3: COMPILAR Y EJECUTAR
===============================================

OPCIÓN A - Con Script (Recomendado para Windows):

1. Abre una terminal en la carpeta del proyecto
2. Ejecuta uno de estos:
   
   Para CMD:
   > ejecutar.bat
   
   Para PowerShell:
   > .\ejecutar.ps1

3. Espera a que termine la compilación
4. Verás: "Tomcat started on port(s): 8080"

OPCIÓN B - Manual con Maven:

1. Abre terminal en la carpeta del proyecto
2. Ejecuta:
   
   > mvn clean install
   
3. Luego ejecuta:
   
   > mvn spring-boot:run

4. Espera el mensaje de inicio

OPCIÓN C - Si ya está compilado:

> java -jar target/PhysioTrack-1.0-SNAPSHOT.jar

===============================================
PASO 4: ACCEDER A LA APLICACIÓN
===============================================

1. Abre tu navegador web (Chrome, Firefox, Edge, Safari)

2. Ve a: http://localhost:8080

3. ¡Deberías ver la pantalla de PhysioTrack!

===============================================
PRIMER USO - FLUJO RECOMENDADO
===============================================

A. REGISTRAR UN FISIOTERAPEUTA:
   
   1. En el menú lateral, click en "Registrar Fisioterapeuta"
   2. Completa el formulario:
      - Cédula: 1010123456 (ejemplo)
      - Nombre: Tu nombre
      - Especialidad: Selecciona una
   3. Click en "Guardar Fisioterapeuta"
   4. Deberías ver: "¡Fisioterapeuta registrado exitosamente!"

B. REGISTRAR UN PACIENTE:
   
   1. En el menú lateral, click en "Gestión de Pacientes"
   2. En el dropdown "Seleccione un Fisioterapeuta", elige el que creaste
   3. Click en "Registrar Nuevo Paciente"
   4. Completa el formulario:
      - Cédula: 1010654321 (ejemplo)
      - Nombre: Nombre del paciente
      - Edad: 45
      - Tipo de Lesión: Esguince
      - Nivel: 7
      - Asignar Fisioterapeuta: Selecciona el fisioterapeuta
   5. Click en "Guardar"
   6. El paciente aparecerá en la tabla

C. VER LOS DATOS:
   
   1. Vuelve a "Gestión de Pacientes"
   2. Selecciona el fisioterapeuta
   3. Verás el paciente en la tabla con su información y prioridad

===============================================
SOLUCIÓN RÁPIDA DE PROBLEMAS
===============================================

❌ "Error: Cannot resolve symbol 'Dotenv'"
✅ Solución: Es normal en el primer inicio. Ejecuta: mvn clean install
           Los errores desaparecerán después de la compilación.

❌ "Error: Address already in use"
✅ Solución: Puerto 8080 ocupado
           Cambia el puerto en application.properties:
           server.port=8081

❌ "Error: Cannot connect to MongoDB"
✅ Solución: Verifica que .env existe y contiene la cadena correcta
           O que tienes internet conectado

❌ "Exception: Java version 11 is not supported"
✅ Solución: Necesitas Java 17 o superior
           Descárgalo e instálalo

❌ "Error: mvn command not found"
✅ Solución: Maven no está en el PATH
           Reinstálalo o agrega Maven al PATH

===============================================
ESTRUCTURA DE CARPETAS (Para referencia)
===============================================

PyshioTrack/
├── .env ............................ Variables (NO SUBIR A GIT)
├── .env.example .................... Plantilla de ejemplo
├── pom.xml ......................... Dependencias Maven
├── ejecutar.bat .................... Script Windows
├── ejecutar.ps1 .................... Script PowerShell
├── INSTRUCCIONES.md ................ Documentación completa
├── GUIA_RAPIDA.md .................. Guía de usuario
├── PRIMEROS_PASOS.md ............... Este archivo
│
└── physiotrack/src/main/
    ├── java/
    │   ├── com/physiotrack/
    │   │   ├── Application.java
    │   │   └── gui/
    │   │       ├── MainView.java
    │   │       └── views/...
    │   ├── config/
    │   ├── services/
    │   ├── repositories/
    │   └── modelo/
    │
    └── resources/
        ├── application.properties
        └── styles.css

===============================================
DATOS DE EJEMPLO LISTOS PARA PROBAR
===============================================

FISIOTERAPEUTA:
  Cédula: 1010111111
  Nombre: Juan García
  Especialidad: Ortopedia

PACIENTE 1:
  Cédula: 1010222222
  Nombre: María López
  Edad: 45
  Tipo: Esguince de tobillo
  Nivel: 7
  → Prioridad: SEGUIMIENTO (naranja)

PACIENTE 2:
  Cédula: 1010333333
  Nombre: Carlos Rodríguez
  Edad: 70
  Tipo: Artrosis
  Nivel: 4
  → Prioridad: URGENTE (rojo - por edad)

PACIENTE 3:
  Cédula: 1010444444
  Nombre: Ana Martínez
  Edad: 25
  Tipo: Tendinitis
  Nivel: 2
  → Prioridad: PREVENTIVO (verde)

===============================================
COMANDOS ÚTILES
===============================================

Compilar solo:
  mvn clean compile

Ejecutar tests:
  mvn test

Limpiar archivos generados:
  mvn clean

Ver lista de dependencias:
  mvn dependency:tree

Detener la aplicación:
  Ctrl + C (en la terminal)

Reiniciar la aplicación:
  Cierra la terminal y vuelve a ejecutar

===============================================
PRÓXIMOS PASOS (Después del primer uso)
===============================================

1. Explora las diferentes funcionalidades
2. Crea más fisioterapeutas y pacientes
3. Prueba editar y eliminar registros
4. Observa cómo se calcula la prioridad automáticamente
5. Revisa los datos en MongoDB Atlas si tienes acceso

===============================================
PREGUNTAS FRECUENTES (FAQ)
===============================================

P: ¿Dónde están guardados los datos?
R: En MongoDB Atlas en la nube. Acceso en: https://cloud.mongodb.com/

P: ¿Puedo cambiar el puerto?
R: Sí, en physiotrack/src/main/resources/application.properties
   Busca: server.port=8080 y cámbialo

P: ¿Los datos persisten?
R: Sí, están en MongoDB. Se mantienen incluso si cierras la aplicación

P: ¿Puedo usar una base de datos local?
R: Sí, cambia MONGODB_URI a: mongodb://localhost:27017/physiotrack

P: ¿Necesito estar conectado a internet?
R: Sí, para MongoDB Atlas. Para local, no.

===============================================
CONTACTO Y AYUDA
===============================================

Documentación completa: Ver INSTRUCCIONES.md
Guía de uso: Ver GUIA_RAPIDA.md
Problemas: Revisa el archivo de logs o los mensajes de error

Autor: Martín Vozmediano
Institución: UDLA - Programación II
Proyecto: PhysioTrack v1.0

===============================================
✅ ¡ESTÁS LISTO PARA COMENZAR!
===============================================

Próximo paso: Abre una terminal y ejecuta:
   
   ejecutar.bat (en Windows CMD)
   O
   .\ejecutar.ps1 (en PowerShell)

¡Disfruta usando PhysioTrack! 🏥

