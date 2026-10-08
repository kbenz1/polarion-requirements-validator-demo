package ch.kbenz.polariondemo.workflow;
import java.time.Instant;
public record WorkflowEvent(String workItemId, WorkItemStatus from, WorkItemStatus to,
        WorkflowRole role, String actor, Instant timestamp, String note) {}
