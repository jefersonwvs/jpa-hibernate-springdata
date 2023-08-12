package com.jefersonwvs.app;

import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldHibernateTest {

	@Test
	public void storeLoadMessage() {

		try (
			/*
			 * The SessionFactory created with the createSessionFactory method we previously
			 * defined is passed as an argument to a try with resources, as SessionFactory
			 * implements the AutoCloseable interface.
			 */
			SessionFactory sessionFactory = createSessionFactory();

			/*
			 * Similarly, we begin a new session with the database by creating a Session , which
			 * also implements the AutoCloseable interface. This is our context for all persistence operations.
			 */
			Session session = sessionFactory.openSession())
		{

			/*
			 * Get access to the standard transaction API and begin a transaction on this thread of execution.
			 */
			session.beginTransaction();

			/*
			 * Create a new instance of the mapped domain model class Message , and set its text property.
			 */
			Message message = new Message();
			message.setText("Hello, world! from Hibernate.");

			/*
			 * Enlist the transient instance with the persistence context; we make it persistent.
			 * Hibernate now knows that we wish to store that data, but it doesn't necessarily call
			 * the database immediately. The native Hibernate API is pretty similar to the standard
			 * JPA, and most methods have the same name.
			 */
			session.persist(message);

			/*
			 * Synchronize the session with the database, and close the current session on com-
			 * mit of the transaction automatically.
			 */
			session.getTransaction().commit();

			/*
			 * Begin another transaction. Every interaction with the database should occur
			 * within transaction boundaries, even if we’re only reading data.
			 */
			session.beginTransaction();

			/*
			 * Create an instance of CriteriaQuery by calling the CriteriaBuilder create-
			 * Query() method. A CriteriaBuilder is used to construct criteria queries, com-
			 * pound selections, expressions, predicates, and orderings. A CriteriaQuery defines
			 * functionality that is specific to top-level queries. CriteriaBuilder and Criteria-
			 * Query belong to the Criteria API, which allows us to build a query programmatically.
			 */
			CriteriaQuery<Message> criteriaQuery = session.getCriteriaBuilder().createQuery(Message.class);

			/*
			 * Create and add a query root corresponding to the given Message entity.
			 */
			criteriaQuery.from(Message.class);

			/*
			 * Call the getResultList() method of the query object to get the results. The
			 * query that is created and executed will be SELECT * FROM MESSAGE .
			 */
			List<Message> messages = session.createQuery(criteriaQuery).getResultList();

			/*
			 * Commit the transaction.
			 */
			session.getTransaction().commit();

			/*
			 * We use the JUnit 5 assertAll method, which always checks all the assertions that are passed to it,
			 * even if some of them fail. The two assertions that we verify are conceptually related.
			 */
			assertAll(
				/*
				 * Check the size of the list of messages retrieved from the database.
				 */
				() -> assertEquals(1, messages.size()),

				/*
				 * Check that the message we persisted is in the database.
				 */
				() -> assertEquals("Hello, world! from Hibernate.", messages.get(0).getText()));
		}
	}

	private static SessionFactory createSessionFactory() {
		/*
		 * To create a SessionFactory , we first need to create a configuration.
		 */
		Configuration configuration = new Configuration();

		/*
		 * We need to call the configure method on it and to add Message to it as an annotated class.
		 * The execution of the configure method will load the content of the default hibernate.cfg.xml file.
		 */
		configuration.configure().addAnnotatedClass(Message.class);

		/*
		 * The builder pattern helps us create the immutable service registry and configure it
		 * by applying settings with chained method calls. A ServiceRegistry hosts and manages
		 * services that need access to the SessionFactory . Services are classes that provide
		 * pluggable implementations of different types of functionality to Hibernate.
		 */
		ServiceRegistry serviceRegistry =
			new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

		/*
		 * Build a SessionFactory using the configuration and the service registry we have previously created.
		 */
		return configuration.buildSessionFactory(serviceRegistry);
	}

}
