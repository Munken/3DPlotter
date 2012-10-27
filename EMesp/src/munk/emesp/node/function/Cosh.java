package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;

public class Cosh extends DifferentialOneVariableFunctionNode {

	public Cosh() {
		this(false);
	}
	
	public Cosh(boolean negate) {
		super("cosh", negate);
	}
	
	public Cosh(Expression inner, boolean negate) {
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
		return new Sinh(getInner(), isNegate());
	}

}
