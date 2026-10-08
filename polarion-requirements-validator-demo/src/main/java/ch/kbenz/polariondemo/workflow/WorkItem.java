package ch.kbenz.polariondemo.workflow;
import java.util.*;
public class WorkItem {
    private final String id;
    private final WorkItemType type;
    private final String title;
    private WorkItemStatus status;
    private final List<WorkItemLink> links = new ArrayList<>();
    public WorkItem(String id, WorkItemType type, String title, WorkItemStatus status) {
        this.id=id; this.type=type; this.title=title; this.status=status;
    }
    public String id(){return id;}
    public WorkItemType type(){return type;}
    public String title(){return title;}
    public WorkItemStatus status(){return status;}
    public void changeStatus(WorkItemStatus s){status=s;}
    public void addLink(WorkItemLink l){links.add(l);}
    public List<WorkItemLink> links(){return Collections.unmodifiableList(links);}
}
