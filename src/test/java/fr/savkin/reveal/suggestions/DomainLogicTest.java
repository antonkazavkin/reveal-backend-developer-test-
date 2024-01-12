package fr.savkin.reveal.suggestions;

import fr.savkin.reveal.suggestions.exception.*;
import fr.savkin.reveal.suggestions.model.Company;
import fr.savkin.reveal.suggestions.model.MailingInfo;
import fr.savkin.reveal.suggestions.model.enums.PartnershipStatus;
import fr.savkin.reveal.suggestions.port.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DomainLogicTest {

    private DomainLogic tested;

    private CompaniesPort companiesPort;
    private GrowthPoliciesPort growthPoliciesPort;
    private MailerPort mailerPort;
    private PersistencePort persistencePort;
    private TimerPort timerPort;

    private static Company frenchTransportCompany;
    private static Set<Company> validSuggestions;
    private static Map<Integer, Long> validSchedule;
    private static Set<Company> pendingPartners;
    private static MailingInfo mailingInfo;

    @BeforeEach
    void beforeEach() {

        companiesPort = mock(CompaniesPort.class);
        growthPoliciesPort = mock(GrowthPoliciesPort.class);
        mailerPort = mock(MailerPort.class);
        persistencePort = mock(PersistencePort.class);
        timerPort = mock(TimerPort.class);

        tested = new DomainLogic(
            companiesPort,
            growthPoliciesPort,
            mailerPort,
            persistencePort,
            timerPort);
    }

    @BeforeAll
    static void beforeAll() {

        frenchTransportCompany = new Company(423, "France", "Transport");

        Company secondFrenchTransportCompany = new Company(257, "France", "Transport");
        Company thirdFrenchTransportCompany = new Company(258, "France", "Transport");
        Company fourthFrenchTransportCompany = new Company(259, "France", "Transport");

        validSuggestions = new HashSet<>();
        validSuggestions.add(secondFrenchTransportCompany);
        validSuggestions.add(thirdFrenchTransportCompany);
        validSuggestions.add(fourthFrenchTransportCompany);

        validSchedule = new HashMap<>();
        validSchedule.put(1, 3600L);
        validSchedule.put(2, 86400L);
        validSchedule.put(4, 604800L);

        pendingPartners = new HashSet<>();
        pendingPartners.add(secondFrenchTransportCompany);
        pendingPartners.add(fourthFrenchTransportCompany);

        mailingInfo = new MailingInfo("TITLE", "CONTENT", Set.of("RECIPIENT1", "RECIPIENT2"));
    }


    @Test
    void handlePartnershipStatusUpdate_PersistencePortFailed_ExceptionThrown() throws PersistenceException {

        doThrow(new PersistenceException())
            .when(persistencePort)
            .updatePartnershipStatus(anyInt(), anyInt(), any(PartnershipStatus.class));

        Exception exception = assertThrows(
            SuggestionsServiceException.class,
            () -> tested.handlePartnershipStatusUpdate(frenchTransportCompany.getId(), 231, PartnershipStatus.ACCEPTED)
        );

        String expectedMessage = "Partnership suggestions mailing for companyId - 423: Unable to update partnership status.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void handlePartnershipStatusUpdate_PersistencePortOk_ThenPartnershipUpdated() throws PersistenceException, SuggestionsServiceException {

        doNothing()
            .when(persistencePort)
            .updatePartnershipStatus(anyInt(), anyInt(), any(PartnershipStatus.class));

        tested.handlePartnershipStatusUpdate(frenchTransportCompany.getId(), 231, PartnershipStatus.ACCEPTED);

        verify(persistencePort).updatePartnershipStatus(frenchTransportCompany.getId(), 231, PartnershipStatus.ACCEPTED);
    }

    @Test
    void handleCompanyCreated_CompaniesPortFailed_ExceptionThrown() throws CompaniesException {

        doThrow(new CompaniesException())
            .when(companiesPort)
            .getSuggestions(anyString(), anyString());

        Exception exception = assertThrows(
            SuggestionsServiceException.class,
            () -> tested.handleCompanyCreated(frenchTransportCompany)
        );

        String expectedMessage = "Partnership suggestions mailing for companyId - 423: Unable to get partnership suggestions.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void handleCompanyCreated_PersistencePortFailed_ExceptionThrown() throws CompaniesException, PersistenceException {

        doReturn(validSuggestions)
            .when(companiesPort)
            .getSuggestions(anyString(), anyString());

        doThrow(new PersistenceException())
            .when(persistencePort)
            .saveSuggestions(anyInt(), any());

        Exception exception = assertThrows(
            SuggestionsServiceException.class,
            () -> tested.handleCompanyCreated(frenchTransportCompany)
        );

        String expectedMessage = "Partnership suggestions mailing for companyId - 423: Unable to store partnership suggestions.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void handleCompanyCreated_GrowthPoliciesPortFailed_ExceptionThrown() throws GrowthPoliciesException, CompaniesException, PersistenceException {

        doReturn(validSuggestions)
            .when(companiesPort)
            .getSuggestions(anyString(), anyString());

        doNothing()
            .when(persistencePort)
            .saveSuggestions(anyInt(), any());

        doThrow(new GrowthPoliciesException())
            .when(growthPoliciesPort)
            .getMailingSchedule(anyInt());

        Exception exception = assertThrows(
            SuggestionsServiceException.class,
            () -> tested.handleCompanyCreated(frenchTransportCompany)
        );

        String expectedMessage = "Partnership suggestions mailing for companyId - 423: Unable to get mailing schedule.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void handleCompanyCreated_TimerPortFailed_ExceptionThrown() throws GrowthPoliciesException, CompaniesException, PersistenceException, TimerException {

        doReturn(validSuggestions)
            .when(companiesPort)
            .getSuggestions(anyString(), anyString());

        doNothing()
            .when(persistencePort)
            .saveSuggestions(anyInt(), any());

        doReturn(validSchedule)
            .when(growthPoliciesPort)
            .getMailingSchedule(anyInt());

        doThrow(new TimerException())
            .when(timerPort)
            .sendSchedule(anyInt(), any());

        Exception exception = assertThrows(
            SuggestionsServiceException.class,
            () -> tested.handleCompanyCreated(frenchTransportCompany)
        );

        String expectedMessage = "Partnership suggestions mailing for companyId - 423: Unable to set timer.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void handleCompanyCreated_AllOk_AllMethodsWasCalled()
        throws PersistenceException, CompaniesException, GrowthPoliciesException, TimerException, SuggestionsServiceException {

        doReturn(validSuggestions)
            .when(companiesPort)
            .getSuggestions(anyString(), anyString());

        doNothing()
            .when(persistencePort)
            .saveSuggestions(anyInt(), any());

        doReturn(validSchedule)
            .when(growthPoliciesPort)
            .getMailingSchedule(anyInt());

        doNothing()
            .when(timerPort)
            .sendSchedule(anyInt(), any());

        tested.handleCompanyCreated(frenchTransportCompany);

        verify(companiesPort).getSuggestions(frenchTransportCompany.getCountry(), frenchTransportCompany.getIndustry());
        verify(persistencePort).saveSuggestions(frenchTransportCompany.getId(), validSuggestions);
        verify(growthPoliciesPort).getMailingSchedule(frenchTransportCompany.getId());
        verify(timerPort).sendSchedule(frenchTransportCompany.getId(), validSchedule);
    }

    @Test
    void handleTimerExpired_PersistencePortFailed_ExceptionThrown() throws PersistenceException {

        doThrow(new PersistenceException())
            .when(persistencePort)
            .getPartnersByStatus(anyInt(), any(PartnershipStatus.class));

        Exception exception = assertThrows(
            SuggestionsServiceException.class,
            () -> tested.handleTimerExpired(frenchTransportCompany.getId(), 2)
        );

        String expectedMessage = "Partnership suggestions mailing for companyId - 423: Unable to get pending partners.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void handleTimerExpired_NoMorePendingPartners_InfoLogged() throws PersistenceException, SuggestionsServiceException {

        doReturn(Collections.emptySet())
            .when(persistencePort)
            .getPartnersByStatus(anyInt(), any(PartnershipStatus.class));

        Logger domainLogicLogger = (Logger) LoggerFactory.getLogger(DomainLogic.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        domainLogicLogger.addAppender(listAppender);

        tested.handleTimerExpired(frenchTransportCompany.getId(), 2);

        String expectedLog = "Partnership suggestions mailing for companyId - 423: There's no pending partners left, end of mailing.";
        String actualLog = listAppender.list.get(0).getMessage();

        assertEquals(expectedLog, actualLog);
    }

    @Test
    void handleTimerException_GrowthPoliciesPortFailed_ExceptionThrown() throws PersistenceException, GrowthPoliciesException {

        doReturn(pendingPartners)
            .when(persistencePort)
            .getPartnersByStatus(anyInt(), any(PartnershipStatus.class));

        doThrow(new GrowthPoliciesException())
            .when(growthPoliciesPort)
            .getMailingInfo(anyInt(), anyInt(), any());

        Exception exception = assertThrows(
            SuggestionsServiceException.class,
            () -> tested.handleTimerExpired(frenchTransportCompany.getId(), 2)
        );

        String expectedMessage = "Partnership suggestions mailing for companyId - 423: Unable to get title, content and recipients for mailing.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void handleTimerException_MailerPortFailed_ExceptionThrown() throws PersistenceException, GrowthPoliciesException, MailerException {

        doReturn(pendingPartners)
            .when(persistencePort)
            .getPartnersByStatus(anyInt(), any(PartnershipStatus.class));

        doReturn(mailingInfo)
            .when(growthPoliciesPort)
            .getMailingInfo(anyInt(), anyInt(), any());

        doThrow(new MailerException())
            .when(mailerPort)
            .sendMail(any());

        Exception exception = assertThrows(
            SuggestionsServiceException.class,
            () -> tested.handleTimerExpired(frenchTransportCompany.getId(), 2)
        );

        String expectedMessage = "Partnership suggestions mailing for companyId - 423: Unable to send mail.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void handleTimerExpired_AllOk_AllMethodsWasCalled() throws PersistenceException, GrowthPoliciesException, MailerException, SuggestionsServiceException {

        doReturn(pendingPartners)
            .when(persistencePort)
            .getPartnersByStatus(anyInt(), any(PartnershipStatus.class));

        doReturn(mailingInfo)
            .when(growthPoliciesPort)
            .getMailingInfo(anyInt(), anyInt(), any());

        doNothing()
            .when(mailerPort)
            .sendMail(any());

        tested.handleTimerExpired(frenchTransportCompany.getId(), 2);

        verify(persistencePort).getPartnersByStatus(frenchTransportCompany.getId(), PartnershipStatus.PENDING);
        verify(growthPoliciesPort).getMailingInfo(frenchTransportCompany.getId(), 2, pendingPartners);
        verify(mailerPort).sendMail(mailingInfo);
    }
}