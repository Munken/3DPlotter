package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;

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
