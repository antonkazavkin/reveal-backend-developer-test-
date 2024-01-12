package fr.savkin.reveal.suggestions.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.Set;

/**
 * todo Document type MailingInfo
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
