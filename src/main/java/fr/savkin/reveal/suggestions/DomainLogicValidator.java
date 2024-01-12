package fr.savkin.reveal.suggestions;

import fr.savkin.reveal.suggestions.exception.SuggestionsServiceException;
import fr.savkin.reveal.suggestions.model.Company;
import fr.savkin.reveal.suggestions.model.enums.PartnershipStatus;
import fr.savkin.reveal.suggestions.port.CompaniesPort;
import fr.savkin.reveal.suggestions.port.GrowthPoliciesPort;

import java.util.Map;
import java.util.Set;

import static fr.savkin.reveal.suggestions.exception.SuggestionsServiceLogsAndMessages.*;

/**
 * Provides static validation methods for {@link DomainLogic}
 */
public class DomainLogicValidator {

    /**
     * Validates arguments in {@link DomainLogic#handlePartnershipStatusUpdate(int, int, PartnershipStatus)}.
     * Asserts that:
     * <ul>
     *     <li>companyId and partnerId are not the same (by some mistake in WebApplication);</li>
     *     <li>status has been updated indeed.</li>
     * </ul>
     * @param companyId identifier (or primary key) of a company that triggers an update.
     * @param partnerId identifier (or primary key) of a suggested partner that soon to be declined or accepted.
     * @param status updated status of partnership - can be {@link PartnershipStatus#ACCEPTED} or {@link PartnershipStatus#DECLINED}.
     * @throws SuggestionsServiceException if validation has failed.
     */
    static void validatePartnershipStatusUpdate(int companyId, int partnerId, PartnershipStatus status) throws SuggestionsServiceException {

        if (companyId == partnerId) {
            throw new SuggestionsServiceException(SUGGESTED_ITSELF, companyId);
        }

        if (PartnershipStatus.PENDING.equals(status)) {
            throw new SuggestionsServiceException(WRONG_UPDATED_STATUS, companyId);
        }
    }

    /**
     * Validates suggestions in {@link DomainLogic#handleCompanyCreated(Company)} that is received from {@link CompaniesPort#getSuggestions(String, String)}.
     * Asserts that:
     * <ul>
     *     <li>suggestions set is not empty;</li>
     *     <li>all suggestions match by country;</li>
     *     <li>all suggestions match by industry.</li>
     * </ul>
     * @param company company that joined Reveal.
     * @param suggestions set of potential partners for that company.
     * @throws SuggestionsServiceException if validation has failed.
     */
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

    /**
     * Validates schedule in {@link DomainLogic#handleCompanyCreated(Company)} that is received from {@link GrowthPoliciesPort#getMailingSchedule(int)}.
     * Asserts that:
     * <ul>
     *     <li>schedule is not empty;</li>
     *     <li>there is no repeating timers (with same time before expiration).</li>
     * </ul>
     * @param schedule map of pairs "Email Type" to "Time before expiration".
     * @param companyId identifier (or primary key) of a company for which the mailing is processed.
     * @throws SuggestionsServiceException if validation has failed.
     */
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
