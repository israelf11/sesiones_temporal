
# Seguridad de la Información

El código de ejemplo de la presente carpeta demuestra cómo configurar la Transport Layer Security (TLS) así como el mTLS para proteger la comunicación de red hacia y dentro del clúster Temporal.

## Conexiones TLS y mTLS

1. Comenzamos ubicándonos en la carpeta `tls-component`, donde se encuentran los archivos para correr el servicio y configurar los certificados. Podemos encontrar en `generate-test-certs.sh` las instrucciones para generar los certificados necesarios. Podemos modificar las propiedades de acuerdo con las necesidades del caso. Generamos certificados de prueba con `generate-test-certs.sh`. Esto creará certificados de servidor y cliente en el subdirectorio `certs`. El archivo `generate-fake-test-certs.sh` sirve para generar certificados no compatibles en caso de querer probar cómo funciona este caso.

```bash
bash generate-test-certs.sh
```

2. Ahora podemos iniciar Temporal con `start-temporal.sh`. Esto abrirá un clúster Temporal (a través de `docker-compose`) con el subdirectorio `certs` montado como un volumen y Temporal configurado para usar los certificados de prueba que contiene para proteger las comunicaciones de red.

```bash
bash start-temporal.sh
```

#### Nota de contexto

Para demostrar la correcta ejecución del ejemplo, sugerimos primero ejecutar un workflow sin seguridad integrada. Para esto, es necesario ejecutar el método `main` del archivo `GreetingWorker` o ejecutar en consola:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.seguridad.GreetingStarter"
```

ubicándonos en la carpeta `/sesiones_de_entendimiento`. A continuación, solicitamos la ejecución de un workflow ejecutando el método `main` del archivo `GreetingStarter` o ejecutando en consola:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.seguridad.GreetingStarter"
```

Esto nos permite revalidar el correcto funcionamiento del servicio Temporal, obteniendo además un ejemplo del resultado esperado al ejecutar el mismo workflow con la seguridad integrada. En este punto, es necesario detener la ejecución del worker con `Ctrl + C`.

## Configuración de TLS

En la carpeta `tls-component` se encuentra el archivo `docker-compose.yml`, que configura las variables necesarias para la conexión mediante TLS. La línea de código más importante es la número 45: `TEMPORAL_TLS_REQUIRE_CLIENT_AUTH`, la cual, en su estado `true`, habilita el modo mTLS, y en su estado `false`, asigna la conexión como simple TLS.

3. El siguiente paso para continuar con la demostración es asegurarse de tener la variable `TEMPORAL_TLS_REQUIRE_CLIENT_AUTH` en `false`. A continuación, levanta el servicio de Docker Compose utilizando el siguiente comando desde la carpeta `tls-component`:

```bash
bash start-temporal.sh
```

Va a ser necesario crear un cliente de Temporal desde la aplicación Java para poder conectarse al nuevo servidor Temporal.

La configuración de la conexión se encuentra en `/seguridad/S1WFUtils.java`:

```java
static SslContextBuilder sslContextBuilder;

static {
    try {
        sslContextBuilder = GrpcSslContexts.forClient()
                .trustManager(new FileInputStream(caCertFile));
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }
}

// Crear opciones para el cliente
static WorkflowServiceStubsOptions options;

static {
    try {
        options = WorkflowServiceStubsOptions.newBuilder()
                .setSslContext(sslContextBuilder.build())
                .setTarget("127.0.0.1:7233") // Dirección del servidor Temporal
                .build();
    } catch (SSLException e) {
        throw new RuntimeException(e);
    }
}

// Crear instancia de WorkflowServiceStubs
static WorkflowServiceStubs serviceTLS = WorkflowServiceStubs.newInstance(options);
public static final WorkflowClient clientTLS = WorkflowClient.newInstance(serviceTLS)
        .build();
```

Basta con ejecutar nuevamente el Worker y el Workflow con los comandos anteriormente utilizados para comprobar que se recibe una excepción en tiempo de ejecución (`UNAVAILABLE: Network closed for unknown reason`).
Para lograr una conexión exitosa, es necesario ejecutar el método `main` del archivo `GreetingWorkerTLS` o ejecutar en consola:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.seguridad.GreetingStarterTLS"
```

ubicándonos en la carpeta `/sesiones_de_entendimiento`. A continuación, solicita la ejecución de un workflow ejecutando el método `main` del archivo `GreetingStarterTLS` o ejecutando en consola:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.seguridad.GreetingStarterTLS"
```

Como dato adicional, si se ejecutan archivos configurados para conexión mTLS, no saltará ninguna excepción; sin embargo, la conexión sigue siendo TLS.

Si se considera útil, es posible cambiar la lectura del archivo `caCertFile` en `S1WFUtils` por `fake-ca.cert` o cualquier otro archivo no configurado en el servidor Temporal para verificar que la conexión no será exitosa.

Desde el lado de consola, para utilizar cualquier comando será necesario incluir la variable `--tls_ca_path <<ruta al archivo>>`:

```bash
tctl|temporal --tls_ca_path ./certs/ca.cert workflow list
```

4. El siguiente paso para continuar con la demostración es asegurarse de tener la variable `TEMPORAL_TLS_REQUIRE_CLIENT_AUTH` en `true` y el worker anterior detenido. A continuación, vuelve a levantar el servicio de Docker Compose utilizando el siguiente comando desde la carpeta `tls-component`:

```bash
bash start-temporal.sh
```

El nuevo seteo de configuración para el cliente de la aplicación Java debe incluir los certificados del cliente:

La configuración de la conexión se encuentra en `/seguridad/S1WFUtils.java`:

```java
static {
        try {
            clientCert = new FileInputStream(clientCertFile);
            clientKey = new FileInputStream(clientCertPrivateKey);
            caCert = new FileInputStream(caCertFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static final SslContext sslContext;
    static {
        try {
            sslContext = GrpcSslContexts.configure(SslContextBuilder
                            .forClient()
                            .keyManager(clientCert, clientKey) // Certificado y clave del cliente
                            .trustManager(caCert)) // Certificado CA
                    .build();
        } catch (SSLException e) {
            throw new RuntimeException(e);
        }
    }
    public static final WorkflowServiceStubsOptions stubsOptions = WorkflowServiceStubsOptions.newBuilder()
            .setSslContext(sslContext)
            .setTarget("127.0.0.1:7233") // Dirección del servidor Temporal
            .build();
    public static final WorkflowServiceStubs service = WorkflowServiceStubs.newInstance(stubsOptions);
    public static final WorkflowClient clientmTLS = WorkflowClient.newInstance(service)
            .build();
```

Basta con ejecutar nuevamente el Worker y el Workflow con los comandos anteriormente utilizados para comprobar que se recibe una excepción en tiempo de ejecución (`UNAVAILABLE: ssl exception`).
Para lograr una conexión exitosa, es necesario ejecutar el método `main` del archivo `GreetingWorkerMTLS` o ejecutar en consola:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.seguridad.GreetingStarterMTLS"
```

ubicándonos en la carpeta `/sesiones_de_entendimiento`. A continuación, solicita la ejecución de un workflow ejecutando el método `main` del archivo `GreetingStarterMTLS` o ejecutando en consola:

```bash
mvn clean compile exec:java -Dexec.mainClass="io.workshop.seguridad.GreetingStarterMTLS"
```

Desde el lado de consola, para utilizar cualquier comando será necesario incluir la variable `--tls_ca_path <<ruta al archivo>> --tls_key_path <<ruta al archivo>> --tls_cert_path <<ruta al archivo>>`:

```bash
tctl|temporal --tls_ca_path ./certs/ca.cert --tls_key_path ./certs/client.key --tls_cert_path ./certs/client.pem workflow show --wid test-1
```

## Encriptación de datos (Codec Server)

En `codec/CustomPayloadCodec.java` podemos configurar los métodos `encode` y `decode`. Dichos métodos tendrán la responsabilidad de modificar la información que entra y sale de los workers.
Para asignarle la clase mencionada a los workflows, es necesario instanciar `setDataConverter` en la creación del cliente:

```java
WorkflowClient client = WorkflowClient.newInstance(service, WorkflowClientOptions.newBuilder()
            .setDataConverter(
                    new CodecDataConverter(
                            DefaultDataConverter.newDefaultInstance(),
                            Collections.singletonList(new CustomPayloadCodec()), true))
            .build());
```

En la carpeta `codecserver` reside un servidor utilizado cuando es necesario desencriptar información existente en Temporal que se quiere consultar.
De este modo, puede configurarse en la UI de Temporal el endpoint `http://localhost:8081/{namespace}` para poder ver el input y output de los workflows.

Desde el lado de la consola, si se quiere desencriptar información, será necesario incluir la variable `--codec_endpoint <<endpoint de codec server>>`:

```bash
temporal|tctl --codec_endpoint 'http://localhost:8081/{namespace}' workflow show --wid test-1
```
