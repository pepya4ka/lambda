package lambda.part3.exercise;

import data.Employee;
import data.JobHistoryEntry;
import data.Person;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class Mapping
{

	private static class MapHelper<T>
	{
		private final List<T> list;

		public MapHelper(List<T> list)
		{
			this.list = list;
		}

		public List<T> getList()
		{
			return list;
		}

		// [T] -> (T -> R) -> [R]
		// [T1, T2, T3] -> (T -> R) -> [R1, R2, R3]
		public <R> MapHelper<R> map(Function<T, R> f)
		{
			final List<R> result = new ArrayList<>();
			list.forEach((T t) -> {
				result.add(f.apply(t));
			});
			return new MapHelper<>(result);
		}

		// map: [T, T, T], T -> [R] => [[], [R1, R2], [R3, R4, R5]]
		// flatMap: [T, T, T], T -> [R] => [R1, R2, R3, R4, R5]
		public <R> MapHelper<R> flatMap(Function<T, List<R>> f)
		{
			final List<R> result = new ArrayList<R>();
			list.forEach((T t) ->
							result.addAll(f.apply(t))
			);

			return new MapHelper<R>(result);
		}
	}

	@Test
	public void mapping()
	{
		final List<Employee> employees =
						Arrays.asList(
										new Employee(
														new Person("a", "Galt", 30),
														Arrays.asList(
																		new JobHistoryEntry(2, "dev", "epam"),
																		new JobHistoryEntry(1, "dev", "google")
														)),
										new Employee(
														new Person("b", "Doe", 40),
														Arrays.asList(
																		new JobHistoryEntry(3, "qa", "yandex"),
																		new JobHistoryEntry(1, "qa", "epam"),
																		new JobHistoryEntry(1, "dev", "abc")
														)),
										new Employee(
														new Person("c", "White", 50),
														Collections.singletonList(
																		new JobHistoryEntry(5, "qa", "epam")
														))
						);

		final List<Employee> mappedEmployees =
						new MapHelper<>(employees)
										.map(e -> e.withPerson(e.getPerson().withFirstName("John")))
										.map(e -> e.withJobHistory(addOneYear(e.getJobHistory())))
										.map(e -> e.withJobHistory(updateJobHistoryEntriesPosition(e.getJobHistory())))
										.getList();

		final List<Employee> expectedResult =
						Arrays.asList(
										new Employee(
														new Person("John", "Galt", 30),
														Arrays.asList(
																		new JobHistoryEntry(3, "dev", "epam"),
																		new JobHistoryEntry(2, "dev", "google")
														)),
										new Employee(
														new Person("John", "Doe", 40),
														Arrays.asList(
																		new JobHistoryEntry(4, "QA", "yandex"),
																		new JobHistoryEntry(2, "QA", "epam"),
																		new JobHistoryEntry(2, "dev", "abc")
														)),
										new Employee(
														new Person("John", "White", 50),
														Collections.singletonList(
																		new JobHistoryEntry(6, "QA", "epam")
														))
						);

		assertEquals(mappedEmployees, expectedResult);
	}

	private List<JobHistoryEntry> addOneYear(List<JobHistoryEntry> jobHistory)
	{
		return new MapHelper<>(jobHistory)
						.map(jobHistoryEntry -> jobHistoryEntry.withDuration(jobHistoryEntry.getDuration() + 1))
						.getList();
	}

	private List<JobHistoryEntry> updateJobHistoryEntriesPosition(List<JobHistoryEntry> jobHistory)
	{
		return new MapHelper<>(jobHistory)
						.map(jobHistoryEntry -> jobHistoryEntry.getPosition().equals("qa") ? jobHistoryEntry.withPosition("QA") : jobHistoryEntry)
						.getList();
	}

	private static class LazyMapHelper<T, R>
	{

		private final List<T> list;
		private final Function<T, R> function;

		public LazyMapHelper(List<T> list, Function<T, R> function)
		{
			this.list = list;
			this.function = function;
		}

		public static <T> LazyMapHelper<T, T> from(List<T> list)
		{
			return new LazyMapHelper<>(list, Function.identity());
		}

		public List<R> force()
		{
			List<R> result = new ArrayList<>();
			list.forEach(element -> result.add(function.apply(element)));
			return result;
		}

		public <R2> LazyMapHelper<T, R2> map(Function<R, R2> f)
		{
			return new LazyMapHelper<>(this.list, element -> f.apply(function.apply(element)));
		}

	}

	private static class LazyFlatMapHelper<T, R>
	{

		public LazyFlatMapHelper(List<T> list, Function<T, List<R>> function)
		{
		}

		public static <T> LazyFlatMapHelper<T, T> from(List<T> list)
		{
			throw new UnsupportedOperationException();
		}

		public List<R> force()
		{
			// TODO
			throw new UnsupportedOperationException();
		}

		// TODO filter
		// (T -> boolean) -> (T -> [T])
		// filter: [T1, T2] -> (T -> boolean) -> [T2]
		// flatMap": [T1, T2] -> (T -> [T]) -> [T2]

		public <R2> LazyFlatMapHelper<T, R2> map(Function<R, R2> f)
		{
			final Function<R, List<R2>> listFunction = rR2TorListR2(f);
			return flatMap(listFunction);
		}

		// (R -> R2) -> (R -> [R2])
		private <R2> Function<R, List<R2>> rR2TorListR2(Function<R, R2> f)
		{
			throw new UnsupportedOperationException();
		}

		// TODO *
		public <R2> LazyFlatMapHelper<T, R2> flatMap(Function<R, List<R2>> f)
		{
			throw new UnsupportedOperationException();
		}
	}


	@Test
	public void lazy_mapping()
	{
		final List<Employee> employees =
						Arrays.asList(
										new Employee(
														new Person("a", "Galt", 30),
														Arrays.asList(
																		new JobHistoryEntry(2, "dev", "epam"),
																		new JobHistoryEntry(1, "dev", "google")
														)),
										new Employee(
														new Person("b", "Doe", 40),
														Arrays.asList(
																		new JobHistoryEntry(3, "qa", "yandex"),
																		new JobHistoryEntry(1, "qa", "epam"),
																		new JobHistoryEntry(1, "dev", "abc")
														)),
										new Employee(
														new Person("c", "White", 50),
														Collections.singletonList(
																		new JobHistoryEntry(5, "qa", "epam")
														))
						);

		final List<Employee> mappedEmployees =
						LazyMapHelper.from(employees)
										.map(e -> e.withPerson(e.getPerson().withFirstName("John")))
										.map(e -> e.withJobHistory(lazyAddOneYear(e.getJobHistory())))
										.map(e -> e.withJobHistory(lazyUpdateJobHistoryEntriesPosition(e.getJobHistory())))
										.force();

		final List<Employee> expectedResult =
						Arrays.asList(
										new Employee(
														new Person("John", "Galt", 30),
														Arrays.asList(
																		new JobHistoryEntry(3, "dev", "epam"),
																		new JobHistoryEntry(2, "dev", "google")
														)),
										new Employee(
														new Person("John", "Doe", 40),
														Arrays.asList(
																		new JobHistoryEntry(4, "QA", "yandex"),
																		new JobHistoryEntry(2, "QA", "epam"),
																		new JobHistoryEntry(2, "dev", "abc")
														)),
										new Employee(
														new Person("John", "White", 50),
														Collections.singletonList(
																		new JobHistoryEntry(6, "QA", "epam")
														))
						);

		assertEquals(mappedEmployees, expectedResult);
	}

	private List<JobHistoryEntry> lazyAddOneYear(List<JobHistoryEntry> jobHistory)
	{
		return LazyMapHelper.from(jobHistory)
						.map(jobHistoryEntry -> jobHistoryEntry.withDuration(jobHistoryEntry.getDuration() + 1))
						.force();
	}

	private List<JobHistoryEntry> lazyUpdateJobHistoryEntriesPosition(List<JobHistoryEntry> jobHistory)
	{
		return LazyMapHelper.from(jobHistory)
						.map(jobHistoryEntry -> jobHistoryEntry.getPosition().equals("qa") ? jobHistoryEntry.withPosition("QA") : jobHistoryEntry)
						.force();
	}

}
