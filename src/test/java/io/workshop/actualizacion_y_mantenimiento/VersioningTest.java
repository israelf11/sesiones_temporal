package io.workshop.actualizacion_y_mantenimiento;

import io.temporal.testing.WorkflowReplayer;
import org.junit.Test;

public class VersioningTest {
    @Test
    public void replayFromHistory() throws Exception {
        WorkflowReplayer.replayWorkflowExecutionFromResource(
                "history_for_original_execution.json", LoanProcessingWorkflowImpl.class);
    }
}
