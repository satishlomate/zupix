package io.zupix;

/**
 * Entry point for a Zupix application.
 *
 * <p>The public API is intentionally small at this stage. The application
 * lifecycle and HTTP server will be introduced in subsequent milestones.</p>
 */
public final class ZupixApplication {

    private ZupixApplication() {
    }

    /**
     * Creates a new application instance.
     *
     * @return a new Zupix application
     */
    public static ZupixApplication create() {
        return new ZupixApplication();
    }
}
