package fr.savkin.reveal.suggestions.port;

import fr.savkin.reveal.suggestions.exception.MailerException;
import fr.savkin.reveal.suggestions.model.MailingInfo;
import lombok.NonNull;

/**
 * Port for interaction with MailerService.
 * Should be implemented by any MailerAdapter.
 */
public interface MailerPort {

    /**
     * Sends mails according to the mailing information.
     * @param mailingInfo {@link MailingInfo} - data model for title, content and recipients.
     * @throws MailerException depending on implementation.
     */
    void sendMail(@NonNull MailingInfo mailingInfo) throws MailerException;
}
