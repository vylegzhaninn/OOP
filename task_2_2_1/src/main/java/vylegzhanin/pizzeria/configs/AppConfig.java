package vylegzhanin.pizzeria.configs;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Конфигурация пиццерии, загружаемая из JSON-файла {@code configuration.json}.
 *
 * <p>Все поля неизменяемы и устанавливаются один раз при десериализации
 * с помощью Jackson.
 * (аннотация {@link JsonProperty}).</p>
 *
 * @param bakersCount       количество пекарей (JSON: {@code "N"})
 * @param couriersCount     количество курьеров (JSON: {@code "M"})
 * @param storageCapacity   максимальная ёмкость склада готовых заказов (JSON: {@code "T"})
 * @param workTime          максимальное время выполнения одного заказа работником, мс (JSON: {@code "WorkTime"})
 * @param trankSize         максимальный суммарный размер заказов в багажнике курьера (JSON: {@code "TrunkSize"})
 * @param orderInterval     интервал между генерацией новых заказов, мс (JSON: {@code "OrderInterval"})
 * @param pizzeriaWorkTime  общее время работы пиццерии, мс (JSON: {@code "PizzeriaWorkTime"})
 */
public record AppConfig(
        @JsonProperty("N") int bakersCount,
        @JsonProperty("M") int couriersCount,
        @JsonProperty("T") int storageCapacity,
        @JsonProperty("WorkTime") long workTime,
        @JsonProperty("TrunkSize") int trankSize,
        @JsonProperty("OrderInterval") long orderInterval,
        @JsonProperty("PizzeriaWorkTime") long pizzeriaWorkTime
) {}
