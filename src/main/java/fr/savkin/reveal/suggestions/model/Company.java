package fr.savkin.reveal.suggestions.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * Data class for company.
 * Contains id, country and industry.
 */
@Data
@AllArgsConstructor
public class Company {

    private int id; //todo explain why name was changed

    @NotEmpty
    private String country;

    @NotEmpty
    private String industry;
}
