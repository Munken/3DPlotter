package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;

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
