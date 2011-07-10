package me.m1key.arquillian.repositories;

import me.m1key.arquillian.entities.Dog;

public interface DogRepository {

	Dog getDogByName(String dogName);

	void persistDog(Dog dog);

}