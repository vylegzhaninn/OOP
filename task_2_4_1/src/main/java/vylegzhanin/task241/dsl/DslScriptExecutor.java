package vylegzhanin.task241.dsl;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.io.IOException;
import java.nio.file.Path;
import org.codehaus.groovy.control.CompilerConfiguration;

/**
 * Исполнитель DSL-скриптов, написанных на Groovy.
 * Настраивает компилятор и запускает скрипт в нужном окружении.
 */
class DslScriptExecutor {
    /**
     * Парсит и запускает конфигурационный файл Groovy DSL.
     *
     * @param configFile файл скрипта, который нужно выполнить
     * @param loader     загрузчик конфигурации, передаваемый внутрь скрипта
     * @return результат (как правило объект {@link vylegzhanin.task241.domain.CourseConfig}), который скрипт вернул через `resultConfig`
     * @throws IllegalStateException если не удалось прочитать или спарсить файл
     */
    Object execute(Path configFile, ConfigLoader loader) {
        Binding binding = new Binding();
        binding.setVariable("__loader", loader);

        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass("vylegzhanin.task241.dsl.OopDslScript");

        GroovyShell shell = new GroovyShell(getClass().getClassLoader(), binding, cc);
        try {
            Script script = shell.parse(configFile.toFile());
            script.run();
            return script.getProperty("resultConfig");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse DSL script: " + configFile, e);
        }
    }
}
