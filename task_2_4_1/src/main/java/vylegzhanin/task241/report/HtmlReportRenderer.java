package vylegzhanin.task241.report;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import vylegzhanin.task241.domain.report.StudentScoreReport;
import vylegzhanin.task241.domain.report.TaskScoreResult;

/**
 * Рендерер HTML-отчёта на основе Mustache-шаблона {@code report.mustache}.
 * Преобразует список отчётов студентов в плоскую модель данных и подставляет её в шаблон.
 * HTML-логика сосредоточена в шаблоне, Java-класс отвечает только за подготовку данных.
 */
public class HtmlReportRenderer {

    private static final Template TEMPLATE;

    static {
        try (Reader reader = new InputStreamReader(
            Objects.requireNonNull(
                HtmlReportRenderer.class.getResourceAsStream("/report.mustache"),
                "report.mustache not found in classpath"
            ),
            StandardCharsets.UTF_8
        )) {
            TEMPLATE = Mustache.compiler().compile(reader);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load report.mustache", e);
        }
    }

    /**
     * Генерирует HTML-документ на основе переданных отчётов студентов.
     *
     * @param reports список отчётов по студентам
     * @return строка с готовым HTML
     */
    public String render(List<StudentScoreReport> reports) {
        return TEMPLATE.execute(Map.of("groups", buildGroups(reports)));
    }

    private static List<Map<String, Object>> buildGroups(List<StudentScoreReport> reports) {
        Map<String, List<StudentScoreReport>> byGroup = reports.stream()
            .collect(Collectors.groupingBy(
                StudentScoreReport::groupName, LinkedHashMap::new, Collectors.toList()
            ));

        List<Map<String, Object>> groups = new ArrayList<>();
        for (Map.Entry<String, List<StudentScoreReport>> entry : byGroup.entrySet()) {
            String groupName = entry.getKey();
            List<StudentScoreReport> students = entry.getValue().stream()
                .sorted(Comparator.comparing(StudentScoreReport::fullName))
                .toList();

            Set<String> taskIds = new LinkedHashSet<>();
            for (StudentScoreReport student : students) {
                for (TaskScoreResult task : student.taskResults()) {
                    taskIds.add(task.taskId());
                }
            }

            groups.add(Map.of(
                "groupName",      groupName,
                "taskTables",     buildTaskTables(taskIds, students),
                "taskIds",        List.copyOf(taskIds),
                "summaryColspan", taskIds.size() + 4,
                "summaryRows",    buildSummaryRows(taskIds, students)
            ));
        }
        return groups;
    }

    private static List<Map<String, Object>> buildTaskTables(
        Set<String> taskIds, List<StudentScoreReport> students
    ) {
        List<Map<String, Object>> tables = new ArrayList<>();
        for (String taskId : taskIds) {
            List<Map<String, Object>> rows = new ArrayList<>();
            for (StudentScoreReport student : students) {
                rows.add(buildStudentRow(
                    student.fullName(),
                    findTaskResult(student.taskResults(), taskId)
                ));
            }
            tables.add(Map.of("taskId", taskId, "studentRows", rows));
        }
        return tables;
    }

    private static Map<String, Object> buildStudentRow(String fullName, TaskScoreResult result) {
        if (result == null) {
            return Map.of(
                "studentName", fullName,
                "buildMark", "-",
                "docsMark",  "-",
                "styleMark", "-",
                "tests",  "0/0/0",
                "bonus",  "0",
                "points", "0"
            );
        }
        return Map.of(
            "studentName", fullName,
            "buildMark", mark(result.buildOk()),
            "docsMark",  mark(result.docsOk()),
            "styleMark", mark(result.styleOk()),
            "tests",  result.passed() + "/" + result.failed() + "/" + result.skipped(),
            "bonus",  formatNumber(result.bonusPoints()),
            "points", formatNumber(result.points())
        );
    }

    private static List<Map<String, Object>> buildSummaryRows(
        Set<String> taskIds, List<StudentScoreReport> students
    ) {
        List<Map<String, Object>> rows = new ArrayList<>();
        for (StudentScoreReport student : students) {
            int nonZero = 0;
            List<String> taskPoints = new ArrayList<>();
            for (String taskId : taskIds) {
                TaskScoreResult result = findTaskResult(student.taskResults(), taskId);
                double points = result == null ? 0 : result.points();
                if (points > 0) nonZero++;
                taskPoints.add(formatNumber(points));
            }
            int activity = (int) Math.round(nonZero * 100.0 / Math.max(1, taskIds.size()));
            rows.add(Map.of(
                "studentName", student.fullName(),
                "taskPoints",  taskPoints,
                "total",       formatNumber(student.totalPoints()),
                "activity",    activity + "%",
                "grade",       student.finalGrade()
            ));
        }
        return rows;
    }

    private static TaskScoreResult findTaskResult(List<TaskScoreResult> results, String taskId) {
        return results.stream()
            .filter(r -> taskId.equals(r.taskId()))
            .findFirst().orElse(null);
    }

    private static String mark(boolean ok) {
        return ok ? "+" : "-";
    }

    /**
     * Форматирует число баллов: целые выводит без дроби, дробные — с двумя знаками.
     *
     * @param value числовое значение
     * @return отформатированная строка
     */
    private static String formatNumber(double value) {
        if (Math.abs(value - Math.rint(value)) < 1e-9) {
            return Long.toString(Math.round(value));
        }
        return String.format(java.util.Locale.US, "%.2f", value);
    }
}
