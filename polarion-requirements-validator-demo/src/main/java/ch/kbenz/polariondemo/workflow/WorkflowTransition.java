package ch.kbenz.polariondemo.workflow;
import java.util.Set;
public record WorkflowTransition(WorkItemStatus from, WorkItemStatus to,
        Set<WorkflowRole> allowedRoles, String description) {}
