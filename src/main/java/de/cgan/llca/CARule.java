package de.cgan.llca;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

public class CARule {
    private static final String RULE_PATTERN_STR = "^B([0-8]{0,9})/S([0-8]{0,9})$";
    private static final Pattern RULE_PATTERN = Pattern.compile(RULE_PATTERN_STR);
    private Set<Integer> ruleBorn;
    private Set<Integer> ruleSurvive;

    /**
     * The rule is in Golly/RLE format.
     *
     * {@link http://en.wikipedia.org/wiki/Life-like_cellular_automaton}
     *
     * @param rule
     * @param fieldSpec
     */
    public CARule(String rule, String fieldSpec) {
        checkNotNull(rule);
        checkArgument(rule.length() > 0 && rule.matches(RULE_PATTERN_STR));
        parseRule(rule);
    }

    private void parseRule(String rule) {
        Matcher matcher = RULE_PATTERN.matcher(rule);
        if (matcher.matches()) {
            ruleBorn = createSet(matcher.group(1));
            ruleSurvive = createSet(matcher.group(2));
        }
    }

    private Set<Integer> createSet(String numbers) {
        Set<Integer> set = Sets.newTreeSet();
        for (int i = 0; i < numbers.length(); i++) {
            set.add(Integer.valueOf(numbers.substring(i, i + 1)));
        }
        return set;
    }

    public Set<Integer> getBornAtRule() {
        return ruleBorn;
    }

    public Set<Integer> getSurviveAtRule() {
        return ruleSurvive;
    }

    public String getRuleString() {
        return String.format("B%s/S%s", Joiner.on("").join(ruleBorn), Joiner.on("").join(ruleSurvive));
    }
}
