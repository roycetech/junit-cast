/**
 *
 */
package io.github.roycetech.junitcast.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.github.roycetech.junitcast.JUnitCastException;

/**
 * Utilities for working with the #r class.
 */
public final class RuleUtil {

	/**
	 * Utility class.
	 */
	private RuleUtil() {
	}

	/**
	 * Parse a rule definition mapping of outcome to clause from a rule definition
	 * string.
	 *
	 * @param ruleDefinition the mapping of outcome to clause in a text form.
	 * @return a map instance with the outcome to clause mapping.
	 */
	public static Map<String, Object> parseRuleDefinition(final String ruleDefinition)
	{
		if (ruleDefinition == null) {
			throw new JUnitCastException("Action-to-rule must not be null");
		} else if (ruleDefinition.trim().endsWith(",")) {
			throw new JUnitCastException("Invalid trailing comma detected");
		}

		final Map<String, Object> ruleMap = new LinkedHashMap<>();
		final String[] ruleArr = ruleDefinition.split("~");

		final List<String> duplicates = new ArrayList<>();
		for (final String nextRule : ruleArr) {
			processRuleItem(ruleMap, duplicates, nextRule);
		}
		return ruleMap;
	}

	/**
	 * Refactored out of {@link #parseRuleDefinition(String)}, process the next rule
	 * item from the list.
	 *
	 * @param ruleMap    the rule mapping containing the parsed rules.
	 * @param duplicates the list used for tracking the duplicates.
	 * @param ruleItem   the next rule item to parse.
	 */
	private static void processRuleItem(final Map<String, Object> ruleMap,
			final List<String> duplicates, final String ruleItem)
	{
		if (!ruleItem.contains(":")) {
			throw new JUnitCastException(
					"A colon is required to separate the outcome followed by the rule clause.");
		}

		final String[] actionClauseArr = ruleItem.split(":");
		final boolean isMultiAction = actionClauseArr.length > 2;
		if (isMultiAction) {
			throw new JUnitCastException("Too many colons");
		}

		if (actionClauseArr.length == 1 || "".equals(actionClauseArr[0])) {
			throw new JUnitCastException("Invalid colon placement");
		}

		final String action = actionClauseArr[0].trim();
		checkDuplicates(duplicates, action);
		ruleMap.put(action, actionClauseArr[1].trim());

	}

	/**
	 * Refactored out of {@link #parseRuleDefinition(String)}, validates that the
	 * rule does not fall in to multiple outcomes.
	 *
	 * @param duplicates the list used to monitor the duplicity.
	 * @param outcome    the rule outcome to check.
	 */
	private static void checkDuplicates(final List<String> duplicates, final String outcome)
	{
		if (duplicates.contains(outcome)) {
			throw new JUnitCastException("Duplicate outcomes detected.");
		} else {
			duplicates.add(outcome);
		}

	}

}
