# Capacidades de Orquestación y Coreografía

El código de ejemplo de la presente carpeta ofrece una solución mediante Temporal para la orquestación de sistemas.

#### Si no se tiene un servidor Temporal corriendo

Las imágenes base de Temporal se encuentran en `sesiones_de_entendimiento/src/docker-compose`. Si se quiere actualizar alguna versión, se modifica la variable requerida en el archivo `.env`. Para correr el servidor, se necesita posicionarse en dicha carpeta y ejecutar:

```bash
bash start-server.sh
```

## Pasos para la Ejecución

Para comenzar el worker, debemos ejecutar el método `main` del archivo `SendSignalClientWorker` o ejecutar en consola:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.orquestation.SendSignalClientWorker"
```

ubicándonos en la carpeta `/sesiones_de_entendimiento`.

A continuación, pasamos a la ejecución corriendo el método `main` del archivo `Starter` o ejecutamos en consola:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.orquestation.Starter"
```

De este modo, podemos estudiar cómo la utilización de señales permite esperar interacciones de sistemas externos. La
ejecución del workflow va a esperar una señal para continuar o cancelarse al llegar un timeout.

Para concluir con el proceso, debemos ejecutar el método `main` de `SignalClient` para autorizar que el proceso
continúe.
