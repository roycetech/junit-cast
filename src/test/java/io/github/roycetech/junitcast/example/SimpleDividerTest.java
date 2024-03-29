package io.github.roycetech.junitcast.example;

import java.util.Collection;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;

import io.github.roycetech.junitcast.AbstractTransientValueTestCase;
import io.github.roycetech.junitcast.MockitoHelper;
import io.github.roycetech.junitcast.Parameter;
import io.github.roycetech.junitcast.ParameterGenerator;

/**
 * Test class for SimpleDivider.
 *
 * @author Royce Remulla.
 */
public class SimpleDividerTest extends
	AbstractTransientValueTestCase<SimpleDivider, String, Integer> {

	/**
	 * @param pParameter Data Transfer Object Parameter in Parameterized test.
	 */
	public SimpleDividerTest(final Parameter<String> pParameter)
	{
		super(pParameter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setupTargetObject(final List<Object> constructorParams)
	{
		new MockitoHelper().setupTargetObject(this, constructorParams);
	}

	/**
	 * <pre>
	 * Test data generator.
	 * This method is called by the JUnit parameterized test runner and
	 * returns a Collection of Arrays.  For each Array in the Collection,
	 * each array element corresponds to a parameter in the constructor.
	 * </pre>
	 */
	@Parameters(name = "{0}")
	public static Collection<Object[]> generateData()
	{
		return new ParameterGenerator<String>()
			.genVarData("io.github.roycetech.junitcast.example.SimpleDividerTest");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void prepare()
	{
		for (final String scenarioToken : getParameter().getScenario()) {
			final String[] params = scenarioToken.split("=");
			if ("arg1".equals(params[0])) {
				setTransientValue(0, Integer.parseInt(params[1]));
			} else if ("arg2".equals(params[0])) {
				setTransientValue(1, Integer.parseInt(params[1]));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void execute()
	{
		final int dividend = getTransientValue(0);
		final int divisor = getTransientValue(1);
		try {
			setResult(getMockSubject().divide(dividend, divisor));
		} catch (final IllegalArgumentException iae) {
			setResult("ERROR");
		}
	}

}