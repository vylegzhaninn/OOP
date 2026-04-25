package vylegzhanin.task241.domain;

import java.time.LocalDate;

public record TaskSpec(
        String id,
        String title,
        double maxPoints,
        LocalDate softDeadline,
        LocalDate hardDeadline
) {
}

