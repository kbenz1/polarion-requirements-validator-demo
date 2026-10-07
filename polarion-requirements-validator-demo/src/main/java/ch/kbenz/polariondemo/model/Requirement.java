package ch.kbenz.polariondemo.model;

import java.util.List;

public record Requirement(
        String id,
        String title,
        String status,
        String description,
        List<String> acceptanceCriteria,
        List<String> testIds) {
}
