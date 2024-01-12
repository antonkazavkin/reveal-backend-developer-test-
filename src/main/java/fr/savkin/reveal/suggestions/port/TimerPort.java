package fr.savkin.reveal.suggestions.port;

import fr.savkin.reveal.suggestions.exception.TimerException;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

/**
 * todo Document type TimerPort
 */
public interface TimerPort {

    void setTimer(int companyId, Map.Entry<Integer, Long> timer) throws TimerException; // todo delete

    void sendSchedule(int companyId, Map<Integer, Long> schedule) throws TimerException;
}
