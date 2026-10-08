# Polarion Requirements Validator Demo

Small hands-on reference project created after an interview to explore how a custom
requirements validation tool around Polarion could be structured.

**Important:** This is not claimed to be a production Polarion plug-in and it has not
been tested against the interviewer's Polarion installation. It demonstrates the
engineering concepts around requirement validation, Java-based extensions,
Apache Velocity reporting, CI integration and a REST adapter boundary.

## What it demonstrates

- Requirements represented as structured YAML
- Java validation rules
- JUnit tests
- Apache Velocity report generation
- A clean adapter boundary for Polarion REST API integration
- GitHub Actions CI
- Equivalent GitLab CI example
- A small Groovy validation script as an example of script-based customization

## Example requirement

```yaml
id: REQ-1001
title: Portfolio performance can be calculated for a reporting date
status: APPROVED
description: >
  The system shall calculate portfolio performance for a selected reporting date.
acceptanceCriteria:
  - Response contains performance in the requested currency
  - Calculation completes successfully
testIds:
  - TEST-2001
```

## Local run

Requires Java 17+ and Gradle 8+.

```bash
gradle clean test run
```

The application validates all YAML files in `requirements/` and generates:

```text
build/reports/requirements-report.html
```

## How this maps to a Polarion environment

A real implementation could replace the filesystem loader with the `PolarionClient`
adapter and retrieve Work Items through Polarion REST API. The same validation engine
could then be used in:

1. a CI pipeline;
2. a service called by Polarion;
3. a Java/Eclipse-based extension;
4. a migration or quality-gate tool.

The demo deliberately separates the *validation logic* from the *Polarion integration*
so that the engineering concept can be tested independently.

See `docs/polarion-mapping.md`.

## Interview story

> In the first interview I learned that the existing Polarion landscape is more than
> configuration and scripting: it contains Java-based tooling, Velocity and custom
> validation logic. I therefore built this small lab to understand the engineering
> pattern hands-on. It validates structured requirements, produces a Velocity-based
> report, is tested in CI, and contains an adapter boundary for Polarion REST.
> I would still need to learn the customer's concrete Polarion customizations and
> operational environment, but the underlying Java/CI/integration concepts are familiar.

## Realistic Polarion REST mapping simulation

The project now contains a realistic JSON fixture that mirrors Polarion's
documented JSON:API Work Item response shape.

Run `PolarionRequirementMapperTest` or start `PolarionMappingDemo`.

The flow is:

```text
simulated Polarion REST response
        -> PolarionRequirementMapper
        -> Requirement
        -> RequirementValidator
```

See `docs/polarion-rest-mapping.md`.

## Requirements Compliance Matrix

The project now contains a second end-to-end demo:

```text
simulated Polarion REST Work Items
        -> PolarionRequirementMapper
        -> RequirementValidator
        + test evidence
        -> ComplianceMatrixBuilder
        -> Apache Velocity
        -> compliance-matrix.html
```

Run `ComplianceMatrixDemo` as a Java Application in Eclipse.

It demonstrates three states:

- `REQ-1001` -> `COMPLIANT`
- `REQ-1002` -> `NON_COMPLIANT`
- `REQ-1003` -> `REVIEW`

The compliance rules are deliberately explicit and are **demo rules**, not a
claim of regulatory compliance.

See `docs/compliance-matrix.md`.


## Work Item Workflow Demo

Run `WorkItemWorkflowDemo` to demonstrate linked Work Items, role-based transitions, guard rules and a Velocity workflow report. See `docs/work-item-workflow.md`.
