package fr.savkin.reveal.suggestions.port;

import fr.savkin.reveal.suggestions.exception.GrowthPoliciesException;
import fr.savkin.reveal.suggestions.model.Company;
import fr.savkin.reveal.suggestions.model.MailingInfo;

import java.util.Map;
import java.util.Set;

/**
 * todo Document type GrowthPoliciesPort
 */
public interface GrowthPoliciesPort {

    Map<Integer, Long> getMailingSchedule(int companyId) throws GrowthPoliciesException; //todo explain why Map

    MailingInfo getMailingInfo(int companyId, int emailType, Set<Company> pendingPartners) throws GrowthPoliciesException;
}
