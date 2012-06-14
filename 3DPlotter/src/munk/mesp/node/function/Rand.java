package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;

public class Rand extends AbstractFunctionNode{

	public Rand() {
		this(false);
	}
	
	public Rand(boolean negate) {
		super("rand", negate);
	}

	@Override
	protected int nParameters() {
		return 0;
	}

	@Override
	public double eval(VariableValues varVal) {
		return Math.random();
	}

	@Override
	public Expression getDerivative(String variable) {
		throw new UnsupportedOperationException("The derivative of rand is not defined");
	}

}
