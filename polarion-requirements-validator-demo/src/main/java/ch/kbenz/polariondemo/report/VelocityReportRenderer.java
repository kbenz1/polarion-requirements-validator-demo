package ch.kbenz.polariondemo.report;

import ch.kbenz.polariondemo.model.Requirement;
import ch.kbenz.polariondemo.validation.ValidationIssue;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

public class VelocityReportRenderer {

    private final VelocityEngine engine;

    public VelocityReportRenderer() {
        Properties properties = new Properties();
        properties.setProperty("resource.loaders", "class");
        properties.setProperty(
                "resource.loader.class.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        engine = new VelocityEngine(properties);
        engine.init();
    }

    public String render(List<Requirement> requirements, List<ValidationIssue> issues) {
        var context = new VelocityContext();
        context.put("requirements", requirements);
        context.put("issues", issues);

        var writer = new StringWriter();
        engine.getTemplate("templates/report.vm", "UTF-8").merge(context, writer);
        return writer.toString();
    }
}
