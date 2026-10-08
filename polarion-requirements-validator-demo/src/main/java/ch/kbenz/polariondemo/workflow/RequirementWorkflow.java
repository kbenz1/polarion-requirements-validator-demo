package ch.kbenz.polariondemo.workflow;
import java.time.Instant;
import java.util.*;
public class RequirementWorkflow {
    private final List<WorkflowTransition> transitions = List.of(
        new WorkflowTransition(WorkItemStatus.DRAFT, WorkItemStatus.IN_REVIEW, Set.of(WorkflowRole.AUTHOR), "Submit for review"),
        new WorkflowTransition(WorkItemStatus.IN_REVIEW, WorkItemStatus.DRAFT, Set.of(WorkflowRole.REVIEWER), "Return for rework"),
        new WorkflowTransition(WorkItemStatus.IN_REVIEW, WorkItemStatus.APPROVED, Set.of(WorkflowRole.REVIEWER), "Approve requirement"),
        new WorkflowTransition(WorkItemStatus.APPROVED, WorkItemStatus.IMPLEMENTED, Set.of(WorkflowRole.DEVELOPER), "Implementation complete"),
        new WorkflowTransition(WorkItemStatus.IMPLEMENTED, WorkItemStatus.VERIFIED, Set.of(WorkflowRole.TESTER), "Verification complete"),
        new WorkflowTransition(WorkItemStatus.VERIFIED, WorkItemStatus.CLOSED, Set.of(WorkflowRole.PRODUCT_OWNER), "Close requirement")
    );
    private final List<WorkflowEvent> history = new ArrayList<>();

    public void transition(WorkItem item, WorkItemStatus target, WorkflowRole role, String actor, String note) {
        WorkflowTransition t = transitions.stream()
            .filter(x -> x.from()==item.status() && x.to()==target)
            .findFirst()
            .orElseThrow(() -> new WorkflowException("No transition from "+item.status()+" to "+target));
        if (!t.allowedRoles().contains(role))
            throw new WorkflowException("Role "+role+" is not allowed for "+t.from()+" -> "+t.to());
        if (target==WorkItemStatus.APPROVED &&
            item.links().stream().noneMatch(l -> l.type()==WorkItemLinkType.VERIFIED_BY))
            throw new WorkflowException("Requirement cannot be APPROVED without a VERIFIED_BY link.");
        if (target==WorkItemStatus.IMPLEMENTED &&
            item.links().stream().noneMatch(l -> l.type()==WorkItemLinkType.IMPLEMENTED_BY))
            throw new WorkflowException("Requirement cannot be IMPLEMENTED without an IMPLEMENTED_BY link.");

        WorkItemStatus before=item.status();
        item.changeStatus(target);
        history.add(new WorkflowEvent(item.id(), before, target, role, actor, Instant.now(), note==null?"":note));
    }

    public List<WorkflowTransition> transitions(){return transitions;}
    public List<WorkflowEvent> history(){return List.copyOf(history);}
}
