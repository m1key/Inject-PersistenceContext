package me.m1key.arquillian.repositories;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import me.m1key.arquillian.entities.Dog;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This test sets the Entity Manager explicitly. This should work without any
 * changes made to Arquillian Weld Embedded.
 * 
 * @author M1key
 * 
 */
@RunWith(Arquillian.class)
public class JpaDogRepositoryExplicitSetTest {

	private static final String SEGA_NAME = "Sega";

	@Inject
	private JpaDogRepository repository;

	private EntityManager entityManager;

	@Deployment
	public static Archive<?> createTestArchive()
			throws IllegalArgumentException, IOException {
		return ShrinkWrap
				.create(WebArchive.class,
						JpaDogRepositoryExplicitSetTest.class.getSimpleName()
								+ ".jar")
				.addAsManifestResource(
						new File("src/test/resources/META-INF/persistence-test.xml"),
						ArchivePaths.create("persistence.xml"))
				.addClasses(Dog.class, JpaDogRepository.class);
	}

	@Before
	public void setup() {
		initialiseEntityManager();
		clearTestData();
		insertTestData();
	}

	@Test
	public void shouldReturnNoDogsByUnknownName() {
		assertNull(repository.getDogByName("unknown-dog"));
	}

	@Test
	public void shouldReturnDogsByKnownName() {
		assertNotNull(repository.getDogByName(SEGA_NAME));
	}

	@Test
	public void shouldSaveAndReturnDogsName() {
		entityManager.getTransaction().begin();
		String seguniaName = "Segunia";
		Dog segunia = new Dog(seguniaName);

		assertNull(repository.getDogByName(seguniaName));

		repository.persistDog(segunia);
		entityManager.getTransaction().commit();
		assertNotNull(repository.getDogByName(seguniaName));
	}
	
	@After
	public void clearTestData() {
		entityManager.getTransaction().begin();
		Query deleteAllQuery = entityManager.createQuery("DELETE FROM Dog");
		deleteAllQuery.executeUpdate();
		entityManager.getTransaction().commit();
	}

	private void initialiseEntityManager() {
		EntityManagerFactory entityManagerFactory = Persistence
				.createEntityManagerFactory("testPu");
		entityManager = entityManagerFactory.createEntityManager();
		repository.setEntityManager(entityManager);
	}

	private void insertTestData() {
		entityManager.getTransaction().begin();
		Dog sega = new Dog(SEGA_NAME);
		entityManager.persist(sega);
		entityManager.getTransaction().commit();
	}

}
