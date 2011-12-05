package de.cgan.llca;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class RuleTests {
	// see http://en.wikipedia.org/wiki/Life-like_cellular_automaton
	static final String	gameOfLifeRule	= "B3/S23";

	@Test(expected = IllegalArgumentException.class)
	public void empty() {
		Rule.createFromString("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegal() {
		Rule.createFromString("BS");
	}

	@Test(expected = IllegalArgumentException.class)
	public void tooLong() {
		Rule.createFromString("B0123456789/S");
	}

	@Test(expected = IllegalArgumentException.class)
	public void wrongNumber() {
		Rule.createFromString("B9/S");
	}

	@Test
	public void basic() {
		Rule automaton = Rule.createFromString("B/S");
		assertThat(automaton.getBornAt().size(), is(0));
		assertThat(automaton.getSurviveAt().size(), is(0));
	}

	@Test
	public void gameOfLife() {
		Rule automaton = Rule.createFromString(gameOfLifeRule);
		assertNotNull(automaton.getBornAt());
		assertNotNull(automaton.getSurviveAt());

		assertThat(automaton.getBornAt(), instanceOf(Set.class));
		assertThat(automaton.getSurviveAt(), instanceOf(Set.class));
		assertThat(automaton.getBornAt().size(), is(1));
		assertThat(automaton.getSurviveAt().size(), is(2));
		assertTrue(automaton.getBornAt().containsAll(Arrays.asList(3)));
		assertTrue(automaton.getSurviveAt().containsAll(Arrays.asList(2, 3)));

		assertThat(automaton.getRuleString(), is(gameOfLifeRule));
	}
}