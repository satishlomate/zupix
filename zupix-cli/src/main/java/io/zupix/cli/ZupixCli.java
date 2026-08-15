package io.zupix.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Initial Zupix command-line interface. */
public final class ZupixCli {
    private ZupixCli() {}

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || "help".equals(args[0])) {
            help();
            return;
        }
        switch (args[0]) {
            case "new" -> requireProjectName(args);
            case "version" -> System.out.println("Zupix CLI 0.1.0-SNAPSHOT");
            default -> {
                System.err.println("Unknown command: " + args[0]);
                help();
                System.exit(1);
            }
        }
    }

    private static void requireProjectName(String[] args) throws IOException {
        if (args.length < 2 || args[1].isBlank()) throw new IllegalArgumentException("Usage: zupix new <name>");
        createProject(args[1]);
    }

    private static void createProject(String name) throws IOException {
        Path root = Path.of(name).toAbsolutePath().normalize();
        Files.createDirectories(root.resolve("src/main/java"));
        Files.createDirectories(root.resolve("src/test/java"));
        Files.writeString(root.resolve("README.md"), "# " + name + "\n\nCreated with Zupix CLI.\n");
        Files.writeString(root.resolve(".gitignore"), "target/\n.idea/\n.vscode/\n");
        Files.writeString(root.resolve("src/main/java/Application.java"),
                "import io.zupix.Get;\nimport io.zupix.ZupixApp;\n\n@ZupixApp\npublic class Application {\n    @Get(\"/\")\n    public String hello() { return \"Hello Zupix!\"; }\n}\n");
        System.out.println("Created Zupix project: " + root);
    }

    private static void help() {
        System.out.println("Zupix CLI");
        System.out.println("  zupix new <name>   Create a new API project");
        System.out.println("  zupix version      Show version");
        System.out.println("  zupix help         Show help");
    }
}
