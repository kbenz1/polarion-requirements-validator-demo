package ch.kbenz.polariondemo.polarion;

import ch.kbenz.polariondemo.model.Requirement;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps a Polarion REST Work Item response (JSON:API style) into the
 * demo's internal Requirement model.
 *
 * The mapper intentionally supports only a small subset of fields:
 * id, title, status, description, acceptanceCriteria and testIds.
 *
 * acceptanceCriteria and testIds are treated as demo-specific custom
 * fields. A real customer installation can use different field IDs.
 */
public class PolarionRequirementMapper {

    private final ObjectMapper objectMapper;

    public PolarionRequirementMapper() {
        this(new ObjectMapper());
    }

    public PolarionRequirementMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Requirement fromJson(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode data = required(root, "data");
            JsonNode attributes = required(data, "attributes");

            String id = text(attributes, "id");
            if (id.isBlank()) {
                String resourceId = text(data, "id");
                int slash = resourceId.lastIndexOf('/');
                id = slash >= 0 ? resourceId.substring(slash + 1) : resourceId;
            }

            String title = text(attributes, "title");
            String status = text(attributes, "status").toUpperCase();
            String description = richTextValue(attributes.get("description"));

            List<String> acceptanceCriteria =
                    stringList(attributes.get("acceptanceCriteria"));

            List<String> testIds =
                    stringList(attributes.get("testIds"));

            return new Requirement(
                    id,
                    title,
                    status,
                    description,
                    acceptanceCriteria,
                    testIds
            );
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Could not map Polarion Work Item JSON to Requirement", e);
        }
    }

    private static JsonNode required(JsonNode parent, String fieldName) {
        JsonNode node = parent.get(fieldName);
        if (node == null || node.isNull()) {
            throw new IllegalArgumentException(
                    "Missing required JSON field: " + fieldName);
        }
        return node;
    }

    private static String text(JsonNode parent, String fieldName) {
        if (parent == null) return "";
        JsonNode node = parent.get(fieldName);
        return node == null || node.isNull() ? "" : node.asText("");
    }

    private static String richTextValue(JsonNode node) {
        if (node == null || node.isNull()) return "";
        if (node.isTextual()) return node.asText();

        JsonNode value = node.get("value");
        return value == null || value.isNull() ? "" : value.asText("");
    }

    private static List<String> stringList(JsonNode node) {
        if (node == null || node.isNull()) return List.of();

        if (node.isArray()) {
            List<String> values = new ArrayList<>();
            for (JsonNode item : node) {
                if (!item.isNull() && !item.asText().isBlank()) {
                    values.add(item.asText());
                }
            }
            return List.copyOf(values);
        }

        if (node.isTextual()) {
            return node.asText().lines()
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .toList();
        }

        return List.of();
    }
}
