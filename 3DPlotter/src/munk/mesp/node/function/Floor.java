package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;

public class Floor extends AbstractFunctionNode{

	public Floor() {
		this(false);
	}
	
	public Floor(boolean negate) {
		super("floor", negate);
	}

	@Override
	protected int nParameters() {
		return 1;
	}

	@Override
	public double eval(VariableValues varVal) {
		double val = getChild(0).eval(varVal);
		return Math.floor(val);
	}

	@Override
	public Expression getDerivative(String variable) {
		throw new UnsupportedOperationException("The derivative of floor is not defined");
	}

}
