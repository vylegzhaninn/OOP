package vylegzhanin.task241.service;

/**
 * Сырой результат запуска проверки одной задачи для студента (git clone, build, javadoc, checkstyle, test).
 *
 * @param gitOk        успешно ли скачан/обновлен репозиторий
 * @param compileOk    успешно ли откомпилирован проект
 * @param javadocOk    успешно ли прошла генерация (или проверка) javadoc
 * @param checkstyleOk успешно ли пройдена проверка на стандарты кодирования (styleguide)
 * @param testsOk      успешно ли выполнилась задача тестирования
 * @param passed       кол-во пройденных тестов
 * @param failed       кол-во упавших тестов
 * @param skipped      кол-во пропущенных тестов
 * @param details      текстовое описание ошибки(детали из вывода командной строки) в случае падения
 */
public record RepoRunResult(
    boolean gitOk,
    boolean compileOk,
    boolean javadocOk,
    boolean checkstyleOk,
    boolean testsOk,
    int passed,
    int failed,
    int skipped,
    String details
) {
    /**
     * Создает результат запуска, скомпонованный как полностью проваленный с указанием причины.
     *
     * @param details текст (причина) ошибки
     * @return {@link RepoRunResult} где все флаги равны false
     */
    public static RepoRunResult failed(String details) {
        return new RepoRunResult(false, false, false, false, false, 0, 0, 0, details);
    }

    /**
     * Вычисляет долю (отношение) успешных тестов ко всем тестам.
     *
     * @return коэффициент успешности от 0.0 до 1.0
     */
    public double successRatio() {
        int total = passed + failed + skipped;
        if (!testsOk || total == 0) {
            return 0.0;
        }
        return (double) passed / total;
    }
}
