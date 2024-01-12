package fr.savkin.reveal.suggestions;

import fr.savkin.reveal.suggestions.exception.*;
import fr.savkin.reveal.suggestions.model.Company;
import fr.savkin.reveal.suggestions.model.MailingInfo;
import fr.savkin.reveal.suggestions.model.enums.PartnershipStatus;
import fr.savkin.reveal.suggestions.port.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

import static fr.savkin.reveal.suggestions.exception.SuggestionsServiceLogsAndMessages.*;

/**
 * Contains domain logic for Suggestions Service.
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
     * Updates partnership status for user and partner.
     * Used by WebAdapter.
     * @param companyId identifier of user's company.
     * @param partnerId identifier of partner to update.
     * @param status new status.
     * @throws SuggestionsServiceException if validation failed or interaction with a port went bad.
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
     * Receives a notification about newly joined company and starts the mailing process.
     * Used by CompaniesAdapter.
     * @param company company that just joined Reveal.
     * @throws SuggestionsServiceException if validation failed or interaction with a port went bad.
     */
    public void handleCompanyCreated(@NonNull Company company) throws SuggestionsServiceException {

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
        } catch (Exception e) {
            throw new UnknownError(UNKNOWN_ERROR);
        }
    }

    /**
     * Receives a notification about timer expiration and asks to send mail.
     * @param companyId identifier (or primary key) of a company for which the mailing is processed.
     * @param emailType type of mailing that is used to retrieve correct mailing info.
     * @throws SuggestionsServiceException if interaction with a port went bad.
     */
    public void handleTimerExpired(int companyId, int emailType) throws SuggestionsServiceException {

        try {
            Set<Company> pendingPartners = persistencePort.getPartnersByStatus(companyId, PartnershipStatus.PENDING);
            if (pendingPartners.isEmpty()) {
                log.info(String.format(PROCESS_ID + NO_MORE_PARTNERS, companyId));
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
        } catch (Exception e) {
            throw new UnknownError(UNKNOWN_ERROR);
        }
    }
}
