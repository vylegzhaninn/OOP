package vylegzhanin.task241.domain;

import java.util.List;
import java.util.Map;

/**
 * Класс конфигурации курса, содержащий информацию о заданиях, студентах,
 * отправленных решениях, контрольных точках и настройках.
 */
public record CourseConfig(
    Map<String, TaskSpec> tasks,
    Map<String, StudentSpec> students,
    List<SubmissionSpec> submissions,
    List<CheckpointSpec> checkpoints,
    SettingsSpec settings
) {
}
