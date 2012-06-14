package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;

public class Sinh extends DifferentialOneVariableFunctionNode {

	public Sinh() {
		this(false);
	}
	
	public Sinh(boolean negate) {
		super("sinh", negate);
	}
	
	public Sinh(Expression inner, boolean negate) {
		this(negate);
		addChild(inner);
	}

	@Override
	public double eval(VariableValues varVal) {
		double innerValue = getInner().eval(varVal);
		
		return Math.acos(innerValue);
	}

	@Override
	public Expression getOuterDerivative(String variable) {
		return new Cosh(getInner(), isNegate());
	}

}
