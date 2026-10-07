package ch.kbenz.polariondemo.polarion;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PolarionRequirementMapperTest {

    @Test
    void mapsRealisticPolarionWorkItemResponse() throws Exception {
        String json;
        try (var in = getClass().getResourceAsStream(
                "/polarion/REQ-1001-response.json")) {

            if (in == null) {
                throw new IllegalStateException("Test fixture not found");
            }
            json = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }

        var mapper = new PolarionRequirementMapper();
        var requirement = mapper.fromJson(json);

        assertEquals("REQ-1001", requirement.id());
        assertEquals(
                "Portfolio performance can be calculated for a reporting date",
                requirement.title());
        assertEquals("APPROVED", requirement.status());
        assertTrue(requirement.description().contains(
                "calculate portfolio performance"));
        assertEquals(2, requirement.acceptanceCriteria().size());
        assertEquals("TEST-2001", requirement.testIds().get(0));
    }
}
