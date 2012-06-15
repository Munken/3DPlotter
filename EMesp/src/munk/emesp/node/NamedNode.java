package munk.emesp.node;

import munk.emesp.AbstractExpression;

public abstract class NamedNode extends AbstractExpression {

	private String name;
	private boolean negate = false;
	
	public NamedNode(String name, boolean negate) {
		setName(name);
		setNegate(negate);
	}

	/**
	Returns true if the term should negate the result before returning it in the eval method.
	*/
	public boolean isNegate() {
		return negate;
	}

	public void setNegate(boolean b) {
		negate = b;
	}

	/**
	Returns the name of the term.
	*/
	public String getName() {
		return name;
	}

	/**
	Sets the name of the term.  Valid names must not begin with a digit or a decimal, and must not contain
	round brackets, operators, commas or whitespace.

	@throws IllegalArgumentException If the name is null or invalid.
	*/
	public void setName(String s) {
		if (s == null)
			throw new IllegalArgumentException("name cannot be null");

		if (!isValidName(s))
			throw new IllegalArgumentException("invalid name: " + s);

		name = s;
	}
	
	private static boolean isValidName(String s) {
		if (s.length() == 0) return false;

		char c = s.charAt(0);

		if (c >= '0' && c <= '9' || c == '.' || c == ',' || c == '(' || c == ')' || c == '^' || c == '*' || c == '/' || c == '+' || c == '-' || c == ' ' || c == '\t' || c == '\n')
			return false;

		for (int i = 1; i < s.length(); i++) {
			c = s.charAt(i);

			if (c == ',' || c == '(' || c == ')' || c == '^' || c == '*' || c == '/' || c == '+' || c == '-' || c == ' ' || c == '\t' || c == '\n')
				return false;
		}

		return true;
	}
}
