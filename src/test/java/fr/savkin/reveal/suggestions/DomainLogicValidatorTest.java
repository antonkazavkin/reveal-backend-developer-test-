package fr.savkin.reveal.suggestions;

import fr.savkin.reveal.suggestions.exception.SuggestionsServiceException;
import fr.savkin.reveal.suggestions.model.Company;
import fr.savkin.reveal.suggestions.model.enums.PartnershipStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DomainLogicValidatorTest {

    static Company frenchTransportCompany;
    static Company otherFrenchTransportCompany;
    static Company frenchEnergyCompany;
    static Company britishTransportCompany;

    @BeforeAll
    static void beforeAll() {

        frenchTransportCompany = new Company(423, "France", "Transport");
        otherFrenchTransportCompany = new Company(521, "France", "Transport");
        frenchEnergyCompany = new Company(87, "France", "Energy");
        britishTransportCompany = new Company(123, "UK", "Transport");
    }

    @Test
    void validatePartnershipStatusUpdate_SameIds_ExceptionThrown() {

        Exception exception = assertThrows(
            SuggestionsServiceException.class,
            () -> DomainLogicValidator.validatePartnershipStatusUpdate(
                frenchTransportCompany.getId(),
                frenchTransportCompany.getId(),
                PartnershipStatus.ACCEPTED)
        );

        String expectedMessage = "Partnership suggestions mailing for companyId - 423: A company suggested for itself.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validatePartnershipStatusUpdate_StatusIsPending_ExceptionThrown() {

        Exception exception = assertThrows(
            SuggestionsServiceException.class,
            () -> DomainLogicValidator.validatePartnershipStatusUpdate(
                frenchTransportCompany.getId(),
                otherFrenchTransportCompany.getId(),
                PartnershipStatus.PENDING)
        );

        String expectedMessage = "Partnership suggestions mailing for companyId - 423: New status should be ACCEPTED or DECLINED, found PENDING.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validatePartnershipStatusUpdate_ValidUpdate_ExceptionNotThrown() {

        assertDoesNotThrow(() -> DomainLogicValidator.validatePartnershipStatusUpdate(
            frenchTransportCompany.getId(),
            otherFrenchTransportCompany.getId(),
            PartnershipStatus.ACCEPTED));
    }

    @Test
    void validateSuggestions_EmptySet_ExceptionThrown() {

        Set<Company> emptySuggestionsSet = Collections.emptySet();

        Exception exception = assertThrows(
            SuggestionsServiceException.class,
            () -> DomainLogicValidator.validateSuggestions(frenchTransportCompany, emptySuggestionsSet)
        );

        String expectedMessage = "Partnership suggestions mailing for companyId - 423: Set of suggestions is empty.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateSuggestions_WrongCountrySuggested_ExceptionThrown() {

        Set<Company> wrongSuggestions = new HashSet<>();
        wrongSuggestions.add(britishTransportCompany);

        Exception exception = assertThrows(
            SuggestionsServiceException.class,
            () -> DomainLogicValidator.validateSuggestions(frenchTransportCompany, wrongSuggestions)
        );

        String expectedMessage = "Partnership suggestions mailing for companyId - 423: Some suggestions don't match by country or industry.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateSuggestions_WrongIndustrySuggested_ExceptionThrown() {

        Set<Company> wrongSuggestions = new HashSet<>();
        wrongSuggestions.add(frenchEnergyCompany);

        Exception exception = assertThrows(
            SuggestionsServiceException.class,
            () -> DomainLogicValidator.validateSuggestions(frenchTransportCompany, wrongSuggestions)
        );

        String expectedMessage = "Partnership suggestions mailing for companyId - 423: Some suggestions don't match by country or industry.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateSuggestions_SuggestedForItself_ExceptionThrown() {

        Set<Company> wrongSuggestions = new HashSet<>();
        wrongSuggestions.add(frenchTransportCompany);

        Exception exception = assertThrows(
            SuggestionsServiceException.class,
            () -> DomainLogicValidator.validateSuggestions(frenchTransportCompany, wrongSuggestions)
        );

        String expectedMessage = "Partnership suggestions mailing for companyId - 423: A company suggested for itself.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateSuggestions_ValidSuggestions_ExceptionNotThrown() {

        Set<Company> validSuggestions = new HashSet<>();
        validSuggestions.add(otherFrenchTransportCompany);

        assertDoesNotThrow(() -> DomainLogicValidator.validateSuggestions(frenchTransportCompany, validSuggestions));
    }

    @Test
    void validateSchedule_EmptySchedule_ExceptionThrown() {

        Map<Integer, Long> emptySchedule = Collections.emptyMap();

        Exception exception = assertThrows(
            SuggestionsServiceException.class,
            () -> DomainLogicValidator.validateSchedule(emptySchedule, frenchTransportCompany.getId())
        );

        String expectedMessage = "Partnership suggestions mailing for companyId - 423: Mailing schedule is empty.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateSchedule_RepeatingTimers_ExceptionThrown() {

        Map<Integer, Long> repeatingTimersSchedule = new HashMap<>();
        repeatingTimersSchedule.put(1, 3600L);
        repeatingTimersSchedule.put(2, 86400L);
        repeatingTimersSchedule.put(4, 86400L);

        Exception exception = assertThrows(
            SuggestionsServiceException.class,
            () -> DomainLogicValidator.validateSchedule(repeatingTimersSchedule, frenchTransportCompany.getId())
        );

        String expectedMessage = "Partnership suggestions mailing for companyId - 423: Mailing schedule contains repeating timers.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateSchedule_ValidSchedule_ExceptionNotThrown() {

        Map<Integer, Long> validSchedule = new HashMap<>();
        validSchedule.put(1, 3600L);
        validSchedule.put(2, 86400L);
        validSchedule.put(4, 604800L);

        assertDoesNotThrow(() -> DomainLogicValidator.validateSchedule(validSchedule, frenchTransportCompany.getId()));
    }
}