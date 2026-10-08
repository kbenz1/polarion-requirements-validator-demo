package ch.kbenz.polariondemo.compliance;

import ch.kbenz.polariondemo.model.Requirement;
import ch.kbenz.polariondemo.validation.ValidationIssue;

import java.util.List;
import java.util.Map;

public class ComplianceMatrixBuilder {

    public ComplianceMatrixEntry build(
            Requirement requirement,
            List<ValidationIssue> issues,
            Map<String, TestEvidence> evidenceByTestId) {

        int errorCount = (int) issues.stream()
                .filter(i -> requirement.id().equals(i.requirementId()))
                .filter(i -> "ERROR".equalsIgnoreCase(i.severity()))
                .count();

        TestStatus aggregateTestStatus =
                aggregateTestStatus(requirement.testIds(), evidenceByTestId);

        ComplianceDecision decision =
                determineCompliance(requirement, errorCount, aggregateTestStatus);

        return new ComplianceMatrixEntry(
                requirement.id(),
                requirement.title(),
                requirement.status(),
                requirement.acceptanceCriteria() == null
                        ? 0
                        : requirement.acceptanceCriteria().size(),
                requirement.testIds() == null
                        ? List.of()
                        : List.copyOf(requirement.testIds()),
                aggregateTestStatus.name(),
                errorCount,
                decision.status(),
                decision.rationale()
        );
    }

    private TestStatus aggregateTestStatus(
            List<String> testIds,
            Map<String, TestEvidence> evidenceByTestId) {

        if (testIds == null || testIds.isEmpty()) {
            return TestStatus.UNKNOWN;
        }

        boolean anyNotRun = false;
        boolean anyUnknown = false;

        for (String testId : testIds) {
            TestEvidence evidence = evidenceByTestId.get(testId);

            if (evidence == null) {
                anyUnknown = true;
                continue;
            }

            if (evidence.status() == TestStatus.FAILED) {
                return TestStatus.FAILED;
            }

            if (evidence.status() == TestStatus.NOT_RUN) {
                anyNotRun = true;
            }

            if (evidence.status() == TestStatus.UNKNOWN) {
                anyUnknown = true;
            }
        }

        if (anyNotRun) {
            return TestStatus.NOT_RUN;
        }

        if (anyUnknown) {
            return TestStatus.UNKNOWN;
        }

        return TestStatus.PASSED;
    }

    private ComplianceDecision determineCompliance(
            Requirement requirement,
            int errorCount,
            TestStatus testStatus) {

        if (errorCount > 0) {
            return new ComplianceDecision(
                    ComplianceStatus.NON_COMPLIANT,
                    "Requirement validation contains errors.");
        }

        if (!"APPROVED".equalsIgnoreCase(requirement.status())) {
            return new ComplianceDecision(
                    ComplianceStatus.REVIEW,
                    "Requirement is not yet APPROVED.");
        }

        if (requirement.acceptanceCriteria() == null
                || requirement.acceptanceCriteria().isEmpty()) {
            return new ComplianceDecision(
                    ComplianceStatus.NON_COMPLIANT,
                    "No acceptance criteria are defined.");
        }

        if (requirement.testIds() == null || requirement.testIds().isEmpty()) {
            return new ComplianceDecision(
                    ComplianceStatus.NON_COMPLIANT,
                    "No verification test is linked.");
        }

        if (testStatus == TestStatus.FAILED) {
            return new ComplianceDecision(
                    ComplianceStatus.NON_COMPLIANT,
                    "At least one linked test failed.");
        }

        if (testStatus != TestStatus.PASSED) {
            return new ComplianceDecision(
                    ComplianceStatus.REVIEW,
                    "Test evidence is incomplete or not executed.");
        }

        return new ComplianceDecision(
                ComplianceStatus.COMPLIANT,
                "Approved, valid, traceable and all linked tests passed.");
    }

    private record ComplianceDecision(
            ComplianceStatus status,
            String rationale) {
    }
}
