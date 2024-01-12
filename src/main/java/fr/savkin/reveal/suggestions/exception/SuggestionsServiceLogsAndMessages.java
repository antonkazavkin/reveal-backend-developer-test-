package fr.savkin.reveal.suggestions.exception;

/**
 * String constants for exception messages and logging.
 */
public class SuggestionsServiceLogsAndMessages {

    public static final String PROCESS_ID = "Partnership suggestions mailing for companyId - %d: ";
    public static final String SUGGESTED_ITSELF = "A company suggested for itself.";
    public static final String WRONG_UPDATED_STATUS = "New status should be ACCEPTED or DECLINED, found PENDING.";
    public static final String UNABLE_TO_STORE_SUGGESTIONS = "Unable to store partnership suggestions.";
    public static final String UNABLE_TO_UPDATE_STATUS = "Unable to update partnership status.";
    public static final String UNABLE_TO_GET_SUGGESTIONS = "Unable to get partnership suggestions.";
    public static final String UNABLE_TO_GET_SCHEDULE = "Unable to get mailing schedule.";
    public static final String UNABLE_TO_SET_TIMER = "Unable to set timer.";
    public static final String UNABLE_TO_GET_PENDING_PARTNERS = "Unable to get pending partners.";
    public static final String UNABLE_TO_GET_MAILING_INFO = "Unable to get title, content and recipients for mailing.";
    public static final String UNABLE_TO_SEND_MAIL = "Unable to send mail.";
    public static final String EMPTY_SUGGESTIONS = "Set of suggestions is empty.";
    public static final String WRONG_SUGGESTIONS = "Some suggestions don't match by country or industry.";
    public static final String EMPTY_SCHEDULE = "Mailing schedule is empty.";
    public static final String REPEATING_TIMERS = "Mailing schedule contains repeating timers.";
    public static final String NO_MORE_PARTNERS = "There's no pending partners left, end of mailing.";
    public static final String UNKNOWN_ERROR = "Something went wrong, the reason is unknown.";

}
