package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;

public class Ceil extends AbstractFunctionNode{

	public Ceil() {
		this(false);
	}
	
	public Ceil(boolean negate) {
		super("ceil", negate);
	}

	@Override
	protected int nParameters() {
		return 1;
	}

	@Override
	public double eval(VariableValues varVal) {
		double val = getChild(0).eval(varVal);
		return Math.ceil(val);
	}

	@Override
	public Expression getDerivative(String variable) {
		throw new UnsupportedOperationException("The derivative of ceil is not defined");
	}

}
