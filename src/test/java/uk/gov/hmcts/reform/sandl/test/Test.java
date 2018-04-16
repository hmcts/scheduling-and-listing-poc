package uk.gov.hmcts.reform.sandl.test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import uk.gov.hmcts.reform.sandl.engine.RulesEngine;
import uk.gov.hmcts.reform.sandl.model.jurisdiction.HearingRole;
import uk.gov.hmcts.reform.sandl.model.person.Person;
import uk.gov.hmcts.reform.sandl.model.problem.Problem;
import uk.gov.hmcts.reform.sandl.model.schedule.Available;
import uk.gov.hmcts.reform.sandl.model.session.Session;
import uk.gov.hmcts.reform.sandl.model.session.SessionParticipant;
import uk.gov.hmcts.reform.sandl.model.transaction.TransactionInsert;
import uk.gov.hmcts.reform.sandl.model.transaction.TransactionRollback;
import uk.gov.hmcts.reform.sandl.model.util.CSVIO;
import uk.gov.hmcts.reform.sandl.model.util.UUIDUtil;

public class Test
{
	public static final String FIXTURE_DIRECTORY = "src/test/resources/test-set-01";
	public static final UUID JUDGE_SMITH_ID = UUID.fromString("00000162-2eee-a7f7-0000-01622eeea7f8");
	public static final UUID JUDGE_JONES_ID = UUID.fromString("00000162-2eee-a7f8-0000-01622eeea7f9");
	public static final UUID JUDGE_ROLE_ID = UUID.fromString("00000162-2632-116c-0000-01622632116d");

	public static void main(String[] args) throws Exception
	{
		new Test().run();
	}

	private final RulesEngine rulesEngine = new RulesEngine();

	public void run() throws Exception
	{
		File fixtureDirectory = new File(FIXTURE_DIRECTORY);
		new CSVIO(rulesEngine).loadAll(fixtureDirectory);
		setUpSessions();
		System.out.println("After Setup\n===========");
		listSessions();
		listProblems();
		System.out.println("====================");
		// Delete session for judge jones on 1 and 2 Jan 2017
		for (Session session : rulesEngine.getFacts(Session.class, s -> s.begin.equals(LocalDateTime.of(2017,1,1,10,0))))
		{
			rulesEngine.retractFact(session);
		}
		for (Session session : rulesEngine.getFacts(Session.class, s -> s.begin.equals(LocalDateTime.of(2017,1,2,10,0))))
		{
			rulesEngine.retractFact(session);
		}
		rulesEngine.fireAllRules();
		System.out.println("After Delete Sessions\n====================");
//		listSessions();
		listProblems();
		System.out.println("====================");
		// Create availability for judge jones between 8 and 11 Jan 2017
		Available available = new Available();
		available.id = UUIDUtil.uuid();
		available.subjectId = JUDGE_JONES_ID;
		available.begin = LocalDateTime.of(2017,1,8,0,0);
		available.end = LocalDateTime.of(2017,1,12,0,0);
		UUID transactionId = UUIDUtil.uuid();
		rulesEngine.insert(new TransactionInsert(transactionId, available));
//		rulesEngine.assertFact(available);
		rulesEngine.fireAllRules();
		System.out.println("After Create Availability\n====================");
//		listSessions();
		listProblems();
		System.out.println("====================");
		rulesEngine.insert(new TransactionRollback(transactionId));
		rulesEngine.fireAllRules();
		System.out.println("After Rollback Create Availability\n====================");
//		listSessions();
		listProblems();
		System.out.println("====================");
	}

	public void listSessions()
	{
		System.out.println("Sessions\n--------");
		int index = 0;
		for (Session session : rulesEngine.getFacts(Session.class, s -> true))
		{
			System.out.print((index++) + " : Session " + session.id + " starts " + session.begin);
			for (SessionParticipant participant : rulesEngine.getFacts(SessionParticipant.class, p -> p.sessionId.equals(session.id)))
			{
				System.out.print(" with role [ ");
				for (HearingRole role : rulesEngine.getFacts(HearingRole.class, r -> r.id.equals(participant.hearingRoleId)))
				{
					System.out.print(role.name + " ");
				}
				System.out.print("] filled by [ ");
				for (Person person : rulesEngine.getFacts(Person.class, p -> p.id.equals(participant.personId)))
				{
					System.out.print(person.name + " ");
				}
				System.out.println("]");
			}
		}
	}

	public void listProblems()
	{
		System.out.println("Problems\n--------");
		for (Problem problem : rulesEngine.getFacts(Problem.class, p -> true))
		{
			System.out.println(problem);
		}
	}

	public void setUpSessions()
	{
		for (int i = 1; i < 32; ++i)
		{
			Session session = createSession(LocalDateTime.of(2017,1,i,10,0), Duration.ofHours(3));
			SessionParticipant participant = createSessionParticipant(session, JUDGE_ROLE_ID, JUDGE_JONES_ID);
			rulesEngine.assertFact(session);
			rulesEngine.assertFact(participant);
		}
		rulesEngine.fireAllRules();
	}

	public Session createSession(LocalDateTime dateTime, Duration duration)
	{
		Session session = new Session();
		session.id = UUIDUtil.uuid();
		session.begin = dateTime;
		session.duration = duration;
		return session;
	}

	public SessionParticipant createSessionParticipant(Session session, UUID hearingRoleId, UUID personId)
	{
		SessionParticipant sessionParticipant = new SessionParticipant();
		sessionParticipant.id = UUIDUtil.uuid();
		sessionParticipant.sessionId = session.id;
		sessionParticipant.hearingRoleId = hearingRoleId;
		sessionParticipant.personId = personId;
		return sessionParticipant;
	}
}
