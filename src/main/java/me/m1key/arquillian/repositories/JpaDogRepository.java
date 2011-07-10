package me.m1key.arquillian.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import me.m1key.arquillian.entities.Dog;

public class JpaDogRepository implements DogRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Dog getDogByName(String dogName) {
		@SuppressWarnings("unchecked")
		List<Dog> dogsByName = entityManager
				.createQuery("from Dog d where d.name = :name")
				.setParameter("name", dogName).getResultList();

		if (dogsByName.isEmpty()) {
			return null;
		} else {
			return dogsByName.get(0);
		}
	}

	@Override
	public void persistDog(Dog dog) {
		entityManager.persist(dog);
	}

	protected void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
