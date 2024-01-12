package fr.savkin.reveal.suggestions.port;

import fr.savkin.reveal.suggestions.exception.MailerException;
import fr.savkin.reveal.suggestions.model.MailingInfo;
import org.springframework.validation.annotation.Validated;

/**
 * todo Document type MailerPort
 */
public interface MailerPort {

    void sendMail(@Validated MailingInfo mailingInfo) throws MailerException;
}
