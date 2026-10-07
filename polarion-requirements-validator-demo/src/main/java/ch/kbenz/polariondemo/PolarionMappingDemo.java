package ch.kbenz.polariondemo;

import ch.kbenz.polariondemo.polarion.PolarionRequirementMapper;
import ch.kbenz.polariondemo.validation.RequirementValidator;

import java.nio.file.Files;
import java.nio.file.Path;

public class PolarionMappingDemo {

    public static void main(String[] args) throws Exception {
        Path responseFile = Path.of(
                "src/test/resources/polarion/REQ-1001-response.json");

        String polarionJson = Files.readString(responseFile);

        var mapper = new PolarionRequirementMapper();
        var requirement = mapper.fromJson(polarionJson);

        System.out.println("Mapped Polarion Work Item:");
        System.out.println("  ID:     " + requirement.id());
        System.out.println("  Title:  " + requirement.title());
        System.out.println("  Status: " + requirement.status());
        System.out.println("  Tests:  " + requirement.testIds());

        var issues = new RequirementValidator().validate(requirement);

        if (issues.isEmpty()) {
            System.out.println("Validation: OK");
        } else {
            System.out.println("Validation issues:");
            issues.forEach(issue ->
                    System.out.println("  " + issue.severity()
                            + ": " + issue.message()));
        }
    }
}
