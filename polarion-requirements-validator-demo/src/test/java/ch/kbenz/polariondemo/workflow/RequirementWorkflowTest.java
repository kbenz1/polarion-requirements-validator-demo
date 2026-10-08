package ch.kbenz.polariondemo.workflow;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class RequirementWorkflowTest {
    @Test void completeWorkflowCanReachClosed() {
        WorkItem r=new WorkItem("REQ-1001",WorkItemType.REQUIREMENT,"Portfolio performance",WorkItemStatus.DRAFT);
        r.addLink(new WorkItemLink(r.id(),WorkItemLinkType.VERIFIED_BY,"TEST-2001"));
        r.addLink(new WorkItemLink(r.id(),WorkItemLinkType.IMPLEMENTED_BY,"TASK-3001"));
        RequirementWorkflow w=new RequirementWorkflow();
        w.transition(r,WorkItemStatus.IN_REVIEW,WorkflowRole.AUTHOR,"BA","");
        w.transition(r,WorkItemStatus.APPROVED,WorkflowRole.REVIEWER,"Architect","");
        w.transition(r,WorkItemStatus.IMPLEMENTED,WorkflowRole.DEVELOPER,"Developer","");
        w.transition(r,WorkItemStatus.VERIFIED,WorkflowRole.TESTER,"Tester","");
        w.transition(r,WorkItemStatus.CLOSED,WorkflowRole.PRODUCT_OWNER,"PO","");
        assertEquals(WorkItemStatus.CLOSED,r.status());
        assertEquals(5,w.history().size());
    }

    @Test void approvalNeedsVerificationLink() {
        WorkItem r=new WorkItem("REQ-1002",WorkItemType.REQUIREMENT,"Currency conversion",WorkItemStatus.DRAFT);
        RequirementWorkflow w=new RequirementWorkflow();
        w.transition(r,WorkItemStatus.IN_REVIEW,WorkflowRole.AUTHOR,"BA","");
        assertThrows(WorkflowException.class,
            () -> w.transition(r,WorkItemStatus.APPROVED,WorkflowRole.REVIEWER,"Architect",""));
    }
}
