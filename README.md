# LearningSystem

La API expone un conjunto basico de endpoints REST para gestionar cursos.

### Recurso principal
`/api/cursos`

### Operaciones disponibles
- `GET /api/cursos`: lista todos los cursos.
- `GET /api/cursos/{id}`: obtiene un curso por su identificador.
- `POST /api/cursos`: crea un nuevo curso.
- `PUT /api/cursos/{id}`: actualiza un curso existente.
- `DELETE /api/cursos/{id}`: elimina un curso.

### Formato de datos
Los cursos manejan campos basicos como:
- nombre
- instructor
- duracionHoras
- costo

---

## Sistema de Inscripciones

### Recurso principal
`/api/inscripciones`

### Operaciones disponibles
- `POST /api/inscripciones`: crea una nueva inscripción y genera número de resumen automáticamente.
- `GET /api/inscripciones/{numeroResumen}`: obtiene una inscripción por su número de resumen.
- `GET /api/inscripciones`: lista todas las inscripciones.
- `GET /api/inscripciones/{numeroResumen}/generar-resumen`: genera y descarga el archivo físico del resumen de inscripción.
- `POST /api/inscripciones/{numeroResumen}/upload`: sube el resumen a un bucket de AWS S3 (guardado en carpeta con nombre del número de resumen).
- `PUT /api/inscripciones/{numeroResumen}/upload`: modifica el resumen en AWS S3.
- `GET /api/inscripciones/{numeroResumen}/download`: descarga el resumen desde AWS S3.
- `DELETE /api/inscripciones/{numeroResumen}`: borra el resumen de AWS S3.

### Formato de datos
Las inscripciones manejan campos como:
- nombreEstudiante
- emailEstudiante
- nombreCurso
- instructorCurso
- duracionHoras
- costoCurso
- fechaInscripcion
- numeroResumen

---

## Operaciones AWS S3

### Recurso principal
`/s3`

### Operaciones disponibles
- `GET /s3/getS3FileContent`: obtiene el contenido de un archivo en S3.
- `GET /s3/downloadS3File`: descarga un archivo desde S3.
- `DELETE /s3/deleteObject`: elimina un objeto de S3.
- `GET /s3/moveObject`: mueve un objeto dentro de S3.
- `POST /s3/uploadObject`: sube un archivo a S3.

