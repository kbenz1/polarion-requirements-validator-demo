package ch.kbenz.polariondemo.compliance;

import java.util.List;

public record ComplianceMatrixEntry(
        String requirementId,
        String title,
        String requirementStatus,
        int acceptanceCriteriaCount,
        List<String> testIds,
        String testSummary,
        int validationErrors,
        ComplianceStatus complianceStatus,
        String rationale
) {
}
