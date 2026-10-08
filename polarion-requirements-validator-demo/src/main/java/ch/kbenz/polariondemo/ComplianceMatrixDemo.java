package ch.kbenz.polariondemo;

import ch.kbenz.polariondemo.compliance.ComplianceMatrixBuilder;
import ch.kbenz.polariondemo.compliance.ComplianceMatrixEntry;
import ch.kbenz.polariondemo.compliance.ComplianceMatrixRenderer;
import ch.kbenz.polariondemo.compliance.TestEvidenceLoader;
import ch.kbenz.polariondemo.polarion.PolarionRequirementMapper;
import ch.kbenz.polariondemo.validation.RequirementValidator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ComplianceMatrixDemo {

    public static void main(String[] args) throws Exception {
        Path fixtureDir = Path.of("src/test/resources/polarion");

        var mapper = new PolarionRequirementMapper();
        var validator = new RequirementValidator();
        var matrixBuilder = new ComplianceMatrixBuilder();

        var evidence = new TestEvidenceLoader().load(
                fixtureDir.resolve("test-results.json"));

        List<ComplianceMatrixEntry> matrix = new ArrayList<>();

        for (String fileName : List.of(
                "REQ-1001-response.json",
                "REQ-1002-response.json",
                "REQ-1003-response.json")) {

            String json = Files.readString(fixtureDir.resolve(fileName));
            var requirement = mapper.fromJson(json);
            var validationIssues = validator.validate(requirement);

            matrix.add(matrixBuilder.build(
                    requirement,
                    validationIssues,
                    evidence));
        }

        String html = new ComplianceMatrixRenderer().render(matrix);

        Path output = Path.of(
                "build/reports/compliance-matrix.html");

        Files.createDirectories(output.getParent());
        Files.writeString(output, html);

        System.out.println("Requirements Compliance Matrix");
        System.out.println("---------------------------------------------");

        for (var entry : matrix) {
            System.out.printf(
                    "%s | %-15s | %-13s | %s%n",
                    entry.requirementId(),
                    entry.testSummary(),
                    entry.complianceStatus(),
                    entry.rationale());
        }

        System.out.println();
        System.out.println("Report: " + output.toAbsolutePath());
    }
}
