package lambda.part1.exercise;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.StringJoiner;

import static org.junit.Assert.assertEquals;

public class Lambdas03Exercise
{

	private interface GenericProduct<T>
	{
		T prod(T a, int i);

		default T twice(T t)
		{
			return prod(t, 2);
		}
	}

	@Test
	public void generic0()
	{
		// Use anonymous class
		final GenericProduct<Integer> prod = new GenericProduct<Integer>()
		{
			@Override
			public Integer prod(Integer a, int i)
			{
				return BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(i)).intValue();
			}
		};

		assertEquals(prod.prod(3, 2), Integer.valueOf(6));
	}

	@Test
	public void generic1()
	{
		// Use statement lambda
		final GenericProduct<Integer> prod = (a, i) -> {
			return BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(i)).intValue();
		};

		assertEquals(prod.prod(3, 2), Integer.valueOf(6));
	}

	@Test
	public void generic2()
	{
		// Use expression lambda
		final GenericProduct<Integer> prod = (a, i) -> BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(i)).intValue();

		assertEquals(prod.prod(3, 2), Integer.valueOf(6));
	}

	private static String stringProd(String s, int i)
	{
		final StringBuilder sb = new StringBuilder();
		for (int j = 0; j < i; j++)
		{
			sb.append(s);
		}
		return sb.toString();
	}

	@Test
	public void strSum()
	{
		final GenericProduct<String> prod = Lambdas03Exercise::stringProd; // use stringProd;

		assertEquals(prod.prod("a", 2), "aa");
	}

	private final String delimeter = "-";

	private String stringSumWithDelimeter(String s, int i)
	{
		final StringJoiner sj = new StringJoiner(delimeter);
		for (int j = 0; j < i; j++)
		{
			sj.add(s);
		}
		return sj.toString();
	}

	@Test
	public void strSum2()
	{
		final GenericProduct<String> prod = this::stringSumWithDelimeter; // use stringSumWithDelimeter;

		assertEquals(prod.prod("a", 3), "a-a-a");
	}

}
