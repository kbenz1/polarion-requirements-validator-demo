package ch.kbenz.polariondemo.compliance;

import ch.kbenz.polariondemo.model.Requirement;
import ch.kbenz.polariondemo.validation.RequirementValidator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComplianceMatrixBuilderTest {

    private final ComplianceMatrixBuilder builder =
            new ComplianceMatrixBuilder();

    private final RequirementValidator validator =
            new RequirementValidator();

    @Test
    void approvedValidRequirementWithPassedTestIsCompliant() {
        var requirement = new Requirement(
                "REQ-1001",
                "Portfolio performance",
                "APPROVED",
                "Calculate portfolio performance.",
                List.of("Result is returned."),
                List.of("TEST-2001"));

        var evidence = Map.of(
                "TEST-2001",
                new TestEvidence(
                        "TEST-2001",
                        TestStatus.PASSED,
                        "TR-1",
                        "2026-10-07T18:15:00Z"));

        var entry = builder.build(
                requirement,
                validator.validate(requirement),
                evidence);

        assertEquals(
                ComplianceStatus.COMPLIANT,
                entry.complianceStatus());
    }

    @Test
    void approvedRequirementWithoutTestIsNonCompliant() {
        var requirement = new Requirement(
                "REQ-1002",
                "Currency conversion",
                "APPROVED",
                "Retain evidence.",
                List.of("Rate is traceable."),
                List.of());

        var entry = builder.build(
                requirement,
                validator.validate(requirement),
                Map.of());

        assertEquals(
                ComplianceStatus.NON_COMPLIANT,
                entry.complianceStatus());
    }

    @Test
    void requirementInReviewIsReview() {
        var requirement = new Requirement(
                "REQ-1003",
                "Monthly report",
                "REVIEW",
                "Generate report.",
                List.of("PDF is generated."),
                List.of("TEST-2003"));

        var evidence = Map.of(
                "TEST-2003",
                new TestEvidence(
                        "TEST-2003",
                        TestStatus.NOT_RUN,
                        "",
                        ""));

        var entry = builder.build(
                requirement,
                validator.validate(requirement),
                evidence);

        assertEquals(
                ComplianceStatus.REVIEW,
                entry.complianceStatus());
    }
}
