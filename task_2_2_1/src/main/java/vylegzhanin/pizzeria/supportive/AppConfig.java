package vylegzhanin.pizzeria.supportive;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AppConfig(
        @JsonProperty("N") int bakersCount,
        @JsonProperty("M") int couriersCount,
        @JsonProperty("T") int storageCapacity,
        @JsonProperty("WorkTime") long workTime,
        @JsonProperty("OrderInterval") long orderInterval,
        @JsonProperty("PizzeriaWorkTime") long pizzeriaWorkTime
) {}
