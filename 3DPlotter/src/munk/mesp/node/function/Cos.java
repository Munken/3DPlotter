package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;

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
