package lambda.part1.exercise;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import data.Person;

import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class Lambdas02Exercise
{
	@Test
	public void sortPersonsByAge()
	{
		Person[] persons = {
						new Person("name 3", "lastName 3", 20),
						new Person("name 1", "lastName 2", 40),
						new Person("name 2", "lastName 1", 30)
		};

		Arrays.sort(persons, Comparator.comparingInt(Person::getAge));

		assertArrayEquals(persons, new Person[] {
						new Person("name 3", "lastName 3", 20),
						new Person("name 2", "lastName 1", 30),
						new Person("name 1", "lastName 2", 40),
		});
	}

	@Test
	public void findFirstWithAge30()
	{
		List<Person> persons = ImmutableList.of(
						new Person("name 3", "lastName 3", 20),
						new Person("name 1", "lastName 2", 30),
						new Person("name 2", "lastName 1", 30)
		);

		Person person = FluentIterable.from(persons)
						.filter(it -> it.getAge() == 30)
						.first()
						.orNull();

		assertEquals(person, new Person("name 1", "lastName 2", 30));
	}
}
