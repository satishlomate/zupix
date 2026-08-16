package io.zupix.cli;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ZupixCliTest {
    @Test
    void helpListsCoreCommands() throws Exception {
        var output = new ByteArrayOutputStream();
        var original = System.out;
        try {
            System.setOut(new PrintStream(output));
            ZupixCli.main(new String[]{"help"});
        } finally {
            System.setOut(original);
        }
        String text = output.toString();
        assertTrue(text.contains("zupix new <name>"));
        assertTrue(text.contains("zupix run"));
        assertTrue(text.contains("zupix version"));
    }

    @Test
    void versionReportsReleaseVersion() throws Exception {
        var output = new ByteArrayOutputStream();
        var original = System.out;
        try {
            System.setOut(new PrintStream(output));
            ZupixCli.main(new String[]{"version"});
        } finally {
            System.setOut(original);
        }
        assertTrue(output.toString().contains("Zupix CLI 0.1.0"));
    }
}
