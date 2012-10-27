package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;

public class Cos extends DifferentialOneVariableFunctionNode {

	public Cos() {
		this(false);
	}
	
	public Cos(boolean negate) {
		super("cos", negate);
	}
	
	public Cos(Expression inner, boolean negate) {
		this(negate);
		addChild(inner);
	}

	@Override
	public double eval(VariableValues varVal) {
		double innerValue = getInner().eval(varVal);
		
		return Math.cos(innerValue);
	}

	@Override
	protected Expression getOuterDerivative(String variable) {
		return new Sin(getInner(), !isNegate());
	}



}
