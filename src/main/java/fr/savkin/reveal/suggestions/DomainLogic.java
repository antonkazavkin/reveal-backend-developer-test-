package fr.savkin.reveal.suggestions;

import fr.savkin.reveal.suggestions.exception.*;
import fr.savkin.reveal.suggestions.model.Company;
import fr.savkin.reveal.suggestions.model.MailingInfo;
import fr.savkin.reveal.suggestions.model.enums.PartnershipStatus;
import fr.savkin.reveal.suggestions.port.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import java.util.Set;

import static fr.savkin.reveal.suggestions.exception.SuggestionsServiceException.*;

/**
 * todo Document type DomainLogicRefactored
 */
@AllArgsConstructor
@Slf4j
public class DomainLogic {

    private CompaniesPort companiesPort;
    private GrowthPoliciesPort growthPoliciesPort;
    private MailerPort mailerPort;
    private PersistencePort persistencePort;
    private TimerPort timerPort;


    /**
     * This method is called by web-application (controller).
     * We can return ResponseEntity with status code?
     * Bad idea, because an Adapter should be responsible for data formatting.
     *
     *
     *
     * @param companyId
     * @param partnerId
     * @param status
     */
    public void handlePartnershipStatusUpdate(int companyId, int partnerId, PartnershipStatus status) throws SuggestionsServiceException {

        DomainLogicValidator.validatePartnershipStatusUpdate(companyId, partnerId, status);
        try {
            persistencePort.updatePartnershipStatus(companyId, partnerId, status);
        } catch (PersistenceException pe) {
            throw new SuggestionsServiceException(UNABLE_TO_UPDATE_STATUS, companyId, pe);
        }
    }

    /**
     * Possible validations:
     * 1. Suggestions returned have the same country and industry as the initial company.
     * 2. Suggestions Set should not be empty.
     * 3. Schedule should not be empty.
     * 4. Schedule should be correct format:
     * - different email types (fixed by Map)
     * - no same time for two or more timers map.values().validate
     * 5. country or industry is missing
     *
     * @param company
     */
    public void handleCompanyCreated(@Validated Company company) throws SuggestionsServiceException {

        String companyCountry = company.getCountry();
        String companyIndustry = company.getIndustry();
        int companyId = company.getId();

        try {
            Set<Company> companySuggestions = companiesPort.getSuggestions(companyCountry, companyIndustry);
            DomainLogicValidator.validateSuggestions(company, companySuggestions);
            persistencePort.saveSuggestions(companyId, companySuggestions);

            Map<Integer, Long> mailingSchedule = growthPoliciesPort.getMailingSchedule(companyId);
            DomainLogicValidator.validateSchedule(mailingSchedule, companyId);
            timerPort.sendSchedule(companyId, mailingSchedule);

        } catch (CompaniesException ce) {
            throw new SuggestionsServiceException(UNABLE_TO_GET_SUGGESTIONS, companyId, ce);
        } catch (PersistenceException pe) {
            throw new SuggestionsServiceException(UNABLE_TO_STORE_SUGGESTIONS, companyId, pe);
        } catch (GrowthPoliciesException gpe) {
            throw new SuggestionsServiceException(UNABLE_TO_GET_SCHEDULE, companyId, gpe);
        } catch (TimerException te) {
            throw new SuggestionsServiceException(UNABLE_TO_SET_TIMER, companyId, te);
        } // todo catch UnexpectedError
    }

    /**
     * Possible validations:
     * 1. partners list is empty -> stop mailing
     * 2. partners list contains non-pending partners:
     * 3. mailing info is missing: fixed with @NonNull and @NonEmpty
     *
     *
     * @param companyId
     * @param emailType
     */
    public void handleTimerExpired(int companyId, int emailType) throws SuggestionsServiceException {

        try {
            Set<Company> pendingPartners = persistencePort.getPartnersByStatus(companyId, PartnershipStatus.PENDING);
            if (pendingPartners.isEmpty()) {
                log.info(String.format(PROCESS_ID + NO_MORE_PARTNERS, companyId)); //todo rewrite
            } else {
                MailingInfo mailingInfo = growthPoliciesPort.getMailingInfo(companyId, emailType, pendingPartners);
                mailerPort.sendMail(mailingInfo);
            }

        } catch (PersistenceException pe) {
            throw new SuggestionsServiceException(UNABLE_TO_GET_PENDING_PARTNERS, companyId, pe);
        } catch (GrowthPoliciesException gpe) {
            throw new SuggestionsServiceException(UNABLE_TO_GET_MAILING_INFO, companyId, gpe);
        } catch (MailerException me) {
            throw new SuggestionsServiceException(UNABLE_TO_SEND_MAIL, companyId, me);
        } //todo catch UnexpectedError
    }
}
