package vylegzhanin.task241.infra;

public record CommandResult(int exitCode, String output) {
    public boolean isSuccess() {
        return exitCode == 0;
    }
}

