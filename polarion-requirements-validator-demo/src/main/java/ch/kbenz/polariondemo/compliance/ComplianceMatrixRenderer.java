package ch.kbenz.polariondemo.compliance;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

public class ComplianceMatrixRenderer {

    private final VelocityEngine engine;

    public ComplianceMatrixRenderer() {
        Properties properties = new Properties();
        properties.setProperty("resource.loaders", "class");
        properties.setProperty(
                "resource.loader.class.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        engine = new VelocityEngine(properties);
        engine.init();
    }

    public String render(List<ComplianceMatrixEntry> entries) {
        VelocityContext context = new VelocityContext();
        context.put("entries", entries);

        StringWriter writer = new StringWriter();
        engine.getTemplate(
                "templates/compliance-matrix.vm",
                "UTF-8").merge(context, writer);

        return writer.toString();
    }
}
