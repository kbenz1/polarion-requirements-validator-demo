# Polarion Requirements Validator Demo

Kleines Hands-On Referenz-Projekt als Lab für ein Custom
Requirements Validierungstool, das auf Polarion ALM aufsetzt.



**Wichtig:** Das ist kein production-ready Polarion plugin. Es zeigt nur die Engineering-Konzepte für mögliche Eclipse Plugins z. B. bzgl. Traceability, Work Items und den Requirements Workflow.

## Was zeigt diese Demo

* Requirements als strukturierte YAML\_Dateien
* Java Validierungsregeln
* JUnit tests
* Apache Velocity Report Generierung
* Einen Adapter für Polarion REST API Integration
* GitHub Actions CI
* Ein äquivalentes GitLab CI Beispiel
* Ein kleines Groovy Validation Script als Beispiel für script-basierte Customizations

## Beispiel für ein Requirement

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

## Anleitung für Lokalen Run

Benötigt Java 17+ und Gradle 8+.

```bash
gradle clean test run
```

Die Application validiert alle YAML-Files in `requirements/` und generiert:

```text
build/reports/requirements-report.html
```

## How this maps to a Polarion environment

A real implementation could replace the filesystem loader with the `PolarionClient`
adapter and retrieve Work Items through Polarion REST API. Dieselbe Validation Engine könnte genutzt werden:

1. in einer CI Pipeline;
2. einem Service, der von Polarion aufgerufen wird;
3. einer Java/Eclipse-Extension;
4. einem Migration- oder Quality-Gate-Tool.

Die Demo trennt *Validation Logik* vom *Polarion Integration Layer*, so dass die verschiedenen Engineering\_Konzepte unabhängig getestet werden können.

Weiteres unter `docs/polarion-mapping.md`.

## Realistische Polarion REST mapping Simulation

Das Eclipse-Projekt enthält eine JSON Fixture, die Polarions JSON:API Work Item Antwort simuliert.

Starte `PolarionRequirementMapperTest` oder `PolarionMappingDemo`.

Der Flow lautet:

```text
simulated Polarion REST response
        -> PolarionRequirementMapper
        -> Requirement
        -> RequirementValidator
```

Weiteres unter `docs/polarion-rest-mapping.md`.

## Requirements Compliance Matrix

Das Projekt enthält eine Demo für eine Compliance Matrix:

```text
Simulierte Polarion REST Work Items
        -> PolarionRequirementMapper
        -> RequirementValidator
        + test evidence
        -> ComplianceMatrixBuilder
        -> Apache Velocity
        -> compliance-matrix.html
```

Starte `ComplianceMatrixDemo` als Java Application in Eclipse.

Es zeigt drei Stati:

* `REQ-1001` -> `COMPLIANT`
* `REQ-1002` -> `NON\_COMPLIANT`
* `REQ-1003` -> `REVIEW`

Die Compliance-Regeln sind **Demo Regeln**, und keine echten Compliance-Regeln,

aber sie verdeutlichen das Prinzip.

Weiteres unter `docs/compliance-matrix.md`.



## Work Item Workflow Demo

Starte `WorkItemWorkflowDemo` um verlinkte Work Items, Role-Based Transitions, Guard Rules und einen Velocity workflow Report zu sehen.

Weiteres unter `docs/work-item-workflow.md`.

