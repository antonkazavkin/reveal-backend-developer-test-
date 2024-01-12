package fr.savkin.reveal.suggestions.exception;

import static fr.savkin.reveal.suggestions.exception.SuggestionsServiceLogsAndMessages.PROCESS_ID;

/**
 * An exception thrown by SuggestionsService.
 */
public class SuggestionsServiceException extends Exception {

    public SuggestionsServiceException(String messageTemplate, int companyId, Throwable cause) {
        super(String.format(PROCESS_ID + messageTemplate, companyId), cause);
    }

    public SuggestionsServiceException(String messageTemplate, int companyId) {
        super(String.format(PROCESS_ID + messageTemplate, companyId));
    }
}
