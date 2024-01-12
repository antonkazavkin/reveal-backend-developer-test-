package fr.savkin.reveal.suggestions.port;

import fr.savkin.reveal.suggestions.exception.PersistenceException;
import fr.savkin.reveal.suggestions.model.enums.PartnershipStatus;
import fr.savkin.reveal.suggestions.model.Company;

import java.util.Set;

/**
 * Port for interaction with persistence provider.
 * Should be implemented by any PersistenceAdapter.
 */
public interface PersistencePort {

    /**
     * Persists an updated status of partnership.
     * @param companyId identifier (or primary key) of a company that triggers an update.
     * @param partnerId identifier (or primary key) of a partner.
     * @param status updated status.
     * @throws PersistenceException depending on implementation.
     */
    void updatePartnershipStatus(int companyId, int partnerId, PartnershipStatus status) throws PersistenceException;

    /**
     * Persists all suggestions for a company.
     * @param companyId identifier (or primary key) of a company for which suggestions are made.
     * @param suggestions set of potential partners.
     * @throws PersistenceException depending on implementation.
     */
    void saveSuggestions(int companyId, Set<Company> suggestions) throws PersistenceException;

    /**
     * Retrieves all partners by partnership status.
     * @param companyId identifier (or primary key) of a company for which partners are retrieving.
     * @param status one of {@link PartnershipStatus}.
     * @return set of partners with corresponding status.
     * @throws PersistenceException depending on implementation.
     */
    Set<Company> getPartnersByStatus(int companyId, PartnershipStatus status) throws PersistenceException;
}
