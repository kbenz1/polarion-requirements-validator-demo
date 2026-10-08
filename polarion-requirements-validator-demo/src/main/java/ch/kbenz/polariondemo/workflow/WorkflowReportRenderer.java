package ch.kbenz.polariondemo.workflow;
import org.apache.velocity.*;
import org.apache.velocity.app.VelocityEngine;
import java.io.StringWriter;
import java.util.*;
public class WorkflowReportRenderer {
    private final VelocityEngine engine;
    public WorkflowReportRenderer() {
        Properties p=new Properties();
        p.setProperty("resource.loaders","class");
        p.setProperty("resource.loader.class.class",
            "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        engine=new VelocityEngine(p); engine.init();
    }
    public String render(List<WorkItem> items, List<WorkflowEvent> history, List<WorkflowTransition> transitions){
        VelocityContext c=new VelocityContext();
        c.put("workItems",items); c.put("history",history); c.put("transitions",transitions);
        StringWriter w=new StringWriter();
        engine.getTemplate("templates/workflow-report.vm","UTF-8").merge(c,w);
        return w.toString();
    }
}
