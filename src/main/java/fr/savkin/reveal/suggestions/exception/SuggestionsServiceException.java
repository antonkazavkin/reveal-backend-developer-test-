package fr.savkin.reveal.suggestions.exception;

/**
 * An exception thrown by SuggestionsService.
 */
public class SuggestionsServiceException extends Exception {

    public SuggestionsServiceException(String messageTemplate, int companyId, Throwable cause) {
        super(String.format(SuggestionsServiceLogsAndMessages.PROCESS_ID + messageTemplate, companyId), cause);
    }

    public SuggestionsServiceException(String messageTemplate, int companyId) {
        super(String.format(SuggestionsServiceLogsAndMessages.PROCESS_ID + messageTemplate, companyId));
    }
}
