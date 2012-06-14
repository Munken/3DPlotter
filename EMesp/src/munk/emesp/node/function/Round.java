package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;

public class Round extends AbstractFunctionNode{

	public Round() {
		this(false);
	}
	
	public Round(boolean negate) {
		super("round", negate);
	}

	@Override
	protected int nParameters() {
		return 1;
	}

	@Override
	public double eval(VariableValues varVal) {
		double val = getChild(0).eval(varVal);
		if (val >= Long.MAX_VALUE || val <= Long.MIN_VALUE)
			return val;

		return Math.round(val);
	}

	@Override
	public Expression getDerivative(String variable) {
		throw new UnsupportedOperationException("The derivative of round is not defined");
	}

}
