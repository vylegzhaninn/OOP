package vylegzhanin.pizzeria;

import com.fasterxml.jackson.databind.ObjectMapper;
import vylegzhanin.pizzeria.controllers.Pizzeria;
import vylegzhanin.pizzeria.configs.AppConfig;

import java.io.File;
import java.io.IOException;

public class Main {
    static void main() throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        AppConfig config = mapper.readValue(new File("configuration.json"), AppConfig.class);

        Pizzeria pizzeria = new Pizzeria(config);
        pizzeria.work();
    }
}
