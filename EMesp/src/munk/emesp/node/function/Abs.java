package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;

public class Abs extends DifferentialOneVariableFunctionNode {

	public Abs() {
		this(false);
	}
	
	public Abs(boolean negate) {
		super("abs", negate);
	}
	
	public Abs(Expression inner, boolean negate) {
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
		return new Sign(getInner(), isNegate());
	}

}
