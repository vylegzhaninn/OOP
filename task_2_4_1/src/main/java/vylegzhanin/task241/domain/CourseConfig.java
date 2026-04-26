package vylegzhanin.task241.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс конфигурации курса, содержащий информацию о заданиях, студентах,
 * отправленных решениях, контрольных точках и настройках.
 */
public final class CourseConfig {
    private final Map<String, TaskSpec> tasks;
    private final Map<String, StudentSpec> students;
    private final List<SubmissionSpec> submissions;
    private final List<CheckpointSpec> checkpoints;
    private final SettingsSpec settings;

    /**
     * Конструктор для создания объекта конфигурации курса.
     *
     * @param tasks       карта заданий
     * @param students    карта студентов
     * @param submissions список отправленных студентами решений
     * @param checkpoints список контрольных точек (checkpoints)
     * @param settings    настройки курса
     */
    public CourseConfig(
        Map<String, TaskSpec> tasks,
        Map<String, StudentSpec> students,
        List<SubmissionSpec> submissions,
        List<CheckpointSpec> checkpoints,
        SettingsSpec settings
    ) {
        this.tasks = Map.copyOf(tasks);
        this.students = Map.copyOf(students);
        this.submissions = List.copyOf(submissions);
        this.checkpoints = List.copyOf(checkpoints);
        this.settings = settings;
    }

    /**
     * Создает пустую конфигурацию курса с настройками по умолчанию.
     *
     * @return пустая конфигурация {@link CourseConfig}
     */
    public static CourseConfig empty() {
        return new CourseConfig(Map.of(), Map.of(), List.of(), List.of(), SettingsSpec.defaults());
    }

    /**
     * Объединяет текущую конфигурацию с переданной конфигурацией.
     * Значения из переданной конфигурации перезаписывают или дополняют текущие.
     *
     * @param overlay конфигурация, которая накладывается поверх текущей
     * @return новая объединенная конфигурация
     */
    public CourseConfig mergedWith(CourseConfig overlay) {
        Map<String, TaskSpec> mergedTasks = new LinkedHashMap<>(tasks);
        mergedTasks.putAll(overlay.tasks);

        Map<String, StudentSpec> mergedStudents = new LinkedHashMap<>(students);
        mergedStudents.putAll(overlay.students);

        List<SubmissionSpec> mergedSubmissions = new ArrayList<>(submissions);
        mergedSubmissions.addAll(overlay.submissions);

        List<CheckpointSpec> mergedCheckpoints = new ArrayList<>(checkpoints);
        mergedCheckpoints.addAll(overlay.checkpoints);

        return new CourseConfig(
            mergedTasks,
            mergedStudents,
            mergedSubmissions,
            mergedCheckpoints,
            overlay.settings
        );
    }

    /**
     * Возвращает карту заданий.
     *
     * @return карта заданий
     */
    public Map<String, TaskSpec> tasks() {
        return tasks;
    }

    /**
     * Возвращает карту студентов.
     *
     * @return карта студентов
     */
    public Map<String, StudentSpec> students() {
        return students;
    }

    /**
     * Возвращает список отправленных решений.
     *
     * @return список решений (submissions)
     */
    public List<SubmissionSpec> submissions() {
        return submissions;
    }

    /**
     * Возвращает список контрольных точек.
     *
     * @return список контрольных точек (checkpoints)
     */
    public List<CheckpointSpec> checkpoints() {
        return checkpoints;
    }

    /**
     * Возвращает настройки конфигурации.
     *
     * @return настройки курса
     */
    public SettingsSpec settings() {
        return settings;
    }
}
