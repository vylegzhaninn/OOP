package vylegzhanin.pizzeria;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import vylegzhanin.pizzeria.configs.AppConfig;
import vylegzhanin.pizzeria.controllers.Pizzeria;

/**
 * Точка входа приложения «Пиццерия».
 *
 * <p>Загружает конфигурацию из файла {@code configuration.json},
 * создаёт экземпляр {@link Pizzeria} и запускает её работу.
 * Все исключения перехватываются здесь и выводятся в {@code System.err}
 * в виде информативных сообщений — без проброса наружу.</p>
 */
public class Main {

    /**
     * Точка входа JVM.
     *
     * @param args аргументы командной строки (не используются)
     */
    static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            AppConfig config = mapper.readValue(new File("configuration.json"), AppConfig.class);
            Pizzeria pizzeria = new Pizzeria(config);
            pizzeria.work();
        } catch (IOException e) {
            System.err.println("Ошибка чтения конфигурации: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Работа пиццерии прервана: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
