package vylegzhanin.task241.service;

import vylegzhanin.task241.domain.CheckpointSpec;
import vylegzhanin.task241.domain.CourseConfig;
import vylegzhanin.task241.domain.SettingsSpec;
import vylegzhanin.task241.domain.StudentSpec;
import vylegzhanin.task241.domain.SubmissionSpec;
import vylegzhanin.task241.domain.TaskSpec;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CourseEvaluationService {
    private final RepositoryEvaluationService repositoryEvaluationService;
    private final ScoreCalculator scoreCalculator;
    private final GradeService gradeService;

    public CourseEvaluationService(
            RepositoryEvaluationService repositoryEvaluationService,
            ScoreCalculator scoreCalculator,
            GradeService gradeService
    ) {
        this.repositoryEvaluationService = repositoryEvaluationService;
        this.scoreCalculator = scoreCalculator;
        this.gradeService = gradeService;
    }

    public List<StudentScoreReport> evaluate(CourseConfig config, Path launchDirectory) {
        SettingsSpec settings = config.settings();
        Path workspace = launchDirectory.resolve(settings.workspace()).normalize();
        Map<String, RepoRunResult> repoCache = new HashMap<>();

        Map<String, List<SubmissionSpec>> submissionsByStudent = config.submissions().stream()
                .collect(Collectors.groupingBy(SubmissionSpec::studentGithub));

        List<StudentScoreReport> reports = new ArrayList<>();
        List<StudentSpec> students = config.students().values().stream()
                .sorted(Comparator.comparing(StudentSpec::groupName).thenComparing(StudentSpec::fullName))
                .toList();

        for (StudentSpec student : students) {
            List<SubmissionSpec> studentSubmissions = submissionsByStudent.getOrDefault(student.github(), List.of());
            RepoRunResult runResult = repoCache.computeIfAbsent(
                    student.github(),
                    key -> repositoryEvaluationService.runForStudent(
                            student.github(),
                            student.repositoryUrl(),
                            settings,
                            workspace
                    )
            );

            List<TaskScoreResult> taskResults = new ArrayList<>();
            for (SubmissionSpec submission : studentSubmissions) {
                TaskSpec task = config.tasks().get(submission.taskId());
                if (task == null) {
                    taskResults.add(new TaskScoreResult(
                            submission.taskId(),
                            "",
                            0,
                            0,
                            submission.bonusPoints(),
                            false,
                            false,
                            false,
                            0,
                            0,
                            0,
                            "UNKNOWN_TASK",
                            "Task is not defined in DSL"
                    ));
                    continue;
                }
                taskResults.add(scoreCalculator.calculate(task, submission, runResult, settings));
            }

            double total = taskResults.stream().mapToDouble(TaskScoreResult::points).sum();
            double max = taskResults.stream().mapToDouble(TaskScoreResult::maxPoints).sum();
            Map<String, Double> checkpoints = evaluateCheckpoints(config.checkpoints(), config.tasks(), studentSubmissions, taskResults);
            String grade = gradeService.resolve(total, max, settings.gradeBounds());

            reports.add(new StudentScoreReport(
                    student.github(),
                    student.fullName(),
                    student.groupName(),
                    List.copyOf(taskResults),
                    round(total),
                    round(max),
                    checkpoints,
                    grade
            ));
        }

        return reports;
    }

    private Map<String, Double> evaluateCheckpoints(
            List<CheckpointSpec> checkpoints,
            Map<String, TaskSpec> tasks,
            List<SubmissionSpec> submissions,
            List<TaskScoreResult> taskResults
    ) {
        Map<String, TaskScoreResult> byTask = taskResults.stream()
                .collect(Collectors.toMap(TaskScoreResult::taskId, t -> t, (a, b) -> b));

        Map<String, Double> points = new LinkedHashMap<>();
        for (CheckpointSpec checkpoint : checkpoints) {
            double cpTotal = 0;
            for (SubmissionSpec submission : submissions) {
                TaskSpec task = tasks.get(submission.taskId());
                TaskScoreResult result = byTask.get(submission.taskId());
                if (task == null || result == null || task.softDeadline() == null) {
                    continue;
                }
                if (!task.softDeadline().isAfter(checkpoint.date())) {
                    cpTotal += result.points();
                }
            }
            points.put(checkpoint.name(), round(cpTotal));
        }
        return points;
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
