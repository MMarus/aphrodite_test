package jar;

import org.jboss.set.aphrodite.Aphrodite;
import org.jboss.set.aphrodite.config.AphroditeConfig;
import org.jboss.set.aphrodite.config.IssueTrackerConfig;
import org.jboss.set.aphrodite.config.RepositoryConfig;
import org.jboss.set.aphrodite.domain.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ryan Emerson
 */
public class ProgrammaticConfiguration {
    public static void main(String[] args) throws Exception {
        IssueTrackerConfig jiraService =
                new IssueTrackerConfig("https://issues.stage.jboss.org", "username", "password", "bugzilla");
        List<IssueTrackerConfig> issueTrackerConfigs = new ArrayList<>();
        issueTrackerConfigs.add(jiraService);

        RepositoryConfig githubService = new RepositoryConfig("https://github.com/", "username", "password", "github");
        List<RepositoryConfig> repositoryConfigs = new ArrayList<>();
        repositoryConfigs.add(githubService);

        AphroditeConfig config = new AphroditeConfig(issueTrackerConfigs, repositoryConfigs);
        Aphrodite aphrodite = Aphrodite.instance(config);

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
