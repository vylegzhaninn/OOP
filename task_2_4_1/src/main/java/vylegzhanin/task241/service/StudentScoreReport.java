package vylegzhanin.task241.service;

import java.util.List;
import java.util.Map;

public record StudentScoreReport(
        String github,
        String fullName,
        String groupName,
        List<TaskScoreResult> taskResults,
        double totalPoints,
        double maxPoints,
        Map<String, Double> checkpointPoints,
        String finalGrade
) {
}

