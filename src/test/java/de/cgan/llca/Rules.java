package de.cgan.llca;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class Rules {
	// see http://en.wikipedia.org/wiki/Life-like_cellular_automaton
	private String gameOfLifeRule = "B3/S23";

	@Test(expected = IllegalArgumentException.class)
	public void empty() {
		new CARule("", "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegal() {
		new CARule("BS", "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void tooLong() {
		new CARule("B0123456789/S", "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void wrongNumber() {
		new CARule("B9/S", "");
	}

	@Test
	public void basic() {
		CARule automaton = new CARule("B/S", "");
		assertThat(automaton.getBornAtRule().size(), is(0));
		assertThat(automaton.getSurviveAtRule().size(), is(0));
	}

	@Test
	public void gameOfLife() {
		CARule automaton = new CARule(gameOfLifeRule, "");
		assertNotNull(automaton.getBornAtRule());
		assertNotNull(automaton.getSurviveAtRule());

		assertThat(automaton.getBornAtRule(), instanceOf(Set.class));
		assertThat(automaton.getSurviveAtRule(), instanceOf(Set.class));
		assertThat(automaton.getBornAtRule().size(), is(1));
		assertThat(automaton.getSurviveAtRule().size(), is(2));
		assertTrue(automaton.getBornAtRule().containsAll(Arrays.asList(3)));
		assertTrue(automaton.getSurviveAtRule().containsAll(Arrays.asList(2, 3)));

		assertThat(automaton.getRuleString(), is(gameOfLifeRule));
	}
}