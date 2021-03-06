package org.apereo.cas.util;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;

import static org.junit.Assert.*;

/**
 * This is {@link ScriptingUtilsTests}.
 *
 * @author Misagh Moayyed
 * @since 5.3.0
 */
@RunWith(JUnit4.class)
public class ScriptingUtilsTests {

    @Test
    public void verifyInlineGroovyScript() {
        assertTrue(ScriptingUtils.isInlineGroovyScript("groovy {return 0}"));
    }

    @Test
    public void verifyExternalGroovyScript() {
        assertTrue(ScriptingUtils.isExternalGroovyScript("file:/etc/cas/sample.groovy"));
    }

    @Test
    public void verifyGroovyScriptShellExecution() {
        final Object result = ScriptingUtils.executeGroovyShellScript("return name", CollectionUtils.wrap("name", "casuser"), String.class);
        assertEquals("casuser", result.toString());
    }

    @Test
    @SneakyThrows
    public void verifyGroovyResourceExecution() {
        final File file = File.createTempFile("test", ".groovy");
        FileUtils.write(file, "def process(String name) { return name }");
        final Resource resource = new FileSystemResource(file);

        final Object result = ScriptingUtils.executeGroovyScript(resource, "process", String.class, "casuser");
        assertEquals("casuser", result.toString());
    }

    @Test
    @SneakyThrows
    public void verifyGroovyResourceEngineExecution() {
        final Object result = ScriptingUtils.executeGroovyScriptEngine("return name", CollectionUtils.wrap("name", "casuser"), String.class);
        assertEquals("casuser", result.toString());
    }

    @Test
    @SneakyThrows
    public void verifyResourceScriptEngineExecution() {
        final File file = File.createTempFile("test", ".groovy");
        FileUtils.write(file, "def run(String name) { return name }");

        final Object result = ScriptingUtils.executeScriptEngine(file.getCanonicalPath(), new Object[]{"casuser"}, String.class);
        assertEquals("casuser", result.toString());
    }
}
