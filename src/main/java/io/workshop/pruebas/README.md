# Soporte a Pruebas de Resiliencia y Simulación de Servicios

El código de ejemplo de la presente carpeta demuestra las capacidades de testeo con las que cuenta la plataforma
Temporal.

## Pruebas Unitarias

Los tests de Temporal no requieren de un servidor en ejecución. Tampoco debemos preocuparnos de los timers dentro de los
workflows. En la carpeta `sesiones_de_entendimiento/src/test/java/io/workshop/seguridad` podemos encontrar el archivo
`GreetingWorkflowTest`, el cual presenta un ejemplo de cómo es posible implementar un test de un workflow.

Podemos ejecutar el test para comprobar que el timer `Workflow.sleep(Duration.ofSeconds(100));` es saltado. Para esto,
podemos ejecutar el método `testWorkflow` de la clase `GreetingWorkflowTest` o desde consola ejecutando:

```bash
mvn -Dtest=GreetingWorkflowTest#testWorkflow test
```

## Simulación de Servicios

Moviéndonos a la carpeta aledaña (`sesiones_de_entendimiento/src/test/java/io/workshop/pruebas`), vamos a encontrar el
archivo `ContentLengthWorkflowTest`, el cual permite analizar cómo se pueden probar actividades aisladas. Al mismo
tiempo, podemos encontrar un ejemplo de implementación de una actividad.
