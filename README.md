# Reveal Backend Developer Test

## By [Anton Savkin](https://www.linkedin.com/in/anton-savkin-5a8746254/)

### Task

Design Suggestions Service and implement its domain logic according to Hexagonal Architecture. The original text of the assignment can be found [here](https://github.com/reveal-co/hiring/blob/master/backend/README.md). 

### Solution

According to Hexagonal Architecture, the domain logic of the Suggestions Service should be isolated from the outer layer, which consists of other services and adapters.
That is why a group of ports (`CompaniesPort`, `CompaniesPort`, `GrowthPoliciesPort`, `MailerPort`, `PersistencePort`, and `TimerPort`) was created as Java public interfaces. They should be implemented by adapters to provide a channel of communication.

The `DomainLogic` class contains orchestrating logic for the workflow that is used to suggest potential partners to any new joining companies. Also, it's responsible for catching exceptions and transforming them for the common exception handler, which is not implemented.   `DomainLogic` handling methods representing inbound ports.

The `DomainLogicValidator` class contains validating logic for the same workflow.

The `model` package contains data classes and enum for the suggestions' workflow.

Please, find more detailed information about how the designed system works in Javadocs.

### Not Implemented

Some thoughts about refactoring and improving the system.
* Mailing schedule used in `DomainLogic.handleCompanyCreated(..)` can be a list of enum values, if `email_type` and `time_to_wait_before_sending_the_email_in_seconds` correspond. For example, if the mailing of `email_type == 1` always sends after one hour or 3600 seconds, and so on;
* Handling methods of `DomainLogic` may return some status to inform inbound adapters about the results of operation;
* A common exception handler may be implemented.

### Tools used:

1. Java 21
2. SpringBoot 3.2.1
3. Lombok annotations
4. JUnit 5 and Mockito frameworks for testing 


