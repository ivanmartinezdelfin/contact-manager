# Contact Manager: (Java puro, consola).
## Descripción breve: 
Aplicacíon de consola en java puro para gestionar contactos (personas y empresas).
Enfocada en fundamentos sólidos: OOP (herencia/abstracción) interfaces, colecciones, manejo de excepciones y persistencia simple a archivo.

## Features:
* CRUD completo de contactos:crear, listar, buscar, actualizar, eliminar.
* Dos tipos de contacto usando herencia:
    * PERSON (Persona).
    * COMPANY (Empresa) con campo extra "Empresa".
* Búsqueda por nombre (case-insensitive).
* Validaciones: nombre no vacío, email válido, teléfono válido (patrones simples).
* Persistencia simple a archibo: los contactos se mantienen entre ejecuciones.
* Manejo de errores: entradas inválidas, id inexistente, y fallos de IO sin romper la app.

## Requisitos
* Java 17+ (o 11+ si ajustas según tu instalación).
* Terminal (Windows / Linux / macOS).

## Estructura del proyecto
* src/main/java/com/portfolio/contacts/
    * domain/ (modelo + OOP)
    * repo/ (contratos + persistencia)
    * cli/ (UI consola)
    * util/ (IO + validación)
* data/contacts.db (se crea automáticamente al ejecutar)


## Compilar
javac -d out $(find src/main/java -name "*.java")

## Ejecutar
java -cp out com.portfolio.contacts.App


## Dónde se guardan los datos
    * Se crea automáticamente.
    * Se actualiza al agregar/actualizar/eliminar contactos.
    * No es necesario editarlo a mano.

## Example usage

=== Contact Manager ===

Agregar contacto (Persona)

Agregar contacto (Empresa)

Listar contactos

Buscar por nombre

Actualizar contacto por id

Eliminar contacto por id

Salir

-- Alta Persona --
Nombre: Ana López
Teléfono: +52 81 1234 5678
Email: ana.lopez@email.com

Notas (opcional): Amiga de la universidad

Creado: [PERSON] Ana López | +52 81 1234 5678 | ana.lopez@email.com
 | Notas: Amiga de la universidad | id=...

 ## Decisiones técnicas

 1. Patrón Repository (ContactRepository)
 Separé el acceso a datos del resto del sistema mediante una interfaz
 (ContactRepository). Esto desacopla la lógica de negocio (ContactService) del almacenamiento real. Gracias a eso puedo cambiar de InMemory a File sin tocar la UI ni la capa de negocio, y el diseño queda preparado para una migración futura a JDBC/JPA/PostgreSQL.
 2. Persistencia simple en archivo (type|id|..)
 Usé un formato lineal de texto por contacto para mantener persistencia sin frameworks. Cada línea representa un registro con campos delimitados
 (tipo|id|nombre|teléfono|email|extra). Esto demuestra serializacion/deserialización y manejo de errores/IO. Además, incluye un escape básico para no romper el delimitador.
 3. Diseño inside-out (dominio -> casos de uso -> infraestructura -> UI)
 implementé primero el dominio (Contact, PersonContact, CompanyContact), luego los contratos y casos de uso (Repository + Service), y al final infraestructura (archivo) y UI (Menú). Este enfoque reduce acoplamiento y mantiene el core independiente.

 Roadmap / mejoras futuras (opcional, buen plus)
 * Exportar/importar CSV.
 * Validación más estrictaa (regex mejoradas o reglas por país).
 * Tests unitarios para ContactService y parsing del repositorio de archivo.
 * Migración a Spring Boot + PostgreSQL (misma idea de capas y repositorio).

 Licencia MIT u opcional ("sin licencia" si no quieres).

 ## Pulido de estilo y nombres (checklist rápido)

 * Mensajes consistentes("Opción", "Id", "Sin resultados", "Creado/ACtualizado/Eliminado).
 * Evita mezclar español e ingles en el UI; elige uno.
 * Ordena listados por nombre (ya lo haces en service/repo).
 * No uses System.out en capas internas salvo warnings controlados; idealmente centraliza en UI, pero para un proyecto simple se tolera en FileRepo para warnings.
 * Asegura que todos los paquetes están bien nombrados: com.portfolio.contacts.(domain/repo/service/util).
 * Mantén métodos cortos en Menu (ya está dividido por acción).
 