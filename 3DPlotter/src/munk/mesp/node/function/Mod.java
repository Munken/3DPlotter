package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;

public class Mod extends AbstractFunctionNode{

	public Mod() {
		this(false);
	}
	
	public Mod(boolean negate) {
		super("mod", negate);
	}

	@Override
	protected int nParameters() {
		return 2;
	}

	@Override
	public double eval(VariableValues varVal) {
		return getChild(0).eval(varVal) % getChild(1).eval(varVal);
	}

	@Override
	public Expression getDerivative(String variable) {
		throw new UnsupportedOperationException("The derivative of mod is not defined");
	}

}
