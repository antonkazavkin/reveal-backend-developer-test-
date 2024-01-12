package fr.savkin.reveal.suggestions.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.Set;

/**
 * Data class for mailing info.
 * Contains title, content and set of recipients.
 */
@Data
@NotNull
@AllArgsConstructor
public class MailingInfo {

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private Set<String> recipients;
}
