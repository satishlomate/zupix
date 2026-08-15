package io.zupix;

import java.time.Instant;

/** Small framework logger abstraction; implementation can evolve without changing user APIs. */
public final class ZupixLogger {
    private final String name;

    private ZupixLogger(String name) { this.name = name; }

    public static ZupixLogger get(Class<?> type) { return new ZupixLogger(type.getSimpleName()); }
    public void info(String message) { log("INFO", message, null); }
    public void warn(String message) { log("WARN", message, null); }
    public void error(String message, Throwable error) { log("ERROR", message, error); }

    private void log(String level, String message, Throwable error) {
        String line = Instant.now() + " " + level + " [" + name + "] " + message;
        if ("ERROR".equals(level)) System.err.println(line); else System.out.println(line);
        if (error != null) error.printStackTrace(System.err);
    }
}
