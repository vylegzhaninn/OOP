package vylegzhanin.task241.infra;

/**
 * Результат выполнения терминальной команды.
 *
 * @param exitCode код возврата
 * @param output   вывод команды (stdout/stderr)
 */
public record CommandResult(int exitCode, String output) {
    /**
     * Проверяет, была ли команда выполнена успешно.
     *
     * @return true, если код возврата равен 0, иначе false
     */
    public boolean isSuccess() {
        return exitCode == 0;
    }
}
