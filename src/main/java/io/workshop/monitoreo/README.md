
# Monitoreo en Tiempo Real

El código de ejemplo de la presente carpeta demuestra la facilidad para configurar herramientas de monitoreo desde el servicio de Temporal.

## Sobre el Servidor

Este repositorio incluye algunos experimentos sobre la implementación de un servidor Temporal a través de Docker Compose y Swarm. Puede servir como referencia para la comunidad para una serie de preguntas relacionadas con la implementación de Docker. Para este repositorio, usamos PostgreSQL para la persistencia de la base de datos. Configuramos una visibilidad avanzada con Postgres DB (pero son posibles las opciones con OpenSearch/ElasticSearch) para `temporal_visibility`. También muestra cómo configurar el servicio de UI y workers.

Esta configuración está orientada a la mayoría de entornos de producción, ya que implementa cada función del servidor Temporal (frontend, matching, history, worker) en contenedores individuales. La configuración ejecuta dos instancias para los hosts frontend, matching e history.

### Cómo Iniciar el Docker Compose

Primero necesitamos instalar el plugin Loki (tienes que hacer esto solo una vez):

```bash
docker plugin install grafana/loki-docker-driver:latest --alias loki --grant-all-permissions
```

Para verificar si se instaló correctamente:

```bash
docker plugin ls
```

Después iniciamos los Docker Compose desde `/server-component` con:

```bash
bash start-server.sh
```

### Conocer las Vistas

Para tener flujo de datos, necesitamos conectar el worker a un endpoint de Prometheus. Podemos encontrar la configuración en `MetricsWorker`.

Con el fin de iniciar el flujo, requerimos ejecutar el método `main` del archivo `MetricsWorker` o ejecutar en consola:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.monitoreo.MetricsWorker"
```

ubicándonos en la carpeta `/sesiones_de_entendimiento`. A continuación, solicitamos la ejecución de un workflow ejecutando el método `main` del archivo `MetricsStarter` o ejecutando en consola:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.monitoreo.MetricsStarter"
```

#### Grafana

Lo siguiente es abrir un navegador y dirigirse al siguiente link:

```plaintext
http://localhost:8085/
```

En los distintos dashboards se encuentran las posibles métricas que pueden visualizarse.
