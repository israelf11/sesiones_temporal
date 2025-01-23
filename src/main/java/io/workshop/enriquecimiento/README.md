
# Capacidades de Transformación y Enriquecimiento de Datos

El código de ejemplo de la presente carpeta ofrece una solución mediante Temporal para la generación de payloads tras el llamado a múltiples microservicios.

#### Si no se tiene un servidor Temporal corriendo

Las imágenes base de Temporal se encuentran en `sesiones_de_entendimiento/src/docker-compose`. Si se quiere actualizar alguna versión, se modifica la variable requerida en el archivo `.env`. Para correr el servidor, se necesita posicionarse en dicha carpeta y ejecutar:

```bash
bash start-server.sh
```

## Pasos para la Ejecución

Para comenzar el worker, debemos ejecutar el método `main` del archivo `GenerateRequestWorker` o ejecutar en consola:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.enriquecimiento.GenerateRequestWorker"
```

ubicándonos en la carpeta `/sesiones_de_entendimiento`.

A continuación, pasamos a la ejecución corriendo el método `main` del archivo `Starter` o ejecutamos en consola:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.enriquecimiento.Starter"
```

De este modo, podemos estudiar el múltiple llamado de actividades asíncronas desde un workflow para la formación de un único payload.
