package fr.savkin.reveal.suggestions.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * todo Document type Company
 * 1. id or pk?
 * 2. country -> countryCode?
 * 3. industry -> enum
 * todo add validation like @NonNull
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
