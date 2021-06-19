package black.bracken.verdirectrenewed.model.error;

public final class InvalidConfigurationError {

    private final String message;

    public InvalidConfigurationError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
