/**
 *   Copyright 2013 Royce Remulla
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.github.roycetech.junitcast;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.roycetech.ruleengine.Rule;
import com.github.roycetech.ruleengine.converter.ElementConverter;

/**
 * Represents a fixture for test cases.
 *
 * @param <T> The type of scenario tokens.
 */
public class CaseFixture<T> {

	/** Case description. */
	private final transient String caseDesc;

	/** List of variables. */
	private final transient List<List<T>> variables;

	/** Map of rule token converters. */
	private transient Map<String, ElementConverter> ruleConv;

	/** The rule associated with this fixture. */
	private final transient Rule rule;

	/** Action pair map for binary result fixture. */
	private final transient Map<String, String> pairMap = new ConcurrentHashMap<>();

	/** Action pair reverse map for binary result fixture. */
	private final transient Map<String, String> reversePairMap = new ConcurrentHashMap<>();

	/** Case identifier. */
	private transient List<String> caseId;

	/** Exempt rule. */
	private transient String exemptRule;

	/**
	 * Creates a CaseFixture.
	 *
	 * @param pCaseDesc  The case description.
	 * @param pVariables The case variables.
	 * @param pRule      The source output to rule mapping (e.g.,
	 *                   OUTPUT:true|false).
	 */
	public CaseFixture(final String pCaseDesc, final List<List<T>> pVariables, final Rule pRule) {
		this.caseDesc = pCaseDesc;
		this.variables = pVariables;
		this.rule = pRule;
		this.caseId = Arrays.asList(new String[] { pCaseDesc });
	}

	/**
	 * Creates an instance of this fixture with the necessary components for the test.
	 *
	 * @param pCaseDesc  case description.
	 * @param pVariables case variables.
	 * @param pRule      source output to rule mapping. (e.g. OUTPUT:true|false).
	 * @param pPair      output pair for binary output rules.
	 */
	public CaseFixture(final String pCaseDesc, final List<List<T>> pVariables, final Rule pRule,
			final String pPair) {
		this(pCaseDesc, pVariables, pRule);
//		if (pPair == null)
//			return;

		final String[] pairArr = pPair.split(":");
		this.pairMap.put(pairArr[0], pairArr[1]);
		this.reversePairMap.put(pairArr[1], pairArr[0]);
	}

	/**
	 * Builder pattern.
	 *
	 * @param pPair output pair for binary output rules.
	 * @return this instance of this class with the added pair property.
	 */
	public CaseFixture<T> pair(final String pPair)
	{
		if (pPair != null) {
			final String[] pairArr = pPair.split(":");

			this.pairMap.clear();
			this.reversePairMap.clear();

			this.pairMap.put(pairArr[0], pairArr[1]);
			this.reversePairMap.put(pairArr[1], pairArr[0]);
		}
		return this;
	}

	/**
	 * Builder pattern.
	 *
	 * @param pCaseId Case identifier.
	 * @return this instance of this class with the added case ID property.
	 */
	public CaseFixture<T> caseIdentifier(final List<String> pCaseId)
	{
		this.caseId = pCaseId;
		return this;
	}

	/**
	 * Builder pattern.
	 *
	 * @param pExempt exemption rule.
	 * @return this instance of this class with the added exempt property.
	 */
	public CaseFixture<T> exempt(final String pExempt)
	{
		this.exemptRule = pExempt;
		return this;
	}

//	/**
//	 * Builder pattern.
//	 *
//	 * @param pCaseId Case identifier.
//	 */
//	public CaseFixture<T> caseIdentifier(final String pCaseId)
//	{
//		this.caseId = Arrays.asList(new String[] { pCaseId });
//		return this;
//	}

	/**
	 * Builder pattern.
	 *
	 * @param pConverters Element type converters.
	 * @return this builder instance with the updated property.
	 */
	public CaseFixture<T> convert(final List<ElementConverter> pConverters)
	{
		return this;
	}

	/**
	 * Builder pattern.
	 *
	 * @param pRuleConverter rule token converters.
	 * @return this builder instance with the updated property.
	 */
	public CaseFixture<T> ruleConverter(final Map<String, ElementConverter> pRuleConverter)
	{
		this.ruleConv = pRuleConverter;
		return this;
	}

	/**
	 * Returns the case description.
	 *
	 * @return The case description.
	 */
	public String getCaseDesc()
	{
		return this.caseDesc;
	}

	/**
	 * Returns the rule associated with this fixture.
	 *
	 * @return The rule.
	 */
	public Rule getRule()
	{
		return this.rule;
	}

	/**
	 * Returns the outcome of the rule for a given scenario.
	 *
	 * @param scenario The scenario.
	 * @return The rule outcome.
	 */
	public String getRuleOutcome(final List<String> scenario)
	{
		return this.rule.getRuleOutcome(scenario);
	}

	/**
	 * Gets the list of variable lists.
	 *
	 * @return A list containing variable lists.
	 */
	public List<List<T>> getVariables()
	{
		return this.variables;
	}

	/**
	 * Gets the mapping of action pairs.
	 *
	 * @return A map containing action pairs associated with specific keys.
	 */
	protected Map<String, String> getPairMap()
	{
		return this.pairMap;
	}

	/**
	 * Gets the mapping of reverse action pairs.
	 *
	 * @return A map containing reverse action pairs associated with specific keys.
	 */
	protected Map<String, String> getReversePairMap()
	{
		return this.reversePairMap;
	}

	/**
	 * Gets the list of case identifiers.
	 *
	 * @return A list containing case identifiers.
	 */
	public List<String> getCaseId()
	{
		return this.caseId;
	}


	/**
	 * Retrieves the exemption rule clause.
	 *
	 * @return the exemption rule clause.
	 */
	public String getExemptRule()
	{
		return this.exemptRule;
	}

	/**
	 * Gets the list of case identifiers.
	 *
	 * @return A list containing case identifiers.
	 */
	public Map<String, ElementConverter> getRuleConverter()
	{
		return this.ruleConv;
	}
}