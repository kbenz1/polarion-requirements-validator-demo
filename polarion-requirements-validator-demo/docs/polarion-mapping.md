# Mapping the demo to Polarion concepts

This document distinguishes between what is implemented in the demo and what would
belong to a real Polarion integration.

| Demo concept | Possible Polarion equivalent |
|---|---|
| YAML Requirement | Polarion Work Item |
| `RequirementValidator` | Java-based validation/custom rule |
| Velocity HTML report | Velocity-based generated representation/report |
| GitHub/GitLab CI quality gate | Pipeline validation around requirements |
| `PolarionRestClient` | REST v1 integration boundary |
| JUnit tests | Automated tests for validation logic |

## Real Polarion REST integration

Polarion exposes a versioned REST API under:

```text
https://<polarion-server>/polarion/rest/v1
```

A Work Item can be addressed using a project and Work Item identifier.

The demo contains a `PolarionRestClient` showing the boundary, but intentionally
does not assume the customer's field schema, authentication setup, custom Work Item
types, custom workflows or plug-in architecture.

## Why the boundary matters

A heavily customized Polarion installation may contain years of:

- custom Work Item types and fields;
- workflow rules;
- templates and reports;
- Java extensions;
- validation logic;
- integrations;
- access-control rules.

Therefore a migration to Requirements-as-Code should normally start by inventorying
the semantics and customizations rather than merely converting documents to Markdown.

## Possible next step

Implement a mapping layer:

```text
Polarion Work Item
        |
        v
PolarionRestClient
        |
        v
RequirementMapper
        |
        v
RequirementValidator
        |
        +--> validation report
        +--> CI quality gate
        +--> migration feedback
```

That would allow the same rules to be applied to both existing Polarion Work Items
and future Requirements-as-Code artifacts.
