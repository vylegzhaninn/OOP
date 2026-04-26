package vylegzhanin.task241.domain;

/**
 * Описание студента.
 *
 * @param github        логин на GitHub
 * @param fullName      полное имя студента
 * @param repositoryUrl ссылка на репозиторий студента
 * @param groupName     название академической группы
 */
public record StudentSpec(
    String github,
    String fullName,
    String repositoryUrl,
    String groupName
) {
}
