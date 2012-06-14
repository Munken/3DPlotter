package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;

public class Min extends AbstractFunctionNode{

	public Min() {
		this(false);
	}
	
	public Min(boolean negate) {
		super("min", negate);
	}

	@Override
	protected int nParameters() {
		return 2;
	}

	@Override
	public double eval(VariableValues varVal) {
		if (getNumberChildren() == 0)
			return Double.MIN_VALUE;

		double min = Double.MAX_VALUE;

		for (Expression child : getChildren()) {
			double value = child.eval(varVal);
			if (value < min) {
				min = value;
			}
		}
		return min;
	}

	@Override
	public Expression getDerivative(String variable) {
		throw new UnsupportedOperationException("");
	}

}
