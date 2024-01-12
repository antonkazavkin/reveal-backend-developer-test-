package fr.savkin.reveal.suggestions.port;

import fr.savkin.reveal.suggestions.exception.TimerException;

import java.util.Map;

/**
 * Port for interaction with TimerService.
 * Should be implemented by any TimerAdapter.
 */
public interface TimerPort {

    /**
     * Sends mailing schedule to TimerService in order to set timers.
     * @param companyId identifier (or primary key) of a company for which the mailing is processed.
     * @param schedule schedule of timers - a map of pairs "Email Type" to "Time before expiration".
     * @throws TimerException depending on implementation.
     */
    void sendSchedule(int companyId, Map<Integer, Long> schedule) throws TimerException;
}
