package de.cgan.llca;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.io.Resources;

public class PatternTest {
	private static Rule	LIFE	= Rule.createFromString(RuleTests.gameOfLifeRule);

	@Test
	public void shouldCreatePattern() {
		List<String> noLines = Arrays.asList();
		assertNotNull(Pattern.createFromRle(noLines));
	}

	@Test
	public void shouldReadPatternName() throws Exception {
		Pattern pattern = Pattern.createFromRle(Arrays.asList("#N Barge"));
		assertThat(pattern.getName(), equalTo("Barge"));
	}

	@Test
	public void shouldReadOnePatternComment() throws Exception {
		Pattern pattern = Pattern.createFromRle(Arrays.asList("#C Comment"));
		List<String> comments = pattern.getComments();
		assertNotNull(comments);
		assertThat(comments.size(), equalTo(1));
		assertTrue(comments.contains("Comment"));
	}

	@Test
	public void shouldReadMultipleComments() throws Exception {
		Pattern pattern = Pattern.createFromRle(Arrays.asList("#C Comment", "#C Comment #2", "#C Comment #3"));
		List<String> comments = pattern.getComments();
		assertNotNull(comments);
		assertThat(comments.size(), equalTo(3));
		assertThat(comments.get(0), is("Comment"));
		assertThat(comments.get(1), is("Comment #2"));
		assertThat(comments.get(2), is("Comment #3"));
	}

	@Test
	public void shouldReadSize() throws Exception {
		Pattern pattern = Pattern.createFromRle(Arrays.asList("x =4,y =4, rule =B3/S23"));
		assertThat(pattern.getWidth(), equalTo(4));
		assertThat(pattern.getHeight(), equalTo(4));
	}

	@Test
	public void shouldReadSize2() throws Exception {
		Pattern pattern = Pattern.createFromRle(Arrays.asList("x=2, y= 3, rule= B3/S23"));
		assertThat(pattern.getWidth(), equalTo(2));
		assertThat(pattern.getHeight(), equalTo(3));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldDenyIllegalRule() {
		Pattern.createFromRle(Arrays.asList("x=2, y= 3, rule= B23"));
	}

	@Test
	public void shouldReadRule() throws Exception {
		Pattern pattern = Pattern.createFromRle(Arrays.asList("x=2, y= 3, rule= B3/S23"));
		assertNotNull(pattern.getRule());
		assertThat(pattern.getRule().getBornAt().size(), equalTo(1));
		assertTrue(pattern.getRule().getBornAt().containsAll(Arrays.asList(3)));
		assertThat(pattern.getRule().getSurviveAt().size(), equalTo(2));
		assertTrue(pattern.getRule().getSurviveAt().containsAll(Arrays.asList(2, 3)));
	}

	@Test
	public void shouldAssumeLifeRuleAsDefault() {
		Pattern pattern = Pattern.createFromRle(Arrays.asList("x=2, y= 3"));
		assertThat(pattern.getRule(), equalTo(LIFE));
	}

	@Test
	public void shouldRecognizeRule() {
		final String ruleSpec = "B1/S123456";
		Pattern pattern = Pattern.createFromRle(Arrays.asList("x=2, y= 3, rule=" + ruleSpec));
		assertThat(pattern.getRule(), equalTo(Rule.createFromString(ruleSpec)));
	}

	private List<String> readLinesForPattern(String patternName) throws IOException {
		return Resources.readLines(getClass().getResource("/patterns/" + patternName), Charset.forName("UTF-8"));
	}
}
