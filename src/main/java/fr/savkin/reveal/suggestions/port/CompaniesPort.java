package fr.savkin.reveal.suggestions.port;

import fr.savkin.reveal.suggestions.exception.CompaniesException;
import fr.savkin.reveal.suggestions.model.Company;

import java.util.Set;

/**
 * Port for interaction with CompaniesService.
 * Should be implemented by any CompaniesAdapter.
 */
public interface CompaniesPort {

    /**
     * Gets a set of potential partners (companies with same {@code country} and {@code industry} fields) from CompaniesService.
     * @param country country of operational activity.
     * @param industry company's industry.
     * @return set of potential partners.
     * @throws CompaniesException depending on implementation.
     */
    Set<Company> getSuggestions(String country, String industry) throws CompaniesException;
}
