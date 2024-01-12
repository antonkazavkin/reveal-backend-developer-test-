package fr.savkin.reveal.suggestions.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.Set;

/**
 * Data class for mailing info.
 * Contains title, content and set of recipients.
 */
@Data
@AllArgsConstructor
public class MailingInfo {

    @NonNull
    private String title;

    @NonNull
    private String content;

    @NonNull
    private Set<String> recipients;
}
