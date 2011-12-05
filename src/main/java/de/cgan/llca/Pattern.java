package de.cgan.llca;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Pattern {
	private String			name;
	private List<String>	comments;
	private Integer width;
	private Integer height;
	private Rule	rule;

	public Pattern() {
		this.comments = new ArrayList<String>();
	}

	public static Pattern createFromRle(List<String> rleLines) {
		Pattern pattern = new Pattern();
		for (String line : rleLines) {
			if (line.startsWith("#N ")) {
				pattern.setName(line.substring(3));
			} else if (line.startsWith("#C ")) {
				pattern.addComment(line.substring(3));
			} else if (line.startsWith("#")) {
			} else {
				extractWidthHeightAndRule(pattern, line);
			}
		}
		return pattern;
	}

	private static void extractWidthHeightAndRule(Pattern pattern, String line) {
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("^\\s*x\\s*=\\s*(\\d+)\\s*,\\s*y\\s*=\\s*(\\d+)(?:\\s*,\\s*rule\\s*=\\s*(\\S+))?$");
		Matcher matcher = p.matcher(line);
		if (matcher.matches()) {
			pattern.setWidth(Integer.valueOf(matcher.group(1)));
			pattern.setHeight(Integer.valueOf(matcher.group(2)));
			String groupForRule = matcher.group(3);
			if (groupForRule == null) {
				pattern.setRule(Rule.LIFE);
			} else {
				pattern.setRule(Rule.createFromString(groupForRule));
			}
		}
	}

	private void addComment(String comment) {
		this.comments.add(comment);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getComments() {
		return comments;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}
}
