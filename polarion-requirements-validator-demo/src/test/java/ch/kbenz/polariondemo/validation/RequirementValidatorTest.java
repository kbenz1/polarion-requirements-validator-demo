package ch.kbenz.polariondemo.validation;

import ch.kbenz.polariondemo.model.Requirement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequirementValidatorTest {

    private final RequirementValidator validator = new RequirementValidator();

    @Test
    void approvedRequirementWithTestReferenceIsValid() {
        var requirement = new Requirement(
                "REQ-1001",
                "Portfolio performance",
                "APPROVED",
                "Calculate portfolio performance.",
                List.of("Result is returned in requested currency."),
                List.of("TEST-2001"));

        assertTrue(validator.validate(requirement).isEmpty());
    }

    @Test
    void approvedRequirementWithoutTestReferenceIsRejected() {
        var requirement = new Requirement(
                "REQ-1002",
                "Portfolio performance",
                "APPROVED",
                "Calculate portfolio performance.",
                List.of("Result is returned."),
                List.of());

        assertTrue(
                validator.validate(requirement).stream()
                        .anyMatch(i -> i.message().contains("at least one test")));
    }
}
