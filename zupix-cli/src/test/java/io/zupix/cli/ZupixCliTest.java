package io.zupix.cli;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ZupixCliTest {
    @Test
    void helpListsCoreCommands() throws Exception {
        var output = new ByteArrayOutputStream();
        var original = System.out;
        try { System.setOut(new PrintStream(output)); ZupixCli.main(new String[]{"help"}); }
        finally { System.setOut(original); }
        String text = output.toString();
        assertTrue(text.contains("zupix new <name>"));
        assertTrue(text.contains("zupix run"));
        assertTrue(text.contains("zupix version"));
    }

    @Test
    void versionReportsReleaseVersion() throws Exception {
        var output = new ByteArrayOutputStream();
        var original = System.out;
        try { System.setOut(new PrintStream(output)); ZupixCli.main(new String[]{"version"}); }
        finally { System.setOut(original); }
        assertTrue(output.toString().contains("Zupix CLI 0.1.0"));
    }

    @Test
    void newCreatesHttpBackedProjectSkeleton() throws Exception {
        Path temp = Files.createTempDirectory("zupix-cli-test");
        Path project = temp.resolve("hello-api");
        try {
            ZupixCli.main(new String[]{"new", project.toString()});
            assertTrue(Files.exists(project.resolve("pom.xml")));
            assertTrue(Files.exists(project.resolve("src/main/java/Application.java")));
            assertTrue(Files.readString(project.resolve("pom.xml")).contains("<artifactId>zupix-http</artifactId>"));
            assertTrue(Files.readString(project.resolve("src/main/java/Application.java")).contains("ZupixApplication"));
        } finally {
            Files.walk(temp).sorted((a, b) -> b.compareTo(a)).forEach(path -> {
                try { Files.deleteIfExists(path); } catch (Exception ignored) { }
            });
        }
    }
}
