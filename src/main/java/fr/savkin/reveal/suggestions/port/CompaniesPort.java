package fr.savkin.reveal.suggestions.port;

import fr.savkin.reveal.suggestions.exception.CompaniesException;
import fr.savkin.reveal.suggestions.model.Company;

import java.util.Set;

/**
 * todo Document type CompaniesOutboundPort
 */
public interface CompaniesPort {

    Set<Company> getSuggestions(String country, String industry) throws CompaniesException;
}
