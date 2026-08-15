package io.zupix;

/** Central runtime configuration for a Zupix application. */
public record ZupixConfig(int port, Profile profile) {
    public static ZupixConfig load() {
        Configuration configuration = new Configuration();
        int port = configuration.getInt("ZUPIX_PORT", configuration.getInt("zupix.port", 8080));
        String rawProfile = configuration.getOrDefault("ZUPIX_PROFILE", "development");
        Profile profile = switch (rawProfile.toLowerCase()) {
            case "test" -> Profile.TEST;
            case "production", "prod" -> Profile.PRODUCTION;
            case "default" -> Profile.DEFAULT;
            default -> Profile.DEVELOPMENT;
        };
        return new ZupixConfig(port, profile);
    }
}
