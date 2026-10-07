package ch.kbenz.polariondemo.validation;

import ch.kbenz.polariondemo.model.Requirement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class RequirementValidator {

    private static final Pattern ID_PATTERN = Pattern.compile("REQ-\\d{4,}");
    private static final Set<String> ALLOWED_STATUS =
            Set.of("DRAFT", "REVIEW", "APPROVED", "IMPLEMENTED", "VERIFIED");

    public List<ValidationIssue> validate(Requirement requirement) {
        var issues = new ArrayList<ValidationIssue>();
        String id = blank(requirement.id()) ? "<unknown>" : requirement.id();

        if (blank(requirement.id()) || !ID_PATTERN.matcher(requirement.id()).matches()) {
            issues.add(new ValidationIssue(id, "ERROR",
                    "Requirement id must match REQ-####."));
        }

        if (blank(requirement.title())) {
            issues.add(new ValidationIssue(id, "ERROR",
                    "Title must not be empty."));
        }

        if (blank(requirement.status()) || !ALLOWED_STATUS.contains(requirement.status())) {
            issues.add(new ValidationIssue(id, "ERROR",
                    "Status must be one of " + ALLOWED_STATUS + "."));
        }

        if (requirement.acceptanceCriteria() == null || requirement.acceptanceCriteria().isEmpty()) {
            issues.add(new ValidationIssue(id, "ERROR",
                    "At least one acceptance criterion is required."));
        }

        if ("APPROVED".equals(requirement.status())
                && (requirement.testIds() == null || requirement.testIds().isEmpty())) {
            issues.add(new ValidationIssue(id, "ERROR",
                    "An APPROVED requirement must reference at least one test."));
        }

        return issues;
    }

    private static boolean blank(String value) {
        return value == null || value.isBlank();
    }
}
