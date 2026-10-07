package ch.kbenz.polariondemo;

import ch.kbenz.polariondemo.io.RequirementLoader;
import ch.kbenz.polariondemo.report.VelocityReportRenderer;
import ch.kbenz.polariondemo.validation.RequirementValidator;
import ch.kbenz.polariondemo.validation.ValidationIssue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        Path requirementsDir = Path.of("requirements");

        var loader = new RequirementLoader();
        var validator = new RequirementValidator();

        var requirements = loader.loadDirectory(requirementsDir);
        var issues = new ArrayList<ValidationIssue>();

        for (var requirement : requirements) {
            issues.addAll(validator.validate(requirement));
        }

        var renderer = new VelocityReportRenderer();
        String report = renderer.render(requirements, issues);

        Path output = Path.of("build/reports/requirements-report.html");
        Files.createDirectories(output.getParent());
        Files.writeString(output, report);

        System.out.printf(
                "Validated %d requirements, found %d issues.%n",
                requirements.size(), issues.size());
        System.out.println("Report: " + output.toAbsolutePath());

        if (!issues.isEmpty()) {
            System.exit(1);
        }
    }
}
