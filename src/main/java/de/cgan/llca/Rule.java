package de.cgan.llca;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

/**
 * An immutable class representing a rule for Life-like games.
 * 
 * @author migu
 */
public class Rule {
	private static final String		RULE_PATTERN_STR	= "^B([0-8]{0,9})/S([0-8]{0,9})$";
	private static final Pattern	RULE_PATTERN		= Pattern.compile(RULE_PATTERN_STR);
	private Set<Integer>			ruleBorn;
	private Set<Integer>			ruleSurvive;
	public static final Rule LIFE = Rule.createFromString("B3/S23");

	/**
	 * The rule is in Golly/RLE format.
	 * 
	 * {@link http://en.wikipedia.org/wiki/Life-like_cellular_automaton}
	 * 
	 * @param rule
	 * @param fieldSpec
	 */
	private Rule(Set<Integer> ruleBorn, Set<Integer> ruleSurvive) {
		this.ruleBorn = ruleBorn;
		this.ruleSurvive = ruleSurvive;
	}

	public static Rule createFromString(String ruleSpec) {
		checkArgument(!isNullOrEmpty(ruleSpec) && ruleSpec.matches(RULE_PATTERN_STR));
		Matcher matcher = RULE_PATTERN.matcher(ruleSpec);
		if (!matcher.matches()) {
			throw new IllegalArgumentException(String.format("Rule must match the pattern '%s'.", RULE_PATTERN_STR));
		}
		return new Rule(createSet(matcher.group(1)), createSet(matcher.group(2)));
	}

	private static Set<Integer> createSet(String numbers) {
		Set<Integer> set = Sets.newTreeSet();
		for (int i = 0; i < numbers.length(); i++) {
			set.add(Integer.valueOf(numbers.substring(i, i + 1)));
		}
		return Collections.unmodifiableSet(set);
	}

	public Set<Integer> getBornAt() {
		return ruleBorn;
	}

	public Set<Integer> getSurviveAt() {
		return ruleSurvive;
	}

	public String getRuleString() {
		return String.format("B%s/S%s", Joiner.on("").join(ruleBorn), Joiner.on("").join(ruleSurvive));
	}

	@Override
	public int hashCode() {
		return 31 + toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		return this.toString().equals(other.toString());
	}

	@Override
	public String toString() {
		return getRuleString();
	}
}
