package ch.kbenz.polariondemo.workflow;
public record WorkItemLink(String sourceId, WorkItemLinkType type, String targetId) {}
