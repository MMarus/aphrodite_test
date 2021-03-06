package jar;

import org.jboss.set.aphrodite.Aphrodite;
import org.jboss.set.aphrodite.domain.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JSONConfiguration {
    public static void main(String[] args) throws Exception {
        System.setProperty("aphrodite.config", "aphrodite.properties.json");
        try (Aphrodite aphrodite = Aphrodite.instance()) {
            // IssueTracker methods
            searchIssues(aphrodite);
            getIssue(aphrodite);
            getMultipleIssues(aphrodite);
            updateIssue(aphrodite);
            addCommentsToIssue(aphrodite);

            // RepositoryService methods
            getPatch(aphrodite);
            getRepository(aphrodite);
            getPatchesByState(aphrodite);
            addCommentToPatch(aphrodite);
            checkLabelPermissions(aphrodite);
            addLabelToPatch(aphrodite);
            getPatchesAssociatedWithPatch(aphrodite);
            getCommitStatusFromPatch(aphrodite);

            // Stream service
            getRepositories(aphrodite);
            getStreams(aphrodite);
            getComponentNameBy(aphrodite);
        }
    }

    private static void searchIssues(Aphrodite aphrodite) throws Exception {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("Executing " + name + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        SearchCriteria sc = new SearchCriteria.Builder()
//                .setStatus(IssueStatus.CREATED)
                .setProduct("JBoss Enterprise Application Platform 6")
                .setMaxResults(5) // Per IssueTracker
                .setStage(new Stage())
                .setComponent("CLI")
                .build();

        List<Issue> result = aphrodite.searchIssues(sc);
        for (Issue issue : result) {
            System.out.println(issue.getTrackerId().get() + ": " + issue.getSummary().get() + "\n");
        }
    }

    private static void getIssue(Aphrodite aphrodite) throws Exception {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("Executing " + name + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Issue issue = aphrodite.getIssue(new URL("https://issues.stage.jboss.org/browse/JBEAP-288"));
        System.out.println(issue);
    }

    private static void getMultipleIssues(Aphrodite aphrodite) throws Exception {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("Executing " + name + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        List<URL> urls = new ArrayList<>();
        urls.add(new URL("https://partner-bugzilla.redhat.com/show_bug.cgi?id=1193793"));
        urls.add(new URL("https://partner-bugzilla.redhat.com/show_bug.cgi?id=1175722"));
        urls.add(new URL("https://issues.stage.jboss.org/browse/WFLY-110"));
        urls.add(new URL("https://issues.stage.jboss.org/browse/WFLY-4805?jql=project %3D WFLY"));
        List<Issue> result = aphrodite.getIssues(urls);
        System.out.println(result.size());
    }

    private static void updateIssue(Aphrodite aphrodite) throws Exception {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("Executing " + name + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Issue issue = aphrodite.getIssue(new URL("https://issues.stage.jboss.org/browse/JBEAP-291"));
//        Issue issue = aphrodite.getIssue(new URL("https://issues.stage.jboss.org/browse/WFCORE-751"));
//        Issue issue = aphrodite.getIssue(new URL("https://partner-bugzilla.redhat.com/show_bug.cgi?id=1231916"));
//        System.out.println(issue.getReleases());
//        List<Release> releases = new ArrayList<>();
//        releases.add(new Release("6.2.3"));
//        releases.add(new Release("1.0.0.CR6"));
//        releases.add(new Release("1.0.0.CR7"));
//        issue.setReleases(releases);

//        Map<String, FlagStatus> stream = new HashMap<>();
//        stream.put("jboss‑eap‑6.4.z", FlagStatus.SET);
//        issue.setStreamStatus(stream);

//        Stage stage = new Stage();
//        stage.setStatus(Flag.DEV, FlagStatus.SET);
//        issue.setStage(stage);

        issue.setAssignee(new User("remerson@redhat.com", "ryanemerson"));
//        issue.setSummary("Failed to initialize Aesh console while starting CLI on fresh Windows 2k8");
//        issue.getDependsOn().add(new URL("https://issues.stage.jboss.org/browse/WFCORE-750"));
//        issue.getBlocks().add(new URL("https://issues.stage.jboss.org/browse/WFCORE-749"));
        System.out.println(issue.getReporter());
        System.out.println(issue.getAssignee());
        aphrodite.updateIssue(issue);
        issue = aphrodite.getIssue(new URL("https://issues.stage.jboss.org/browse/WFCORE-751"));
        System.out.println(issue);
    }

    private static void addCommentsToIssue(Aphrodite aphrodite) throws Exception {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("Executing " + name + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        SearchCriteria sc = new SearchCriteria.Builder()
                .setMaxResults(2) // Per IssueTracker
                .setComponent("CLI")
                .build();

        List<Issue> issues = aphrodite.searchIssues(sc);
        issues.forEach(i -> System.out.println(i.getURL()));

        aphrodite.addCommentToIssue(issues.get(0), new Comment("Test", false));

        System.out.println("Success: " + aphrodite.addCommentToIssue(issues, new Comment("Test of single comment on multiple issues", false)));

        Map<Issue, Comment> commentMap = issues.stream().collect(Collectors
                .toMap(issue -> issue, issue -> new Comment("Test of independent comments :" + issue.getURL(), false)));
        System.out.println("Success: " + aphrodite.addCommentToIssue(commentMap));

        // Test empty collections
        commentMap.clear();
        issues.clear();
        System.out.println("Success: " + aphrodite.addCommentToIssue(commentMap));
        System.out.println("Success: " + aphrodite.addCommentToIssue(issues, new Comment("Shouldn't do anything", false)));
    }

    private static void getPatch(Aphrodite aphrodite) throws Exception {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("Executing " + name + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Patch patch = aphrodite.getPatch(new URL("https://github.com/jboss-set/aphrodite_test/pull/1"));
        System.out.println(patch);
    }

    private static void getRepository(Aphrodite aphrodite) throws Exception {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("Executing " + name + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Repository repository = aphrodite.getRepository(new URL("https://github.com/jboss-set/aphrodite_test"));
        System.out.println(repository);
    }

    private static void getPatchesByState(Aphrodite aphrodite) throws Exception {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("Executing " + name + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Repository repository = aphrodite.getRepository(new URL("https://github.com/jboss-set/aphrodite_test"));
        List<Patch> patches = aphrodite.getPatchesByState(repository, PatchState.OPEN);
        System.out.println(patches.size());
    }

    private static void addCommentToPatch(Aphrodite aphrodite) throws Exception {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("Executing " + name + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Patch patch = aphrodite.getPatch(new URL("https://github.com/jboss-set/aphrodite_test/pull/1"));
        aphrodite.addCommentToPatch(patch, "Example Comment");
    }

    private static void checkLabelPermissions(Aphrodite aphrodite) throws Exception {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("Executing " + name + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Repository repository = aphrodite.getRepository(new URL("https://github.com/jboss-set/aphrodite_test"));
        System.out.println("Permission to add Label to patch: " + aphrodite.isRepositoryLabelsModifiable(repository));

        // Should be false
        repository = aphrodite.getRepository(new URL("https://github.com/eclipse/egit-github"));
        System.out.println("Permission to add Label to patch: " + aphrodite.isRepositoryLabelsModifiable(repository));
    }

    private static void addLabelToPatch(Aphrodite aphrodite) throws Exception {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("Executing " + name + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Repository repository = aphrodite.getRepository(new URL("https://github.com/jboss-set/aphrodite_test"));
        List<Label> labels = aphrodite.getLabelsFromRepository(repository);
        System.out.println("Repository Labels: " + labels);
        Patch patch = aphrodite.getPatch(new URL("https://github.com/jboss-set/aphrodite_test/pull/1"));
        System.out.println("Current Labels: " + aphrodite.getLabelsFromPatch(patch));
        aphrodite.setLabelsToPatch(patch, labels);
        aphrodite.removeLabelFromPatch(patch, labels.get(0).getName());
    }

    private static void getPatchesAssociatedWithPatch(Aphrodite aphrodite) throws Exception {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("Executing " + name + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Issue issue = aphrodite.getIssue(new URL("https://partner-bugzilla.redhat.com/show_bug.cgi?id=1057835"));
        List<Patch> patches = aphrodite.getPatchesAssociatedWith(issue);
        System.out.println(patches);
    }

    private static void getCommitStatusFromPatch(Aphrodite aphrodite) throws Exception {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("Executing " + name + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Patch patch = aphrodite.getPatch(new URL("https://github.com/jboss-set/aphrodite/pull/54"));
        aphrodite.getCommitStatusFromPatch(patch);
        System.out.println(aphrodite.getCommitStatusFromPatch(patch));
    }

    private static void getRepositories(Aphrodite aphrodite) throws Exception {
        List<Repository> repoUrls = aphrodite.getDistinctURLRepositoriesFromStreams();
        System.out.println(repoUrls.size());

        repoUrls = aphrodite.getDistinctURLRepositoriesByStream("wildfly");
        System.out.println(repoUrls.size());
    }

    private static void getStreams(Aphrodite aphrodite) throws Exception {
        List<Stream> streams = aphrodite.getAllStreams();
        System.out.println(streams.size());

        streams = aphrodite.getStreamsBy(new Repository(new URL("https://github.com/aeshell/aesh")), new Codebase("master"));
        System.out.println(streams.size());

        System.out.println(aphrodite.getStream("wildfly"));
    }

    private static void getComponentNameBy(Aphrodite aphrodite) throws Exception {
        System.out.println(aphrodite.getComponentBy(new Repository(new URL("https://github.com/hal/core")), new Codebase("master")));
    }

}
