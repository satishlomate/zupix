package io.zupix.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Initial Zupix command-line interface. */
public final class ZupixCli {
    private static final String VERSION = "0.1.0";
    private static final List<String> COMMANDS = List.of("new", "run", "version", "help");
    private ZupixCli() {}

    public static List<String> commands() { return COMMANDS; }

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || "help".equals(args[0])) { help(); return; }
        switch (args[0]) {
            case "new" -> requireProjectName(args);
            case "run" -> runProject();
            case "version" -> System.out.println("Zupix CLI " + VERSION);
            default -> { System.err.println("Unknown command: " + args[0]); help(); System.exit(1); }
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
        Files.writeString(root.resolve("README.md"), "# " + name + "\n\nCreated with Zupix CLI.\n\nRun with `zupix run`.\n");
        Files.writeString(root.resolve(".gitignore"), "target/\n.idea/\n.vscode/\n");
        Files.writeString(root.resolve("pom.xml"), pom(name));
        Files.writeString(root.resolve("src/main/java/Application.java"),
                "import io.zupix.Get;\nimport io.zupix.ZupixApp;\nimport io.zupix.ZupixApplication;\n\n@ZupixApp\npublic class Application {\n    @Get(\"/\")\n    public String hello() { return \"Hello Zupix!\"; }\n\n    public static void main(String[] args) throws Exception {\n        try (var app = ZupixApplication.create(new Application(), 8080)) {\n            app.start();\n            System.out.println(\"Zupix running at http://localhost:\" + app.port());\n            Thread.currentThread().join();\n        }\n    }\n}\n");
        System.out.println("Created Zupix project: " + root);
    }

    private static String pom(String name) {
        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>%s</artifactId>
                    <version>0.1.0</version>
                    <properties>
                        <maven.compiler.release>21</maven.compiler.release>
                        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                    </properties>
                    <dependencies>
                        <dependency>
                            <groupId>io.zupix</groupId>
                            <artifactId>zupix-core</artifactId>
                            <version>0.1.0</version>
                        </dependency>
                    </dependencies>
                    <build>
                        <plugins>
                            <plugin>
                                <groupId>org.codehaus.mojo</groupId>
                                <artifactId>exec-maven-plugin</artifactId>
                                <version>3.5.0</version>
                            </plugin>
                        </plugins>
                    </build>
                </project>
                """.formatted(name);
    }

    private static void runProject() throws Exception {
        Path root = Path.of(".").toAbsolutePath().normalize();
        if (!Files.exists(root.resolve("pom.xml"))) throw new IllegalStateException("No pom.xml found in current directory");
        Process process = new ProcessBuilder("mvn", "compile", "exec:java", "-Dexec.mainClass=Application")
                .directory(root.toFile()).inheritIO().start();
        System.exit(process.waitFor());
    }

    private static void help() {
        System.out.println("Zupix CLI " + VERSION);
        System.out.println("  zupix new <name>   Create a new API project");
        System.out.println("  zupix run          Build and run the current API project");
        System.out.println("  zupix version      Show version");
        System.out.println("  zupix help         Show help");
    }
}
