package fr.savkin.reveal.suggestions.port;

import fr.savkin.reveal.suggestions.exception.PersistenceException;
import fr.savkin.reveal.suggestions.model.enums.PartnershipStatus;
import fr.savkin.reveal.suggestions.model.Company;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * todo Document type PersistencePort
 */
public interface PersistencePort {

    void updatePartnershipStatus(int companyId, int partnerId, PartnershipStatus status) throws PersistenceException;

    void saveSuggestions(int companyId, Set<Company> suggestions) throws PersistenceException;

    void saveMailingSchedule(int companyId, Set<Map.Entry<Integer, Long>> mailingSchedule) throws PersistenceException;

    Map.Entry<Integer, Long> getNextTimer(int companyId) throws PersistenceException;

    Set<Company> getPartnersByStatus(int companyId, PartnershipStatus status) throws PersistenceException;
}
