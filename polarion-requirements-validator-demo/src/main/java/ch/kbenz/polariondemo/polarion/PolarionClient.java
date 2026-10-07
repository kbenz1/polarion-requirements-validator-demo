package ch.kbenz.polariondemo.polarion;

/**
 * Integration boundary for a real Polarion connection.
 *
 * The demo intentionally keeps this separate from validation logic.
 * A production adapter could use Polarion REST v1 and map Work Items
 * into the local Requirement model.
 */
public interface PolarionClient {
    String getWorkItem(String projectId, String workItemId) throws Exception;
}
