package fr.savkin.reveal.suggestions.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Data class for company.
 * Contains id, country and industry.
 */
@Data
@AllArgsConstructor
public class Company {

    private int id;

    @NonNull
    private String country;

    @NonNull
    private String industry;
}
