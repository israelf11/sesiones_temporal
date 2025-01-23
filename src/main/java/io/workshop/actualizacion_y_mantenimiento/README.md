
# Facilidad de Mantenimiento y Actualización

El código de ejemplo de la presente carpeta demuestra las capacidades de actualización y de mantenimiento que Temporal ofrece.

#### Si no se tiene un servidor Temporal corriendo

Las imágenes base de Temporal se encuentran en `sesiones_de_entendimiento/src/docker-compose`. Si se quiere actualizar alguna versión, se modifica la variable requerida en el archivo `.env`. Para correr el servidor, se necesita posicionarse en dicha carpeta y ejecutar:

```bash
bash start-server.sh
```

## Versioning Workflows

La implementación del workflow de esta carpeta simula el cobro de mensualidades mediante timers (para este caso de segundos en lugar de un mes). Debemos correr el workflow para tener la versión -1 o Default registrada en el sistema. Es así que vamos a abrir el archivo `LoanProcessingWorker` y ejecutar su método `main`. Si se hace desde consola, es necesario correr el comando:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.actualizacion_y_mantenimiento.LoanProcessingWorker"
```

A continuación, comenzamos la ejecución del workflow ejecutando el método `main` de `Starter` o desde consola con el comando:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.actualizacion_y_mantenimiento.Starter"
```

### Prueba de Determinismo

Temporal permite probar si un workflow de una versión anterior sigue siendo válido para la misma implementación. Para comprobar esto, es necesario moverse desde consola a la carpeta `sesiones_de_entendimiento/src/test/java/resources` y ejecutar:

```bash
tctl wf show -wid loan-processing-workflow-customer-a100 --output_filename history_for_original_execution.json
```

Esto nos va a permitir probar la validez de la nueva versión desde la vista del determinismo.

### API Version

La implementación necesaria ya viene contemplada en el archivo `LoanProcessingWorkflowImpl`, solo basta con descomentar las líneas de código.

Basta con escribir la línea:

```java
int version = Workflow.getVersion(versionKey, Workflow.DEFAULT_VERSION, 1);
```

para tener una variable con la cual ir determinando qué instrucciones debe seguir cada versión. Por otro lado, como buena práctica debe incluirse el siguiente bloque de código:

```java
if (version != Workflow.DEFAULT_VERSION) {
    Workflow.upsertTypedSearchAttributes(TEMPORAL_CHANGE_VERSION.valueSet(Arrays.asList((versionKey + "-" + version))));
}
```

De esta manera, podremos ubicar fácilmente los workflows ejecutados con la nueva versión o los no pertenecientes a dicha versión.

Dicho esto, ejecutemos nuevamente nuestro workflow para poder observar los cambios. Volvemos a ejecutar el worker y el starter mencionados arriba. El worker debe ser reiniciado nuevamente para contar con la nueva implementación.

Si se comparan ambas ejecuciones, notarás que la actividad `SendThankYouToCustomer` pasó de ser la primera a ser la última. Es necesario versionar la implementación, puesto que mover una actividad produce un error de determinismo en las versiones ejecutándose anteriormente.

Para ver cómo se ve esto, comentemos el código anteriormente descomentado en la primera parte, incluyendo la primera aparición de:

```java
String confirmation = activities.sendThankYouToCustomer(info);
```

y comentemos las llaves del `if` de la última parte, dejando solo disponible la segunda aparición de:

```java
String confirmation = activities.sendThankYouToCustomer(info);
```

El resultado debe ser igual al anterior sin el version marker del inicio del flujo. A continuación, corremos el test de replay ubicado en la carpeta `sesiones_de_entendimiento/src/test/java/io/workshop/actualizacion_y_mantenimiento`, pudiendo observar el error de determinismo planteado antes:

```plaintext
queryType=__replay_only, args=Optional.empty, error=io.temporal.worker.NonDeterministicException: [TMPRL1100] Failure handling event 5 of type 'EVENT_TYPE_ACTIVITY_TASK_SCHEDULED' during replay. [TMPRL1100] Command COMMAND_TYPE_SCHEDULE_ACTIVITY_TASK doesn't match event EVENT_TYPE_ACTIVITY_TASK_SCHEDULED with EventId=5 on check activityType with an expected value 'name: "ChargeCustomer"'
```

Restableciendo el versionado y corriendo nuevamente el test, observaremos que no salta ningún problema.
