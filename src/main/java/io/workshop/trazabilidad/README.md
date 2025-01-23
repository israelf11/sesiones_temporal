
# Capacidades de Auditoría y Trazabilidad

El código de ejemplo de la presente carpeta demuestra las funcionalidades integradas y configurables sobre la auditoría y trazabilidad.

### Trazabilidad Integrada

Las imágenes base de Temporal se encuentran en `sesiones_de_entendimiento/src/docker-compose`. Si se quiere actualizar alguna versión, se modifica la variable requerida en el archivo `.env`. Para correr el servidor, se necesita posicionarse en dicha carpeta y ejecutar:

```bash
bash start-server.sh
```

Para hablar de las capacidades integradas sobre este tema, requerimos contar con un workflow en el historial. Para tal fin, vamos primero a ejecutar el workflow más simple con el que contamos. Nos ubicamos en `seguridad/GreetingWorker` y ejecutamos su método `main` o ejecutamos en consola:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.seguridad.GreetingWorker"
```

ubicándonos en la carpeta `/sesiones_de_entendimiento`. A continuación, solicitamos la ejecución de un workflow ejecutando el método `main` del archivo `GreetingStarter` o ejecutamos en consola:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.seguridad.GreetingStarter"
```

### La Trazabilidad de Temporal UI

Abriendo el detalle del workflow dentro de la página `http://localhost:8080/`, podemos observar:

- La entidad que solicitó la ejecución del workflow.
- El recorrido paso a paso de la ejecución.
- La entidad del worker que ejecutó el workflow.
- El input y output de la ejecución.
- La hora de inicio, fin y duración por actividad.

## Visibilidad Avanzada

Por otro lado, en el presente ejemplo podemos habilitar índices para cada workflow, permitiéndonos encontrar con facilidad workflows mediante consultas similares a SQL. Para esto, es necesario contar con ElasticSearch conectado al servidor.

1. Como primer paso, es necesario añadir los índices al servidor mediante consola:

```bash
tctl admin cluster add-search-attributes --name OperationName --type Text
tctl admin cluster add-search-attributes --name OperationType --type Keyword
tctl admin cluster add-search-attributes --name OperationAmount --type Double
```

Para verificar el paso anterior, podemos ejecutar `tctl cluster get-search-attributes` y verificar que están presentes.

2. Lo siguiente es ejecutar el método `main` del archivo `GreetingWorker` de esta carpeta o ejecutar en consola:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.trazabilidad.GreetingWorker"
```

ubicándonos en la carpeta `/sesiones_de_entendimiento`. A continuación, solicitamos la ejecución de un workflow ejecutando el método `main` del archivo `GreetingStarter` o ejecutamos en consola:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.trazabilidad.GreetingStarter"
```

3. Recargando `http://localhost:8080/`, encontraremos nuevos workflows. Dando clic al botón de filtros, encontraremos nuevos campos añadidos disponibles ("OperationName", "OperationType", "OperationAmount").

4. También es posible buscar workflows por índices añadiendo el parámetro `--query <<parámetro de búsqueda>>`:

```bash
tctl workflow list --query 'OperationType = "Cargo"'
tctl workflow list --query 'OperationAmount >= 25'
```

5. Finalmente, en `S1WFUtils` podemos ver en el método `main` funciones para descomentar y probar cómo funciona el uso de consultas desde el código.
