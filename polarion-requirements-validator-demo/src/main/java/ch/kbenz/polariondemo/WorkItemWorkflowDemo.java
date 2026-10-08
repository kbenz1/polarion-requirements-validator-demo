package ch.kbenz.polariondemo;
import ch.kbenz.polariondemo.workflow.*;
import java.nio.file.*;
import java.util.*;
public class WorkItemWorkflowDemo {
    public static void main(String[] args) throws Exception {
        WorkItem req=new WorkItem("REQ-1001",WorkItemType.REQUIREMENT,
            "Portfolio performance can be calculated for a reporting date",WorkItemStatus.DRAFT);
        WorkItem task=new WorkItem("TASK-3001",WorkItemType.TASK,
            "Implement portfolio performance endpoint",WorkItemStatus.CLOSED);
        WorkItem test=new WorkItem("TEST-2001",WorkItemType.TEST_CASE,
            "Verify portfolio performance for reporting date",WorkItemStatus.CLOSED);

        req.addLink(new WorkItemLink(req.id(),WorkItemLinkType.IMPLEMENTED_BY,task.id()));
        req.addLink(new WorkItemLink(req.id(),WorkItemLinkType.VERIFIED_BY,test.id()));

        RequirementWorkflow wf=new RequirementWorkflow();
        wf.transition(req,WorkItemStatus.IN_REVIEW,WorkflowRole.AUTHOR,"Business Analyst","Ready for review");
        wf.transition(req,WorkItemStatus.APPROVED,WorkflowRole.REVIEWER,"Solution Architect","Approved");
        wf.transition(req,WorkItemStatus.IMPLEMENTED,WorkflowRole.DEVELOPER,"Java Developer","TASK-3001 completed");
        wf.transition(req,WorkItemStatus.VERIFIED,WorkflowRole.TESTER,"Test Engineer","TEST-2001 passed");
        wf.transition(req,WorkItemStatus.CLOSED,WorkflowRole.PRODUCT_OWNER,"Product Owner","Closed");

        String html=new WorkflowReportRenderer().render(List.of(req,task,test),wf.history(),wf.transitions());
        Path out=Path.of("build/reports/work-item-workflow.html");
        Files.createDirectories(out.getParent()); Files.writeString(out,html);

        System.out.println(req.id()+" final status: "+req.status());
        wf.history().forEach(e -> System.out.println(e.from()+" -> "+e.to()+" by "+e.role()));
        System.out.println("Report: "+out.toAbsolutePath());
    }
}
