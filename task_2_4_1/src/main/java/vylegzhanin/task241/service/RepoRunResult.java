package vylegzhanin.task241.service;

public record RepoRunResult(
        boolean gitOk,
        boolean compileOk,
        boolean javadocOk,
        boolean checkstyleOk,
        boolean testsOk,
        int passed,
        int failed,
        int skipped,
        String details
) {
    public static RepoRunResult failed(String details) {
        return new RepoRunResult(false, false, false, false, false, 0, 0, 0, details);
    }

    public double successRatio() {
        int total = passed + failed + skipped;
        if (!testsOk || total == 0) {
            return 0.0;
        }
        return (double) passed / total;
    }
}

