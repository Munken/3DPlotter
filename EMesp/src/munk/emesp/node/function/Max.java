package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;

public class Max extends AbstractFunctionNode{

	public Max() {
		this(false);
	}
	
	public Max(boolean negate) {
		super("max", negate);
	}

	@Override
	protected int nParameters() {
		return 2;
	}

	@Override
	public double eval(VariableValues varVal) {
		if (getNumberChildren() == 0)
			return Double.MAX_VALUE;

		double max = Double.MIN_VALUE;

		for (Expression child : getChildren()) {
			double value = child.eval(varVal);
			if (value > max) {
				max = value;
			}
		}
		return max;
	}

	@Override
	public Expression getDerivative(String variable) {
		throw new UnsupportedOperationException("The derivative of max is not defined");
	}

}
