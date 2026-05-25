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

