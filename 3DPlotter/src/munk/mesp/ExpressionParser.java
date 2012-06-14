package munk.mesp;

import munk.mesp.node.function.FunctionNode;
import munk.mesp.node.operator.*;
import munk.mesp.node.values.ValueNode;
import munk.mesp.node.values.VariableNode;
import munk.mesp.struc.Stack;

public class ExpressionParser {
	
	/**
	Returns an expression-tree that represents the expression string.  Returns null if the string is empty.

	@throws ExpressionParseException If the string is invalid.
	*/
	public static Expression parse(String s, FunctionMap functionMap) throws ExpressionParseException {
		if (s == null)
			throw new ExpressionParseException("Expression string cannot be null.", -1);

		return build(s, 0, functionMap);
	}
	
	private static Expression build(String s, int indexErrorOffset, FunctionMap functionMap) {

		// do not remove (required condition for functions with no parameters, e.g. Pi())
		if (s.trim().length() == 0)
			return null;

		Stack s1 = new Stack(); // contains expression nodes
		Stack s2 = new Stack(); // contains open brackets ( and operators ^,*,/,+,-

		boolean term = true; // indicates a term should come next, not an operator
		boolean signed = false; // indicates if the current term has been signed
		boolean negate = false; // indicates if the sign of the current term is negated

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c == ' ' || c == '\t' || c == '\n')
				continue;

			if (term) {
				if (c == '(') {
					if (negate)
						throw new ExpressionParseException("Open bracket found after negate.", i);

					s2.push("(");
				}
				else if (!signed && (c == '+' || c == '-')) {
					signed = true;
					if (c == '-') negate = true; // by default negate is false
				}
				else if (c >= '0' && c <= '9' || c == '.') {

					int j = i + 1;
					while (j < s.length()) {
						c = s.charAt(j);
						if (c >= '0' && c <= '9' || c == '.') j++;

						// code to account for "computerized scientific notation"
						else if (c == 'e' || c == 'E') {
							j++;

							if (j < s.length()) {
								c = s.charAt(j);

								if (c != '+' && c != '-' && (c < '0' || c > '9'))
									throw new ExpressionParseException("Expected digit, plus sign or minus sign but found: " + String.valueOf(c), j + indexErrorOffset);

								j++;
							}

							while (j < s.length()) {
								c = s.charAt(j);
								if (c < '0' || c > '9')
									break;
								j++;
							}
							break;
						}
						else break;
					}

					double d = 0;
					String _d = s.substring(i, j);

					try {
						d = Double.parseDouble(_d);
					} catch (Throwable t) {
						throw new ExpressionParseException("Improperly formatted value: " + _d, i + indexErrorOffset);
					}

					if (negate) d = -d;
					s1.push(new ValueNode(d));
					i = j - 1;

					negate = false;
					term = false;
					signed = false;
				}
				else if (c != ',' && c != ')' && c != '^' && c != '*' && c != '/' && c != '+' && c != '-') {
					int j = i + 1;
					while (j < s.length()) {
						c = s.charAt(j);
						if (c != ',' && c != ' ' && c != '\t' && c != '\n' && c != '(' && c != ')' && c != '^' && c != '*' && c != '/' && c != '+' && c != '-')
							j++;
						else break;
					}

					if (j < s.length()) {
						int k = j;
						while (c == ' ' || c == '\t' || c == '\n') {
							k++;
							if (k == s.length()) break;
							c = s.charAt(k);
						}

						if (c == '(') {
							FunctionNode fn = functionMap.getFunction(s.substring(i, j), negate);
							int b = 1;
							int kOld = k + 1;
							while (b != 0) {
								k++;

								if (k >= s.length()) {
									throw new ExpressionParseException("Missing function close bracket.", i + indexErrorOffset);
								}

								c = s.charAt(k);

								if (c == ')') {
									b--;
								}
								else if (c == '(') {
									b++;
								}
								else if (c == ',' && b == 1) {
									Expression o = build(s.substring(kOld, k), kOld, functionMap);
									if (o == null) {
										throw new ExpressionParseException("Incomplete function.", kOld + indexErrorOffset);
									}
									fn.addChild(o);
									kOld = k + 1;
								}
							}
							Expression o = build(s.substring(kOld, k), kOld, functionMap);
							if (o == null) {
								if (fn.getNumberChildren() > 0) {
									throw new ExpressionParseException("Incomplete function.", kOld + indexErrorOffset);
								}
							}
							else {
								fn.addChild(o);
							}
							s1.push(fn);
							i = k;
						}
						else {
							s1.push(new VariableNode(s.substring(i, j), negate));
							i = k - 1;
						}
					}
					else {
						s1.push(new VariableNode(s.substring(i, j), negate));
						i = j - 1;
					}

					negate = false;
					term = false;
					signed = false;
				}
				else {
					throw new ExpressionParseException("Unexpected character: " + String.valueOf(c), i + indexErrorOffset);
				}
			}
			else {
				if (c == ')') {
					Stack s3 = new Stack();
					Stack s4 = new Stack();
					while (true) {
						if (s2.isEmpty()) {
							throw new ExpressionParseException("Missing open bracket.", i + indexErrorOffset);
						}
						Object o = s2.pop();
						if (o.equals("(")) break;
						s3.addToTail(s1.pop());
						s4.addToTail(o);
					}
					s3.addToTail(s1.pop());

					s1.push(build(s3, s4));
				}
				else if (c == '^' || c == '*' || c == '/' || c == '+' || c == '-') {
					term = true;
					s2.push(String.valueOf(c));
				}
				else {
					throw new ExpressionParseException("Expected operator or close bracket but found: " + String.valueOf(c), i + indexErrorOffset);
				}
			}
		}

		if (s1.size() != s2.size() + 1) {
			throw new ExpressionParseException("Incomplete expression.", indexErrorOffset + s.length());
		}

		return build(s1, s2);
	}

	private static Expression build(Stack s1, Stack s2) {
		Stack s3 = new Stack();
		Stack s4 = new Stack();

		while (!s2.isEmpty()) {
			Object o = s2.removeTail();
			Object o1 = s1.removeTail();
			Object o2 = s1.removeTail();

			if (o.equals("^")) {
				s1.addToTail(new PowerNode((Expression) o1, (Expression) o2));
			}
			else {
				s1.addToTail(o2);
				s4.push(o);
				s3.push(o1);
			}
		}

		s3.push(s1.pop());

		while (!s4.isEmpty()) {
			Object o = s4.removeTail();
			Object o1 = s3.removeTail();
			Object o2 = s3.removeTail();

			if (o.equals("*")) {
				s3.addToTail(new MultiplyNode((Expression) o1, (Expression) o2));
			}
			else if (o.equals("/")) {
				s3.addToTail(new DivideNode((Expression) o1, (Expression) o2));
			}
			else {
				s3.addToTail(o2);
				s2.push(o);
				s1.push(o1);
			}
		}

		s1.push(s3.pop());

		while (!s2.isEmpty()) {
			Object o = s2.removeTail();
			Object o1 = s1.removeTail();
			Object o2 = s1.removeTail();

			if (o.equals("+")) {
				s1.addToTail(new PlusNode((Expression) o1, (Expression) o2));
			}
			else if (o.equals("-")) {
				s1.addToTail(new MinusNode((Expression) o1, (Expression) o2));
			}
			else {
				// should never happen
				throw new ExpressionParseException("Unknown operator: " + o, -1);
			}
		}

		return (Expression) s1.pop();
	}

}
