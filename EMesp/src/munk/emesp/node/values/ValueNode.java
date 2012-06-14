package munk.emesp.node.values;

import munk.emesp.*;

public class ValueNode extends AbstractExpression {
	
	public static final ValueNode MINUS_ONE = new ValueNode(-1);
	public static final ValueNode HALF = new ValueNode(0.5);
	public static final ValueNode ZERO = new ValueNode(0);	
	public static final ValueNode ONE = new ValueNode(1);
	public static final ValueNode TWO = new ValueNode(2);
	
	private double value = 0;

	public ValueNode(double d) {
		value = d;
	}

	/**
	Returns the value.
	*/
	public double eval(VariableValues varVal) {
		return value;
	}

	public double getValue() {
		return value;
	}

	@Override
	public Expression getDerivative(String variable) {
		return ZERO;
	}

	@Override
	public void toString(StringBuffer buffer) {
		buffer.append(value);
	}

}
