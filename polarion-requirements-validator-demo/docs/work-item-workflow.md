# Work Item Workflow Demo

This module demonstrates a small Polarion-style workflow around linked Work Items.

```text
REQ-1001 REQUIREMENT
  +-- IMPLEMENTED_BY --> TASK-3001
  +-- VERIFIED_BY ----> TEST-2001
```

Workflow:

```text
DRAFT -> IN_REVIEW -> APPROVED -> IMPLEMENTED -> VERIFIED -> CLOSED
```

Roles:
- AUTHOR submits for review
- REVIEWER approves or returns for rework
- DEVELOPER marks implementation complete
- TESTER verifies
- PRODUCT_OWNER closes

Guards:
- APPROVED requires a VERIFIED_BY link
- IMPLEMENTED requires an IMPLEMENTED_BY link

Run `WorkItemWorkflowDemo` in Eclipse. It creates:

`build/reports/work-item-workflow.html`

This is a demo model, not a claim that a customer's actual Polarion workflow is configured exactly this way.
