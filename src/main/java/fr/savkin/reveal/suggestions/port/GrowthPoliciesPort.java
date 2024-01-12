package fr.savkin.reveal.suggestions.port;

import fr.savkin.reveal.suggestions.exception.GrowthPoliciesException;
import fr.savkin.reveal.suggestions.model.Company;
import fr.savkin.reveal.suggestions.model.MailingInfo;

import java.util.Map;
import java.util.Set;

/**
 * Port for interaction with GrowthPoliciesService.
 * Should be implemented by any GrowthPoliciesAdapter.
 */
public interface GrowthPoliciesPort {

    /**
     * Gets a mailing schedule for company by its companyId.
     * @param companyId identifier (or primary key) of a company for which schedule is needed.
     * @return mailing schedule - a map of pairs "Email Type" to "Time before expiration".
     * @throws GrowthPoliciesException depending on implementation.
     */
    Map<Integer, Long> getMailingSchedule(int companyId) throws GrowthPoliciesException; //todo explain why Map

    /** Gets an information needed for mailing: title, content and recipients.
     * @param companyId identifier (or primary key) of a company for which mailing info is needed.
     * @param emailType type of email.
     * @param pendingPartners all partners that are not accepted nor declined.
     * @return {@link MailingInfo} - data model for title, content and recipients.
     * @throws GrowthPoliciesException depending on implementation.
     */
    MailingInfo getMailingInfo(int companyId, int emailType, Set<Company> pendingPartners) throws GrowthPoliciesException;
}
