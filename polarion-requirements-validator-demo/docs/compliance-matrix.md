# Requirements Compliance Matrix

The demo now distinguishes two concepts:

## Traceability

Traceability answers:

> Which test evidence belongs to which requirement?

Example:

```text
REQ-1001
    |
    +--> TEST-2001
             |
             +--> TR-2026-4711
                      |
                      +--> PASSED
```

## Compliance decision

The demo's compliance rule set is intentionally small and transparent.

A requirement is `COMPLIANT` when:

1. it passes requirement validation;
2. it is `APPROVED`;
3. it has at least one acceptance criterion;
4. it has at least one linked verification test;
5. all linked test evidence is `PASSED`.

A requirement is `NON_COMPLIANT` when:

- requirement validation contains errors;
- no verification test is linked; or
- at least one linked test failed.

A requirement is `REVIEW` when:

- it is not yet approved; or
- test evidence is not yet complete.

**This is demo compliance logic, not a claim of regulatory compliance.**

## Run in Eclipse

Run:

```text
ComplianceMatrixDemo
```

as a Java Application.

The program reads simulated Polarion REST Work Item responses and simulated
test evidence, then creates:

```text
build/reports/compliance-matrix.html
```

Expected matrix:

| Requirement | Test Result | Compliance |
|---|---|---|
| REQ-1001 | PASSED | COMPLIANT |
| REQ-1002 | UNKNOWN | NON_COMPLIANT |
| REQ-1003 | NOT_RUN | REVIEW |

## Architecture

```text
Simulated Polarion REST
        |
        v
PolarionRequirementMapper
        |
        v
Requirement
        |
        +--------------------+
        |                    |
        v                    v
RequirementValidator    Test Evidence
        |                    |
        +---------+----------+
                  |
                  v
       ComplianceMatrixBuilder
                  |
                  v
       ComplianceMatrixEntry
                  |
                  v
          Apache Velocity
                  |
                  v
     compliance-matrix.html
```

A real integration can later replace the JSON fixtures with the existing
`PolarionRestClient`.
