package lambda.part2.exercise;

import data.Person;

import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArrowNotationExercise
{

	@Test
	public void getAge()
	{
		// Person -> Integer
		final Function<Person, Integer> getAge = Person::getAge;

		assertEquals(Integer.valueOf(33), getAge.apply(new Person("", "", 33)));
	}

	@Test
	public void compareAges()
	{
		// compareAges: (Person, Person) -> boolean
		final BiPredicate<Person, Person> compareAges = (p1, p2) -> p1.getAge() == p2.getAge();

		assertTrue(compareAges.test(new Person("a", "b", 22), new Person("c", "d", 22)));
	}

	@Test
	public void getAgeOfPersonWithTheLongestFullName()
	{
		// Person -> String
		// getFullName: Person -> String
		final Function<Person, String> getFullName = person -> person.getFirstName() + person.getLastName();

		// (Person, Person) -> Integer
		// ageOfPersonWithTheLongestFullName: (Person -> String) -> (Person, Person) -> int
		final BiFunction<Person, Person, Integer> ageOfPersonWithTheLongestFullName =
						(p1, p2) -> getFullName.apply(p1).length() > getFullName.apply(p2).length() ? p1.getAge() : p2.getAge();

		assertEquals(
						Integer.valueOf(1),
						ageOfPersonWithTheLongestFullName.apply(
										new Person("a", "b", 2),
										new Person("aa", "b", 1)));
	}
}
