package ch.kbenz.polariondemo.compliance;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestEvidenceLoader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, TestEvidence> load(Path jsonFile) throws IOException {
        JsonNode root = objectMapper.readTree(Files.readString(jsonFile));
        Map<String, TestEvidence> result = new LinkedHashMap<>();

        for (JsonNode item : root.path("tests")) {
            String testId = item.path("testId").asText();
            TestStatus status = TestStatus.valueOf(
                    item.path("status").asText("UNKNOWN").toUpperCase());

            TestEvidence evidence = new TestEvidence(
                    testId,
                    status,
                    item.path("executionId").asText(""),
                    item.path("executedAt").asText("")
            );

            result.put(testId, evidence);
        }

        return result;
    }
}
