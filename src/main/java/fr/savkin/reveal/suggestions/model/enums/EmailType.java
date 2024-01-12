package fr.savkin.reveal.suggestions.model.enums;

/**
 * todo Document type EmailType
 */
public enum EmailType {

    HOUR(3600L),
    DAY(86400L),
    THREE_DAYS(259200L), //todo explain why it's here
    WEEK(604800L);

    private final long timeInSeconds;

    EmailType(long timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public long getTimeInSeconds() {
        return timeInSeconds;
    }

    //todo maybe all of it is bad idea
    // because no one told us that email type and time are always corresponding
}
