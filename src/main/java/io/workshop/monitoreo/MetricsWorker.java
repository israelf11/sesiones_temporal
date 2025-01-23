package io.workshop.monitoreo;

import com.sun.net.httpserver.HttpServer;
import com.uber.m3.tally.RootScopeBuilder;
import com.uber.m3.tally.Scope;
import com.uber.m3.util.ImmutableMap;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.temporal.client.WorkflowClient;
import io.temporal.common.reporter.MicrometerClientStatsReporter;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.worker.WorkflowImplementationOptions;
import io.workshop.monitoreo.activities.MetricsActivitiesImpl;
import io.workshop.monitoreo.workflow.FailWorkflowImpl;
import io.workshop.monitoreo.workflow.FillWorkflowImpl;
import io.workshop.monitoreo.workflow.MetricsWorkflowImpl;

public class MetricsWorker {
    public static final String DEFAULT_TASK_QUEUE_NAME = "metricsqueue";

    public static void main(String[] args) {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        Scope scope =
                new RootScopeBuilder()
                        .tags(
                                ImmutableMap.of(
                                        "workerCustomTag1",
                                        "workerCustomTag1Value",
                                        "workerCustomTag2",
                                        "workerCustomTag2Value"))
                        .reporter(new MicrometerClientStatsReporter(registry))
                        .reportEvery(com.uber.m3.util.Duration.ofSeconds(1));
        HttpServer scrapeEndpoint = MetricsUtils.startPrometheusScrapeEndpoint(registry, 8077);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> scrapeEndpoint.stop(1)));
        WorkflowServiceStubsOptions stubOptions =
                WorkflowServiceStubsOptions.newBuilder().setMetricsScope(scope).build();

        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance(stubOptions);
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);

        Worker worker = factory.newWorker(DEFAULT_TASK_QUEUE_NAME);

        WorkflowImplementationOptions options = WorkflowImplementationOptions.newBuilder()
                .setFailWorkflowExceptionTypes(IllegalArgumentException.class)
                .build();

        worker.registerWorkflowImplementationTypes(MetricsWorkflowImpl.class);
        worker.registerWorkflowImplementationTypes(options, FillWorkflowImpl.class, FailWorkflowImpl.class);
        worker.registerActivitiesImplementations(new MetricsActivitiesImpl());

        factory.start();

        System.out.println("Workers metrics are available at http://localhost:8077/metrics");
    }
}
