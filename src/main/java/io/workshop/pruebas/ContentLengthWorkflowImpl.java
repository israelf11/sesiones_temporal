package io.workshop.pruebas;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.workflow.*;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ContentLengthWorkflowImpl implements ContentLengthWorkflow {

    private Logger logger = Workflow.getLogger(this.getClass().getName());
    private ContentLengthInfo defaultInfo;

    private final ContentLengthActivity activities =
            Workflow.newActivityStub(
                    ContentLengthActivity.class,
                    ActivityOptions.newBuilder()
                            .setRetryOptions(RetryOptions.newBuilder()
                                    .setDoNotRetry(NullPointerException.class.getName())
                                    .build())
                            .setStartToCloseTimeout(Duration.ofSeconds(2)).build());


    @Override
    public ContentLengthInfo execute() {
        return invokeAndWaitForResult();
    }

    private ContentLengthInfo invokeAndWaitForResult() {
        return activities.count("https://temporal.io/");
    }

    private ContentLengthInfo invokeAsyncAndWaitForResult() {
        Promise<ContentLengthInfo> activityPromise = Async.function(activities::count, "https://temporal.io/");

        return activityPromise.get();
    }

    private ContentLengthInfo invokeParallelWaitForAll() {
        ContentLengthInfo info = new ContentLengthInfo();

        List<Promise<ContentLengthInfo>> promiseList = new ArrayList<>();
        promiseList.add(Async.function(activities::count, "https://temporal.io/"));
        promiseList.add(Async.function(activities::count, "https://www.google.com/"));

        Promise.allOf(promiseList).get();

        for (Promise<ContentLengthInfo> promise : promiseList) {
            if (promise.getFailure() == null) {
                info.add(promise.get());
            } else {
                throw Activity.wrap(promise.getFailure());
            }
        }

        return info;
    }

    private ContentLengthInfo invokeParallelWaitForFirst() {
        List<Promise<ContentLengthInfo>> promiseList = new ArrayList<>();
        promiseList.add(Async.function(activities::count, "https://temporal.io/"));
        promiseList.add(Async.function(activities::count, "https://www.google.com/"));

        return Promise.anyOf(promiseList).get();
    }

    private ContentLengthInfo invokeParallelBranches() {
        ContentLengthInfo info = new ContentLengthInfo();

        List<Promise<ContentLengthInfo>> promiseList = new ArrayList<>();
        promiseList.add(Async.function(this::branch, 3, "https://temporal.io/"));
        promiseList.add(Async.function(this::branch, 1, "https://www.google.com/"));
        promiseList.add(Async.function(this::branch, 2, "https://www.espn.com/"));

        Promise.allOf(promiseList).get();

        for (Promise<ContentLengthInfo> promise : promiseList) {
            if (promise.getFailure() == null) {
                info.add(promise.get());
            } else {
                throw Activity.wrap(promise.getFailure());
            }
        }

        return info;
    }

    private ContentLengthInfo branch(int seconds, String url) {
        Workflow.sleep(Duration.ofSeconds(seconds));
        return activities.count(url);
    }

    private ContentLengthInfo invokeWithRetries() {
        return activities.count("DOESNOTEXIST");
    }

    private ContentLengthInfo invokeNoRetriesHandleError() {
        try {
            return activities.count("DOESNOTEXIST");
        } catch (ActivityFailure failure) {
            logger.warn("Error: " + failure.getCause().getMessage());
            return new ContentLengthInfo();
        }
    }

    private ContentLengthInfo invokeNoRetryHandleErrorWithSaga() {
        Saga saga = new Saga(new Saga.Options.Builder().setParallelCompensation(false).build());
        saga.addCompensation(this::getDefaultCount);
        saga.addCompensation(
                () -> logger.info("Performing first step of compensation!"));

        try {
            return activities.count("DOESNOTEXIST");
        } catch (ActivityFailure failure) {
            logger.warn("Error: " + failure.getCause().getMessage());

            saga.compensate();
            return defaultInfo;
        }
    }

    private void getDefaultCount() {
        defaultInfo = activities.count("https://temporal.io/");
    }
}
