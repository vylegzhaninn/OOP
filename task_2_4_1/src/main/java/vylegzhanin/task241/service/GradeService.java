package vylegzhanin.task241.service;

import vylegzhanin.task241.domain.GradeBound;

import java.util.List;

public final class GradeService {
    public String resolve(double points, double maxPoints, List<GradeBound> bounds) {
        if (maxPoints <= 0) {
            return "N/A";
        }
        double percent = points * 100.0 / maxPoints;
        for (GradeBound bound : bounds) {
            if (percent >= bound.minPercent()) {
                return bound.grade();
            }
        }
        return "N/A";
    }
}

