package fr.savkin.reveal.suggestions;

import fr.savkin.reveal.suggestions.exception.SuggestionsServiceException;
import fr.savkin.reveal.suggestions.model.Company;
import fr.savkin.reveal.suggestions.model.enums.PartnershipStatus;

import java.util.Map;
import java.util.Set;

import static fr.savkin.reveal.suggestions.exception.SuggestionsServiceException.*;

/**
 * todo Document type DomainLogicValidator
 */
public class DomainLogicValidator {

    static void validatePartnershipStatusUpdate(int companyId, int partnerId, PartnershipStatus status) throws SuggestionsServiceException {

        if (companyId == partnerId) {
            throw new SuggestionsServiceException(SUGGESTED_ITSELF, companyId);
        }

        if (PartnershipStatus.PENDING.equals(status)) {
            throw new SuggestionsServiceException(WRONG_UPDATED_STATUS, companyId);
        }
    }

    static void validateSuggestions(Company company, Set<Company> suggestions) throws SuggestionsServiceException {

        if (suggestions.isEmpty()) {
            throw new SuggestionsServiceException(EMPTY_SUGGESTIONS, company.getId());
        }

        for (Company suggestion: suggestions) {

            if (suggestion.getId() == company.getId()) {
                throw new SuggestionsServiceException(SUGGESTED_ITSELF, company.getId());
            }

            if (!suggestion.getCountry().equals(company.getCountry()) || !suggestion.getIndustry().equals(company.getIndustry())) {
                throw new SuggestionsServiceException(WRONG_SUGGESTIONS, company.getId());
            }
        }
    }

    static void validateSchedule(Map<Integer, Long> schedule, int companyId) throws SuggestionsServiceException {

        if (schedule.isEmpty()) {
            throw new SuggestionsServiceException(EMPTY_SCHEDULE, companyId);
        }

        Set<Long> times = Set.copyOf(schedule.values());
        if (schedule.size() != times.size()) {
            throw new SuggestionsServiceException(REPEATING_TIMERS, companyId);
        }
    }
}
