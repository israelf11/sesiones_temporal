package io.workshop.monitoreo;

import com.sun.net.httpserver.HttpServer;
import com.uber.m3.tally.RootScopeBuilder;
import com.uber.m3.tally.Scope;
import com.uber.m3.util.ImmutableMap;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.common.reporter.MicrometerClientStatsReporter;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.workshop.monitoreo.workflow.FailWorkflow;
import io.workshop.monitoreo.workflow.FillWorkflow;
import io.workshop.monitoreo.workflow.MetricsWorkflow;

public class MetricsStarter {
    public static void main(String[] args) {
        System.out.println("Starter metrics are available at http://localhost:8078/metrics");

        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        Scope scope =
                new RootScopeBuilder()
                        .tags(
                                ImmutableMap.of(
                                        "starterCustomTag1",
                                        "starterCustomTag1Value",
                                        "starterCustomTag2",
                                        "starterCustomTag2Value"))
                        .reporter(new MicrometerClientStatsReporter(registry))
                        .reportEvery(com.uber.m3.util.Duration.ofSeconds(1));
        HttpServer scrapeEndpoint = MetricsUtils.startPrometheusScrapeEndpoint(registry, 8078);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> scrapeEndpoint.stop(1)));

        WorkflowServiceStubsOptions stubOptions =
                WorkflowServiceStubsOptions.newBuilder().setMetricsScope(scope).build();

        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance(stubOptions);
        WorkflowClient client = WorkflowClient.newInstance(service);

        WorkflowOptions workflowOptions =
                WorkflowOptions.newBuilder()
                        .setWorkflowId("metricsWorkflow")
                        .setTaskQueue(MetricsWorker.DEFAULT_TASK_QUEUE_NAME)
                        .build();
        MetricsWorkflow workflow = client.newWorkflowStub(MetricsWorkflow.class, workflowOptions);

        String result = workflow.exec("hello metrics");

        MetricsWorkflow workflow2 = client.newWorkflowStub(MetricsWorkflow.class, WorkflowOptions.newBuilder()
                .setWorkflowId("metricsWorkflowFail")
                .setTaskQueue(MetricsWorker.DEFAULT_TASK_QUEUE_NAME)
                .build());

        WorkflowClient.start(workflow2::exec, "hello metrics");
        WorkflowStub untyped = WorkflowStub.fromTyped(workflow2);
        untyped.cancel();

        for (int i = 0; i < 50; i++) {
            FillWorkflow fillWorkflow = client.newWorkflowStub(FillWorkflow.class, WorkflowOptions.newBuilder()
                    .setWorkflowId("Fill-" + i)
                    .setTaskQueue(MetricsWorker.DEFAULT_TASK_QUEUE_NAME)
                    .build());
            WorkflowClient.start(fillWorkflow::exec, "Filler-" + 1);
            sleep(500);
        }

        for (int i = 0; i < 50; i++) {
            FailWorkflow fillWorkflow = client.newWorkflowStub(FailWorkflow.class, WorkflowOptions.newBuilder()
                    .setWorkflowId("Fail-" + i)
                    .setTaskQueue(MetricsWorker.DEFAULT_TASK_QUEUE_NAME)
                    .build());
            WorkflowClient.start(fillWorkflow::exec, "Fail-" + 1);
            sleep(500);
        }
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
