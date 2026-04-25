package vylegzhanin.task241.dsl;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.IOException;
import java.nio.file.Path;

final class DslScriptExecutor {
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

