package vylegzhanin.task241.domain;

import java.time.LocalDate;

/**
 * Описание отправленного студентом решения по заданию (коммит/pull request).
 *
 * @param studentGithub логин студента на Github
 * @param taskId        идентификатор задания
 * @param submittedAt   дата сдачи задания
 * @param bonusPoints   количество дополнительных бонусных баллов за сдачу
 */
public record SubmissionSpec(
    String studentGithub,
    String taskId,
    LocalDate submittedAt,
    double bonusPoints
) {
}
