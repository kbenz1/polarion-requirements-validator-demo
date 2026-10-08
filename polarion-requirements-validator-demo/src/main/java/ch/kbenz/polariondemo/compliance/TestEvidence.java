package ch.kbenz.polariondemo.compliance;

public record TestEvidence(
        String testId,
        TestStatus status,
        String executionId,
        String executedAt
) {
}
