package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;

public class Sin extends DifferentialOneVariableFunctionNode {

	public Sin() {
		this(false);
	}
	
	public Sin(boolean negate) {
		super("sin", negate);
	}
	
	public Sin(Expression inner, boolean negate) {
		this(negate);
		addChild(inner);
	}

	@Override
	public double eval(VariableValues varVal) {
		double innerValue = getInner().eval(varVal);
		
		return Math.sin(innerValue);
	}

	@Override
	public Expression getOuterDerivative(String variable) {
		return new Cos(getInner(), isNegate());
	}

}