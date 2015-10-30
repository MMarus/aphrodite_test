package jar;

import org.jboss.set.aphrodite.Aphrodite;
import org.jboss.set.aphrodite.domain.*;

import java.net.URL;
import java.util.List;

public class JSONConfiguration {
    public static void main(String[] args) throws Exception {
        System.setProperty("aphrodite.config", "aphrodite.properties.json.example");
        Aphrodite aphrodite = Aphrodite.instance();

        // Search Issues
        SearchCriteria sc = new SearchCriteria.Builder()
                .setStatus(IssueStatus.MODIFIED)
                .setProduct("JBoss Enterprise Application Platform 6")
                .build();
        List<Issue> result = aphrodite.searchIssues(sc);
        System.out.println(result);

        // Get individual Issue
        Issue issue = aphrodite.getIssue(new URL("https://issues.stage.jboss.org/browse/WFLY-100"));

        // Update issue
        issue.setAssignee("ryanemerson");
        aphrodite.updateIssue(issue);

        // Get individual Patch
        Patch patch = aphrodite.getPatch(new URL("https://github.com/ryanemerson/aphrodite_test/pull/1"));

        // Get code repository
        Repository repository = aphrodite.getRepository(new URL("https://github.com/ryanemerson/aphrodite_test"));

        // Get all patches associated with a given issue
        List<Patch> patches = aphrodite.getPatchesAssociatedWith(issue);

        // Get patches based upon their status e.g. open PRs
        patches = aphrodite.getPatchesByStatus(repository, PatchStatus.OPEN);

        // Add a comment to a patch
        aphrodite.addCommentToPatch(patch, "Example Comment");
    }
}
