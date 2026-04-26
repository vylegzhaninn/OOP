package vylegzhanin.task241.service;

import java.util.List;
import java.util.Map;

/**
 * Итоговый результат оценки студента по курсу.
 *
 * @param github           логин на GitHub
 * @param fullName         полное имя студента
 * @param groupName        название учебной группы
 * @param taskResults      список результатов по каждому заданию
 * @param totalPoints      общее число заработанных баллов
 * @param maxPoints        максимально возможное количество баллов за курс
 * @param checkpointPoints баллы, заработанные на каждой контрольной точке
 * @param finalGrade       итоговая буквенная или текстовая оценка студента (например, "A" или "B")
 */
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
