package io.workshop.seguridad;

import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.common.RetryOptions;
import io.temporal.common.converter.CodecDataConverter;
import io.temporal.common.converter.DefaultDataConverter;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.workshop.seguridad.codec.CustomPayloadCodec;

import javax.net.ssl.SSLException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;


public class S1WFUtils {
    private static final int MAX_INBOUND_MESSAGE_SIZE = 52_428_800;
    static Path currentPath = Paths.get(System.getProperty("user.dir"));
    public static String clientCertFile = Paths.get(currentPath.toString(), "src", "main", "java", "io", "workshop", "seguridad", "client.pem").toString();
    public static String clientCertPrivateKey = Paths.get(currentPath.toString(), "src", "main", "java", "io", "workshop", "seguridad", "client.key").toString();
    public static String caCertFile = Paths.get(currentPath.toString(), "src", "main", "java", "io", "workshop", "seguridad", "ca.cert").toString();
    public static InputStream clientCert;
    public static InputStream clientKey;
    public static InputStream caCert;

    // mTLS
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
    public static final WorkflowClient clientmTLS = WorkflowClient.newInstance(service, WorkflowClientOptions.newBuilder()
            .setDataConverter(
                    new CodecDataConverter(
                            DefaultDataConverter.newDefaultInstance(),
                            Collections.singletonList(new CustomPayloadCodec()), true))
            .build());

    public static final String taskQueue = "greeting";

    public static final RetryOptions NO_RETRY = RetryOptions.newBuilder().setMaximumAttempts(1).build();

    public S1WFUtils() throws FileNotFoundException, SSLException {
    }

    //TLS
    static SslContextBuilder sslContextBuilder;

    static {
        try {
            sslContextBuilder = GrpcSslContexts.forClient()
                    .trustManager(new FileInputStream(caCertFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

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

    static WorkflowServiceStubs serviceTLS = WorkflowServiceStubs.newInstance(options);
    public static final WorkflowClient clientTLS = WorkflowClient.newInstance(serviceTLS, WorkflowClientOptions.newBuilder()
            .setDataConverter(
                    new CodecDataConverter(
                            DefaultDataConverter.newDefaultInstance(),
                            Collections.singletonList(new CustomPayloadCodec()), true))
            .build());
}
