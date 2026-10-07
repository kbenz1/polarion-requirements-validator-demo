package ch.kbenz.polariondemo.validation;

public record ValidationIssue(String requirementId, String severity, String message) {
}
