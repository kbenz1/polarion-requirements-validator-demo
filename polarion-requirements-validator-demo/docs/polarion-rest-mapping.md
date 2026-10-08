# Polarion REST mapping demo

This demo uses a JSON fixture that mirrors the documented Polarion REST API
JSON:API structure for a Work Item:

```text
Polarion REST response
        |
        v
PolarionRequirementMapper
        |
        v
Requirement
        |
        v
RequirementValidator
```

The standard Polarion fields used here are:

- `data.type = workitems`
- `data.id = <project>/<work-item-id>`
- `attributes.id`
- `attributes.type`
- `attributes.title`
- `attributes.status`
- `attributes.description`

The demo-specific fields `acceptanceCriteria` and `testIds` are examples of
customer-specific/custom fields. A real Polarion project can use different
field IDs, so a production mapper should make these mappings configurable.

## Run in Eclipse

1. Refresh the Gradle project.
2. Run `PolarionRequirementMapperTest` as a JUnit test.
3. Run `PolarionMappingDemo` as a Java Application.

Expected console output is similar to:

```text
Mapped Polarion Work Item:
  ID:     REQ-1001
  Title:  Portfolio performance can be calculated for a reporting date
  Status: APPROVED
  Tests:  [TEST-2001]
Validation: OK
```

## Next real integration step

Replace the fixture with:

```java
String json = polarionClient.getWorkItem("PF", "REQ-1001");
Requirement requirement = mapper.fromJson(json);
```

and provide a real Polarion base URL and Personal Access Token.

Do not commit real access tokens to Git.
