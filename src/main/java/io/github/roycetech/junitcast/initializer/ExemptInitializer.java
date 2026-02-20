/**
 *
 */
package io.github.roycetech.junitcast.initializer;

import com.github.roycetech.ruleengine.utils.StringUtil;

import io.github.roycetech.junitcast.ResourceFixture;
import io.github.roycetech.junitcast.ResourceFixture.ResourceKey;

/**
 * This class initializes the var parameter from the resource fixture. This was
 * refactored out of ResourceFixture.
 */
public class ExemptInitializer implements ResourceParameterInitializer {

	/** Reference to the original resource fixture. */
	private final ResourceFixture resourceFixture;

	/**
	 * Constructs a new ExemptInitializer instance.
	 *
	 * @param resourceFixture The ResourceFixture instance to initialize exempts
	 *                        from.
	 */
	public ExemptInitializer(final ResourceFixture resourceFixture) {
		this.resourceFixture = resourceFixture;
	}

	/**
	 * Initialize the subject variables definition.
	 */
	@Override
	public void initialize()
	{
		String commonExempt = null; // NOPMD: null default, conditionally redefine.
		if (getResourceFixture().getResourceBundle().containsKey(ResourceKey.commonexempt.name())) {
			commonExempt = getResourceFixture().getResourceString(ResourceKey.commonexempt.name());
		}

		for (int i = 0; i < getResourceFixture().getCaseList().size(); i++) {
			initializeCommonExemptions(commonExempt, i);
		}
	}

	/**
	 * Initializes common exemption for a specific case index and updates the
	 * exemption map.
	 *
	 * @param commonExempt The common exemption string.
	 * @param caseIndex    The index of the case.
	 */
	private void initializeCommonExemptions(final String commonExempt, final int caseIndex)
	{
		final StringBuilder exemptRule = new StringBuilder();
		final String caseExempt = computeCaseExempt(caseIndex);

		if (StringUtil.hasValue(commonExempt) && StringUtil.hasValue(caseExempt)) {
			exemptRule.append(String.format("(%s)|%s", commonExempt, caseExempt));
		} else if (StringUtil.hasValue(caseExempt)) {
			exemptRule.append(caseExempt);
		} else if (StringUtil.hasValue(commonExempt)) {
			exemptRule.append(commonExempt);
		}

		if (StringUtil.hasValue(exemptRule.toString())) {
			getResourceFixture().getCaseExemptMap().put(caseIndex, exemptRule.toString());
		}
	}

	/**
	 * Computes and retrieves an exempt case based on the current index.
	 *
	 * @return The exempt case if found in the resource bundle; otherwise, null.
	 */
	private String computeCaseExempt(final int caseIndex)
	{
		final int actualIdx = caseIndex + getResourceFixture().getDebugStart();
		final String key = String.valueOf(ResourceKey.exempt) + actualIdx;
		if (getResourceFixture().getResourceBundle().containsKey(key)) {
			return getResourceFixture().getResourceString(key);
		}

		return null;
	}

	/**
	 * Returns the bound resourceFixture for test-ability.
	 *
	 * @return the bound resourceFixture instance.
	 */
	/* default */ ResourceFixture getResourceFixture()
	{
		return this.resourceFixture;
	}
}
