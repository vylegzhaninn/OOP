package vylegzhanin.task241.service;

import java.util.Comparator;
import java.util.List;
import vylegzhanin.task241.domain.GradeBound;

/**
 * Сервис вычисления итоговой оценки. Конвертирует баллы в буквенную оценку в соответствии с заданными границами.
 */
public class GradeService {
    /**
     * Конвертирует количество баллов в текстовую оценку.
     *
     * @param points    количество заработанных студентом баллов
     * @param maxPoints максимальное доступное количество баллов
     * @param bounds    список конверсионных границ (грейдов), отсортированных по убыванию требуемого процента
     * @return строка с буквенной оценкой (например, "A", "B", "C")
     */
    public String resolve(double points, double maxPoints, List<GradeBound> bounds) {
        if (maxPoints <= 0) {
            return "N/A";
        }
        double percent = points * 100.0 / maxPoints;
        List<GradeBound> sorted = bounds.stream()
            .sorted(Comparator.comparingDouble(GradeBound::minPercent).reversed())
            .toList();
        for (GradeBound bound : sorted) {
            if (percent >= bound.minPercent()) {
                return bound.grade();
            }
        }
        return "N/A";
    }
}
