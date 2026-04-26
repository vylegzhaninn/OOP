package vylegzhanin.task241.report;

import vylegzhanin.task241.service.StudentScoreReport;
import vylegzhanin.task241.service.TaskScoreResult;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Рендерер HTML-отчета.
 * Генерирует итоговую сводку по оценкам студентов в формате HTML на основе вычисленной статистики.
 */
public final class HtmlReportRenderer {
    /**
     * Создает HTML-документ с результатами автоматической проверки.
     *
     * @param reports список отчетов по студентам
     * @return строка, содержащая готовый HTML-код
     */
    public String render(List<StudentScoreReport> reports) {
        StringBuilder html = new StringBuilder();
        html.append("<!doctype html><html><head><meta charset=\"UTF-8\">")
                .append("<title>oop-checker test</title>")
                .append("<style>")
                .append("body{font-family:'Courier New',monospace;margin:16px}")
                .append("h1,h2,h3{margin:12px 0}")
                .append("table{border-collapse:collapse;width:100%;margin:10px 0 18px}")
                .append("th,td{border:1px solid #333;padding:6px 8px;vertical-align:top}")
                .append("th{font-weight:700;background:#f7f7f7}")
                .append(".center{text-align:center}")
                .append("</style></head><body>");

        html.append("<h1>oop-checker test</h1>");

        Map<String, List<StudentScoreReport>> groups = reports.stream()
                .collect(Collectors.groupingBy(StudentScoreReport::groupName, LinkedHashMap::new, Collectors.toList()));

        for (Map.Entry<String, List<StudentScoreReport>> groupEntry : groups.entrySet()) {
            List<StudentScoreReport> students = groupEntry.getValue().stream()
                    .sorted(Comparator.comparing(StudentScoreReport::fullName))
                    .toList();

            html.append("<h2>Группа ").append(escape(groupEntry.getKey())).append("</h2>");

            Map<String, String> tasks = new LinkedHashMap<>();
            for (StudentScoreReport student : students) {
                for (TaskScoreResult task : student.taskResults()) {
                    tasks.putIfAbsent(task.taskId(), task.taskTitle());
                }
            }

            for (Map.Entry<String, String> taskEntry : tasks.entrySet()) {
                String taskId = taskEntry.getKey();
                String taskTitle = taskEntry.getValue();

                html.append("<table><thead>")
                        .append("<tr><th colspan=\"7\">Лабораторная ")
                        .append(escape(taskId));
                if (taskTitle != null && !taskTitle.isBlank()) {
                    html.append(" (").append(escape(taskTitle)).append(")");
                }
                html.append("</th></tr>")
                        .append("<tr><th>Студент</th><th>Сборка</th><th>Документация</th><th>Style guide</th><th>Тесты</th><th>Доп. балл</th><th>Общий балл</th></tr>")
                        .append("</thead><tbody>");

                for (StudentScoreReport student : students) {
                    TaskScoreResult result = findTaskResult(student.taskResults(), taskId);
                    html.append("<tr><td>").append(escape(student.fullName())).append("</td>");
                    if (result == null) {
                        html.append("<td class=\"center\">-</td><td class=\"center\">-</td><td class=\"center\">-</td>")
                                .append("<td class=\"center\">0/0/0</td><td class=\"center\">0</td><td class=\"center\">0</td></tr>");
                        continue;
                    }
                    html.append("<td class=\"center\">").append(mark(result.buildOk())).append("</td>")
                            .append("<td class=\"center\">").append(mark(result.docsOk())).append("</td>")
                            .append("<td class=\"center\">").append(mark(result.styleOk())).append("</td>")
                            .append("<td class=\"center\">").append(result.passed()).append("/").append(result.failed()).append("/").append(result.skipped()).append("</td>")
                            .append("<td class=\"center\">").append(formatNumber(result.bonusPoints())).append("</td>")
                            .append("<td class=\"center\">").append(formatNumber(result.points())).append("</td></tr>");
                }
                html.append("</tbody></table>");
            }

            html.append("<table><thead><tr><th colspan=\"")
                    .append(tasks.size() + 4)
                    .append("\">Общая статистика группы ")
                    .append(escape(groupEntry.getKey()))
                    .append("</th></tr><tr><th>Студент</th>");

            for (String taskId : tasks.keySet()) {
                html.append("<th>").append(escape(taskId)).append("</th>");
            }
            html.append("<th>Сумма</th><th>Активность</th><th>Оценка</th></tr></thead><tbody>");

            for (StudentScoreReport student : students) {
                html.append("<tr><td>").append(escape(student.fullName())).append("</td>");

                int nonZero = 0;
                for (String taskId : tasks.keySet()) {
                    TaskScoreResult result = findTaskResult(student.taskResults(), taskId);
                    double points = result == null ? 0 : result.points();
                    if (points > 0) {
                        nonZero++;
                    }
                    html.append("<td class=\"center\">").append(formatNumber(points)).append("</td>");
                }

                int taskCount = Math.max(1, tasks.size());
                int activityPercent = (int) Math.round((nonZero * 100.0) / taskCount);
                html.append("<td class=\"center\">").append(formatNumber(student.totalPoints())).append("</td>")
                        .append("<td class=\"center\">").append(activityPercent).append("%</td>")
                        .append("<td class=\"center\">").append(escape(student.finalGrade())).append("</td></tr>");
            }

            html.append("</tbody></table>");
        }

        html.append("</body></html>");
        return html.toString();
    }

    /**
     * Ищет результат по конкретному заданию в списке результатов студента.
     *
     * @param results список результатов
     * @param taskId идентификатор задания
     * @return {@link TaskScoreResult} для указанного задания или null, если не найдено
     */
    private static TaskScoreResult findTaskResult(List<TaskScoreResult> results, String taskId) {
        for (TaskScoreResult result : results) {
            if (taskId.equals(result.taskId())) {
                return result;
            }
        }
        return null;
    }

    /**
     * Возвращает текстовую метку (плюс/минус) на основе логического значения.
     *
     * @param ok флаг успеха (true/false)
     * @return "+" если true, "-" если false
     */
    private static String mark(boolean ok) {
        return ok ? "+" : "-";
    }

    /**
     * Форматирует число баллов для вывода (оставляет только целую часть, если дробная равна нулю).
     *
     * @param value числовое значение баллов
     * @return отформатированная строка
     */
    private static String formatNumber(double value) {
        if (Math.abs(value - Math.rint(value)) < 1e-9) {
            return Long.toString(Math.round(value));
        }
        return String.format(java.util.Locale.US, "%.2f", value);
    }

    /**
     * Экранирует спецсимволы HTML для безопасного вывода в документ.
     *
     * @param value исходная строка
     * @return строка с экранированными символами
     */
    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
