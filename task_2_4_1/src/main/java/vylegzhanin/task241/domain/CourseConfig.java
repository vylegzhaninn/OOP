package vylegzhanin.task241.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class CourseConfig {
    private final Map<String, TaskSpec> tasks;
    private final Map<String, StudentSpec> students;
    private final List<SubmissionSpec> submissions;
    private final List<CheckpointSpec> checkpoints;
    private final SettingsSpec settings;

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

    public static CourseConfig empty() {
        return new CourseConfig(Map.of(), Map.of(), List.of(), List.of(), SettingsSpec.defaults());
    }

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

    public Map<String, TaskSpec> tasks() {
        return tasks;
    }

    public Map<String, StudentSpec> students() {
        return students;
    }

    public List<SubmissionSpec> submissions() {
        return submissions;
    }

    public List<CheckpointSpec> checkpoints() {
        return checkpoints;
    }

    public SettingsSpec settings() {
        return settings;
    }
}

