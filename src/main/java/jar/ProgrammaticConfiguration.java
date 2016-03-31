package jar;

import org.jboss.set.aphrodite.Aphrodite;
import org.jboss.set.aphrodite.config.AphroditeConfig;
import org.jboss.set.aphrodite.config.IssueTrackerConfig;
import org.jboss.set.aphrodite.config.RepositoryConfig;
import org.jboss.set.aphrodite.config.TrackerType;
import org.jboss.set.aphrodite.repository.services.common.RepositoryType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Ryan Emerson
 */
public class ProgrammaticConfiguration {
    public static void main(String[] args) throws Exception {
        // Jira
        IssueTrackerConfig jiraService =
                new IssueTrackerConfig("https://issues.stage.jboss.org",
                        "username",
                        "password",
                        TrackerType.JIRA,
                        -1);

        List<IssueTrackerConfig> issueTrackerConfigs = new ArrayList<>();
        issueTrackerConfigs.add(jiraService);


        // Github
        RepositoryConfig githubService =
                new RepositoryConfig("https://github.com/",
                        "username",
                        "password",
                        RepositoryType.GITHUB);

        List<RepositoryConfig> repositoryConfigs = new ArrayList<>();
        repositoryConfigs.add(githubService);

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        AphroditeConfig config = new AphroditeConfig(executorService, issueTrackerConfigs, repositoryConfigs);
        try (Aphrodite aphrodite = Aphrodite.instance(config)) {
            // Perform some operations
        }
    }
}
